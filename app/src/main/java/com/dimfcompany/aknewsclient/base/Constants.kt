package com.dimfcompany.aknewsclient.base

class Constants
{
    companion object
    {
        const val SHARED_PREFS = "SharedPrefs"
        const val EXTENSION_PNG = "png"
        const val EXTENSION_JPEG = "jpeg"

        const val FOLDER_APP_ROOT = "ak_news_client"
        const val FOLDER_TEMP_FILES = FOLDER_APP_ROOT + "/temp_files"
        const val FOLDER_PDFS = FOLDER_APP_ROOT + "/pdfs"
    }

    object Urls
    {
        const val test_mode = false
        val URL_BASE: String
            get()
            {
                if (test_mode)
                {
                    return "http://192.168.1.68/akcsl/"
                }
                else
                {
                    return "https://ak-hr.ru/"
                }
            }

        const val REGISTER = "register"
        const val LOGIN = "login"
        const val FORGOT_PASS = "forgotpass"
        const val UPDATE_USER_INFO = "update_user_info"
        const val GET_USER_BY_ID = "get_user_by_id"
        const val INSERT_FILE = "insert_file"
        const val INSERT_OR_UPDATE_NEWS = "insert_or_update_news"
        const val GET_NEWS_BY_ID = "get_news_by_id"
        const val INSERT_OR_UPDATE_EVENT = "insert_or_update_event"
        const val GET_EVENTS = "get_events"
        const val GET_NEWS = "get_news"
        const val GET_EVENT_BY_ID = "get_event_by_id"
        const val DELETE_EVENT = "delete_event"
    }

    object Extras
    {
        const val REGISTER_MADE = "register_made"
        const val EMAIL = "email"
        const val NEWS = "news"
        const val NEWS_ID = "news_id"
        const val EVENT_ID = "event_id"
        const val FILTER_DATA = "filter_data"
        const val MY_PUSH = "my_push"
    }
}