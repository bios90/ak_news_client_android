package com.dimfcompany.aknewsclient.networking

import android.util.Log
import com.dimfcompany.aknewsclient.base.AppClass
import com.dimfcompany.aknewsclient.base.extensions.asOptional
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.logic.utils.NotificationManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class FbMessagingService : FirebaseMessagingService()
{
    @Inject
    lateinit var notofication_manger: NotificationManager

    init
    {
        AppClass.app_component.inject(this)
    }


    override fun onNewToken(new_token: String)
    {
        super.onNewToken(new_token)
        SharedPrefsManager.pref_fb_token.asConsumer().accept(new_token.asOptional())
        subscribeToTopic()
    }

    override fun onMessageReceived(remote_message: RemoteMessage)
    {
        super.onMessageReceived(remote_message)
        notofication_manger.notify(remote_message)
    }

    private fun subscribeToTopic()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("ak_news_clients")
                .addOnSuccessListener(
                    {
                    })
    }
}