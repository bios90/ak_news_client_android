package com.dimfcompany.aknewsclient.logic.utils.images

import android.R.attr
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.extensions.runActionWithDelay
import com.dimfcompany.aknewsclient.logic.utils.MessagesManager
import com.dimfcompany.aknewsclient.logic.utils.files.FileManager
import com.dimfcompany.aknewsclient.logic.utils.files.MyFileItem
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ImageCameraManager @Inject constructor(val activity: BaseActivity, val messagesManager: MessagesManager)
{
    enum class TypePick(var action_success: ((MyFileItem) -> Unit), var action_error: (Throwable) -> Unit)
    {
        GALLERY_IMAGE({}, {}),
        CAMERA_IMAGE({}, {}),
        PDF({}, {}),
        ANY({}, {});

        fun getTypeText(): String
        {
            return when (this)
            {
                GALLERY_IMAGE -> "image/*"
                PDF -> "application/pdf"
                ANY -> "*/*"
                else -> throw RuntimeException("**** Error no pick for this type ****")
            }
        }
    }

    fun pickCameraImage(action_success: (MyFileItem) -> Unit)
    {
        val file = FileManager.create_temp_image_file()
        val uri = FileManager.uriFromFile(file)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        activity.startForResult(intent)
        { result ->

            val action =
                    {
                        val my_file_item = MyFileItem.createFromFile(file)
                        action_success(my_file_item)
                    }
            if (file.length() < 1000)
            {
                runActionWithDelay(activity.lifecycleScope, 2000, action)
            }
            else
            {
                action()
            }

        }.onFailed(
            { result ->

            })
    }

    fun pickGalleryImage(action_success: (MyFileItem) -> Unit)
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = TypePick.GALLERY_IMAGE.getTypeText()
        activity.startForResult(intent)
        { result ->

            val data = result.data?.data ?: return@startForResult

            Observable.defer({
                Observable.just(MyFileItem.createFromData(data))
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe({ messagesManager.showProgressDialog() })
                    .doFinally({ messagesManager.dismissProgressDialog() })
                    .subscribe(
                        {
                            action_success(it)
                        },
                        {
                            it.printStackTrace()
                        })

        }.onFailed(
            { result ->

            })
    }


    fun pickPdfFile(action_success: (MyFileItem) -> Unit, action_error: (Throwable) -> Unit)
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.type = TypePick.PDF.getTypeText()
        activity.startForResult(intent)
        { result ->

            val data = result.data?.data ?: return@startForResult

            Observable.defer({
                Observable.just(MyFileItem.createFromData(data))
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe({ messagesManager.showProgressDialog() })
                    .doFinally({ messagesManager.dismissProgressDialog() })
                    .subscribe(
                        {
                            action_success(it)
                        },
                        {
                            it.printStackTrace()
                            action_error.invoke(it)
                        })

        }.onFailed(
            { result ->
                Log.e("ImageCameraManager", "pickPdfFile: here failed!!")
            })
    }

    fun pickAnyFile(action_success: (MyFileItem) -> Unit, action_error: (Throwable) -> Unit)
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.type = TypePick.ANY.getTypeText()
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startForResult(intent)
        { result ->

            val data = result.data?.data ?: return@startForResult

            Observable.defer({
                Observable.just(MyFileItem.createFromData(data))
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe({ messagesManager.showProgressDialog() })
                    .doFinally({ messagesManager.dismissProgressDialog() })
                    .subscribe(
                        {
                            action_success(it)
                        },
                        {
                            it.printStackTrace()
                            action_error.invoke(it)
                        })

        }.onFailed(
            { result ->
                Log.e("ImageCameraManager", "pickPdfFile: here failed!!")
            })
    }
}