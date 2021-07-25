package com.dimfcompany.aknewsclient.logic.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.AppClass
import com.dimfcompany.aknewsclient.base.BusMainEvents
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.enums.TypePush
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.ui.act_main.ActMain
import com.google.firebase.messaging.RemoteMessage
import java.io.Serializable
import java.lang.RuntimeException
import javax.inject.Inject

class MyPush(val type: TypePush,
             val event_id: Long? = null,
             val news_names: String? = null
) : Serializable
{
    companion object
    {
        fun initFromRemoteMessage(remote: RemoteMessage): MyPush?
        {
            val type_str = remote.getString("type") ?: return null
            val type = TypePush.initFromSting(type_str) ?: return null

            val event_id = remote.getLong("event_id")
            val news_names = remote.getString("news_names")

            return MyPush(type, event_id, news_names)
        }
    }

    fun getTitle(): String
    {
        return when (type)
        {
            TypePush.NEWS_ADDED -> "Добавлены новости"
            else -> throw RuntimeException("**** Error not showing notify for this type ****")
        }
    }

    fun getText(): String
    {
        return news_names ?: "Откройте для просмотра"
    }
}

class NotificationManager @Inject constructor()
{
    fun notify(remoteMessage: RemoteMessage)
    {
        val my_push = MyPush.initFromRemoteMessage(remoteMessage) ?: return
        if (my_push.type == TypePush.BAN)
        {
            BusMainEvents.ps_to_logout.onNext(Any())
            return
        }

        if (SharedPrefsManager.pref_current_user.get().value == null)
        {
            return
        }

        Log.e("NotificationManager", "notify: Got push ${my_push.toJsonMy()}")

        val notification = getNotification(my_push)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            showHighNotification(notification)
        }
        else
        {
            showLowNotification(notification)
        }
    }

    fun getNotification(my_push: MyPush): Notification
    {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val style = NotificationCompat.BigTextStyle().bigText(my_push.getText())

        val builder = NotificationCompat.Builder(AppClass.app, getStringMy(R.string.notify_chanel_aknews))
                .setSmallIcon(R.drawable.ei_kei_logo)
                .setLargeIcon(BitmapFactory.decodeResource(AppClass.app.getResources(), R.drawable.ei_kei_logo))
                .setContentTitle(my_push.getTitle())
                .setContentText(my_push.getText())
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getIntentFromPush(my_push))
                .setAutoCancel(true)
                .setStyle(style)
                .setOnlyAlertOnce(true)
                .setSound(sound)

        return builder.build()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun showHighNotification(notification: Notification)
    {
        val notificationChannel = NotificationChannel(getStringMy(R.string.notify_chanel_aknews), getStringMy(R.string.notify_chanel_aknews), NotificationManager.IMPORTANCE_HIGH)

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

        notificationChannel.description = getStringMy(R.string.notify_chanel_aknews)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.setSound(sound, att)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setShowBadge(false)

        val notificationManager = AppClass.app.getSystemService(android.app.NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)
        notificationManager?.notify((0..999).random(), notification)
    }

    private fun showLowNotification(notification: Notification)
    {
        val notificationManagerCompat = NotificationManagerCompat.from(AppClass.app)
        notificationManagerCompat.notify((0..999).random(), notification)
    }


    fun getIntentFromPush(my_push: MyPush): PendingIntent
    {
        val intent = Intent(AppClass.app, ActMain::class.java)
        intent.makeClearAllPrevious()
        intent.putExtra(Constants.Extras.MY_PUSH, my_push)

        val pendingIntent = PendingIntent.getActivity(AppClass.app, (0..999).random(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }
}