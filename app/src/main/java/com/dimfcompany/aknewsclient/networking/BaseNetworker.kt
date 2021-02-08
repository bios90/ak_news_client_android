package com.dimfcompany.aknewsclient.networking

import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.logic.models.ModelUser
import com.dimfcompany.aknewsclient.logic.models.responses.BaseResponse
import com.dimfcompany.aknewsclient.logic.models.responses.RespUserSingle
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderNet
import com.dimfcompany.aknewsclient.logic.utils.files.MyFileItem
import com.dimfcompany.aknewsclient.logic.utils.files.toBase64
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class BaseNetworker @Inject constructor(val base_vm: BaseVm)
{
    fun makeLogin(email: String, password: String, fb_token: String?, action_success: (ModelUser) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        base_vm.api_auth.login(email, password, fb_token)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.user!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun makePassReset(email: String, action_success: () -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<BaseResponse>()
                .setActionResponseBody(
                    {
                        base_vm.api_auth.forgotPass(email)
                    })
                .setObjClass(BaseResponse::class.java)
                .setBaseVm(base_vm)
                .setActionSuccess(
                    {
                        action_success()
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun makeRegister(first_name: String, last_name: String, email: String, password: String, avatar: MyFileItem?, action_success: () -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        val avatar_as_base64 = avatar?.getFile()?.toBase64(true)
                        base_vm.api_auth.register(first_name, last_name, email, password, avatar_as_base64)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success()
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun updateUserInfo(user_id: Long, first_name: String?, last_name: String?, password: String?, avatar: MyFileItem?, action_success: (ModelUser) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        val avatar_as_base64 = avatar?.getFile()?.toBase64(true)
                        base_vm.api_auth.updateUserInfo(user_id, first_name, last_name, password, avatar_as_base64)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.user!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }
//
//
//    fun getNewsById(id: Long, action_success: (ModelNews) -> Unit, action_error: ((Throwable) -> Unit)? = null)
//    {
//        BuilderNet<RespNewsSingle>()
//                .setActionResponseBody(
//                    {
//                        base_vm.api_news.getNewsById(id)
//                    })
//                .setObjClass(RespNewsSingle::class.java)
//                .setBaseVm(base_vm)
//                .setActionParseChecker(
//                    {
//                        it.news != null
//                    })
//                .setActionSuccess(
//                    {
//                        action_success(it.news!!)
//                    })
//                .setActionError(
//                    {
//                        action_error?.invoke(it)
//                    })
//                .run()
//    }
//
//
//    fun getEvents(filter_data: FilterDataEvent, action_success: (ArrayList<ModelEvent>) -> Unit, action_error: ((Throwable) -> Unit)? = null)
//    {
//        BuilderNet<RespEvents>()
//                .setActionResponseBody(
//                    {
//                        base_vm.api_news.getEventsByFilter(filter_data)
//                    })
//                .setObjClass(RespEvents::class.java)
//                .setBaseVm(base_vm)
//                .setActionParseChecker(
//                    {
//                        it.events != null
//                    })
//                .setActionSuccess(
//                    {
//                        action_success(it.events!!)
//                    })
//                .setActionError(
//                    {
//                        action_error?.invoke(it)
//                    })
//                .run()
//    }
//
//    fun getNews(filter_data: FilterDataNews, action_success: (ArrayList<ModelNews>) -> Unit, action_error: ((Throwable) -> Unit)? = null)
//    {
//        BuilderNet<RespNews>()
//                .setActionResponseBody(
//                    {
//                        base_vm.api_news.getNewsByFilter(filter_data)
//                    })
//                .setObjClass(RespNews::class.java)
//                .setBaseVm(base_vm)
//                .setActionParseChecker(
//                    {
//                        it.news != null
//                    })
//                .setActionSuccess(
//                    {
//                        action_success(it.news!!)
//                    })
//                .setActionError(
//                    {
//                        action_error?.invoke(it)
//                    })
//                .run()
//    }
//
//    fun getEventById(id: Long, action_success: (ModelEvent) -> Unit, action_error: ((Throwable) -> Unit)? = null)
//    {
//        BuilderNet<RespEventSingle>()
//                .setActionResponseBody(
//                    {
//                        base_vm.api_news.getEventById(id)
//                    })
//                .setObjClass(RespEventSingle::class.java)
//                .setBaseVm(base_vm)
//                .setActionParseChecker(
//                    {
//                        it.event != null
//                    })
//                .setActionSuccess(
//                    {
//                        action_success(it.event!!)
//                    })
//                .setActionError(
//                    {
//                        action_error?.invoke(it)
//                    })
//                .run()
//    }
//
}