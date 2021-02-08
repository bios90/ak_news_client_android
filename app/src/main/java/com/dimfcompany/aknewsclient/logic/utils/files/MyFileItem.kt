package com.dimfcompany.aknewsclient.logic.utils.files

import android.net.Uri
import java.io.File
import android.content.Intent
import android.util.Log
import com.dimfcompany.aknewsclient.base.ObjWithFile
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import okhttp3.MultipartBody
import java.io.Serializable
import java.lang.RuntimeException
import java.util.*

class MyFileItem(private var uri: String) : Serializable, ObjWithImageUrl, ObjWithFile
{
    companion object
    {
        fun createFromData(data: Uri): MyFileItem
        {
            val file: File
            if (FileManager.isContentImage(data))
            {
                val extension = FileManager.getExtensionFromContentUri(data)
                file = FileManager.create_temp_image_file(extension)
            }
            else if (FileManager.isContentPdf(data))
            {
                file = FileManager.create_temp_image_file("pdf")
            }
            else
            {
                val extension = FileManager.getExtensionFromContentUri(data) ?: ""
                file = FileManager.create_temp_image_file(extension)
            }

            FileManager.copyToFileFromIntentData(file, data)
            val myFile = MyFileItem(file.absolutePath)
            myFile.original_name = FileManager.getUriFileName(data)
            return myFile
        }

        fun createFromFile(file: File): MyFileItem
        {
            return MyFileItem(file.absolutePath)
        }
    }

    var original_name: String? = null
    override var image_url: String? = null
        get()
        {
            return uri
        }

    fun getUri(): Uri
    {
        return Uri.parse(uri)
    }

    fun getUriAsString(): String
    {
        return uri
    }

    fun getFile(): File
    {
        return File(getUri().path)
    }

    fun isImage(): Boolean
    {
        return FileManager.isFileImage(uri)
    }

    //Do not forget to add FLAG_GRANT_READ_URI_PERMISSION to both chooser and intent
    fun getUriForShare(): Uri
    {
        return FileManager.uriFromFile(getFile())
    }

    fun toMultiPartData(field_name: String): MultipartBody.Part?
    {
        return getFile().toPartBody(field_name)
    }

    fun getSizeInMb(): Double
    {
        return getFile().length().toDouble() / (1024 * 1024).toDouble()
    }

    fun getFileName(): String
    {
        return getFile().name
    }

    override fun getObjFileName(): String
    {
        return original_name ?: getFileName()
    }

    override fun getObjFileSize(): Double
    {
        return getSizeInMb()
    }
}


