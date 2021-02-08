package com.dimfcompany.aknewsclient.logic.models

import android.webkit.URLUtil
import com.dimfcompany.aknewsclient.base.ObjWithFile
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.base.ObjectWithDates
import com.dimfcompany.aknewsclient.base.ObjectWithId
import com.dimfcompany.aknewsclient.logic.utils.StringManager
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ModelFile
    (
        override var id: Long?,
        @SerializedName("created_at")
        override var created: Date?,
        @SerializedName("updated_at")
        override var updated: Date?,
        @SerializedName("deleted_at")
        override var deleted: Date?,
        var file_name: String? = null,
        var file_original_name: String? = null,
        var file_mime_type: String? = null,
        var file_size: Long? = null,
        var url: String? = null
) : Serializable, ObjectWithId, ObjectWithDates, ObjWithImageUrl, ObjWithFile
{
    override val image_url: String?
        get() = url

    override fun getObjFileName(): String
    {
        return file_original_name ?: file_name ?: "-"
    }

    override fun getObjFileSize(): Double
    {
        return (file_size ?: 0).toDouble() / (1024 * 1024).toDouble()
    }

    fun getNameForDownloading():String
    {
        if(file_original_name != null)
        {
            return file_original_name!!
        }
        else if(file_name != null)
        {
            return file_name!!
        }
        else if(url != null)
        {
            return URLUtil.guessFileName(url, null, null)
        }
        else
        {
            return StringManager.getNameForNewFile("")
        }
    }
}