package com.dimfcompany.aknewsclient.base

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.dimfcompany.aknewsclient.di.application.ComponentApp
import com.dimfcompany.aknewsclient.di.application.DaggerComponentApp
import com.dimfcompany.aknewsclient.logic.utils.DateManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
//Todo remove forgot pass button
class AppClass: DaggerApplication()
{
    companion object
    {
        lateinit var app: AppClass
        var app_component: ComponentApp = DaggerComponentApp.builder().build()
        var top_activity: AppCompatActivity? = null
        lateinit var gson: Gson
    }

    override fun onCreate()
    {
        super.onCreate()
        app = this
        gson = GsonBuilder()
                .setDateFormat(DateManager.FORMAT_FOR_SERVER_LARAVEL)
                .create()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
    {
        return app_component
    }
}