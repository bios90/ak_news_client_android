package com.dimfcompany.aknewsclient.base.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.dimfcompany.aknewsclient.base.AppClass
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.Serializable

data class Optional<T>(val value: T?)

fun <T> T?.asOptional() = Optional(this)

fun runActionWithDelay(scope: CoroutineScope = GlobalScope, delay: Int, action: () -> Unit, error_action: ((Throwable) -> Unit)? = null, dispatcher: CoroutineDispatcher = Dispatchers.Main): Job
{
    val handler = CoroutineExceptionHandler(
        { context, throwable ->
            error_action?.invoke(throwable)
        })

    return scope.launch(context = handler + dispatcher, block =
    {
        delay(delay.toLong())
        action()
    })
}

fun runRepeatingAction(scope: CoroutineScope = GlobalScope, interval: Int, action: (Int) -> Unit, max_repeat: Int = 40, inital_delay: Int = 0, error_action: ((Throwable) -> Unit)? = null, dispatcher: CoroutineDispatcher = Dispatchers.Main): Job
{
    val handler = CoroutineExceptionHandler(
        { context, throwable ->
            error_action?.invoke(throwable)
        })

    return scope.launch(handler + dispatcher, block =
    {
        if (inital_delay > 0)
        {
            delay(inital_delay.toLong())
        }

        for (i in 0..max_repeat)
        {
            action(i)
        }
    })
}


fun getColorMy(id: Int): Int
{
    return ContextCompat.getColor(AppClass.app, id)
}

fun getDimenMy(id: Int): Float
{
    return AppClass.app.resources.getDimension(id)
}

fun dp2pxInt(dp: Int): Int
{
    return dp2px(dp.toFloat()).toInt()
}

fun dp2pxInt(dp: Float): Int
{
    return dp2px(dp).toInt()
}

fun dp2px(dp: Float): Float
{
    val r = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
}

fun getStringMy(id: Int): String
{
    return AppClass.app.getResources().getString(id)
}

fun getStringMy(id: Int, text: String): String
{
    return AppClass.app.getResources().getString(id, text)
}

fun getStringMy(id: Int, vararg values: Any): String
{
    return AppClass.app.getResources().getString(id, *values)
}

fun Int.applyTransparency(percent: Int): Int
{
    val alpha = (255 * percent) / 100
    val new_color = ColorUtils.setAlphaComponent(this, alpha)
    return new_color
}

fun Int.darken(ratio: Float = 0.2f): Int
{
    return ColorUtils.blendARGB(this, Color.BLACK, ratio)
}

fun Intent.myPutExtra(name: String, obj: Any?)
{
    if (obj is Boolean)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Byte)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Char)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Short)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Int)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Long)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Float)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Double)
    {
        this.putExtra(name, obj)
    }
    else if (obj is String)
    {
        this.putExtra(name, obj)
    }
    else if (obj is CharSequence)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Parcelable)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Array<*>)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Serializable)
    {
        this.putExtra(name, obj)
    }
    else if (obj is BooleanArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is ByteArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is ShortArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is CharArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is IntArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is LongArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is FloatArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is DoubleArray)
    {
        this.putExtra(name, obj)
    }
    else if (obj is Bundle)
    {
        this.putExtra(name, obj)
    }
    else
    {
        throw RuntimeException("**** Error unknown type to put as Extra ****")
    }
}

fun Intent.getIntExtraMy(name: String): Int?
{
    val num = this.getIntExtra(name, -999999)
    if (num == -999999)
    {
        return null
    }
    return num
}

fun Intent.getLongExtraMy(name: String): Long?
{
    val num = this.getLongExtra(name, -999999L)
    if (num == -999999L)
    {
        return null
    }
    return num
}

fun openAppSettings()
{
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.data = Uri.parse("package:" + AppClass.app.packageName)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    AppClass.app.startActivity(intent)
}


fun isNetworkAvailable(): Boolean
{
    val connectivityManager = AppClass.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when
        {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
    else
    {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun Intent.applyShareFlags()
{
    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    this.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}

fun Intent.makeClearAllPrevious()
{
    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}

fun openAnyFile(uri: Uri)
{
    try
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.applyShareFlags()
        val chooser = Intent.createChooser(intent, "Открыть с помощью:")
        chooser.applyShareFlags()
        AppClass.app.startActivity(chooser)
    }
    catch (e: Exception)
    {

    }
}

fun Any?.toJsonMy(): String?
{
    if (this == null)
    {
        return null
    }

    val json = AppClass.gson.toJson(this)
    if (json.equals("null") || json.equals("\"null\""))
    {
        return null
    }

    return json
}

fun hideKeyboard()
{
    if (AppClass.top_activity != null)
    {
        val view = AppClass.top_activity!!.currentFocus
        view?.let({ v ->
            val imm = AppClass.top_activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        })
    }
    else
    {
        val imm = AppClass.app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}

fun Response<ResponseBody>?.getBodyAsStr(): String?
{
    if (this?.code() == 200)
    {
        return this.body()?.string()
    }

    return this?.errorBody()?.string()
}

fun <T> String?.toObjOrNullGson(obj_class: Class<T>): T?
{
    val gson = AppClass.gson
    try
    {
        if (this?.equals("null") == true || this?.equals("\"null\"") == true)
        {
            return null
        }

        return gson.fromJson(this, obj_class)
    }
    catch (e: Exception)
    {
        e.printStackTrace()
        return null
    }
}

fun getDeviceName(): String
{
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer))
    {
        model.capitalize()
    }
    else
    {
        manufacturer.capitalize() + " " + model
    }
}

fun getStatusBarHeight():Int
{
    var result = 0
    val resources = AppClass.app.resources
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
    {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun View.setHeight(heightToSet: Int)
{
    this.getLayoutParams().height = heightToSet
    this.requestLayout()
}

fun View.setWidth(widthToSet: Int)
{
    this.layoutParams.width = widthToSet
    this.requestLayout()
}

fun emailIntent(whom: String?, text: String?, subj: String?)
{
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
        "mailto", whom, null))
    intent.putExtra(Intent.EXTRA_SUBJECT, subj)
    intent.putExtra(Intent.EXTRA_TEXT, text);

    val chooser = Intent.createChooser(intent, "Отправить email")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try
    {
        AppClass.app.startActivity(chooser)
    }
    catch (e: java.lang.Exception)
    {
        e.printStackTrace()
    }
}

fun <T> HashSet<T>.addOrRemove(item: T)
{
    if (this.contains(item))
    {
        this.remove(item)
    }
    else
    {
        this.add(item)
    }
}

fun RemoteMessage.getInt(key:String):Int?
{
    val data = this.getData() as Map<String, String>
    return data.get(key)?.toIntOrNull()
}

fun RemoteMessage.getLong(key:String):Long?
{
    val data = this.getData() as Map<String, String>
    return data.get(key)?.toLongOrNull()
}

fun RemoteMessage.getString(key:String):String?
{
    val data = this.getData() as Map<String, String>
    return data.get(key)
}