package com.dimfcompany.aknewsclient.logic.models.responses

import com.dimfcompany.aknewsclient.base.MyServerError
import com.dimfcompany.aknewsclient.base.enums.TypeResponseStatus
import com.dimfcompany.aknewsclient.logic.utils.StringManager
import java.lang.RuntimeException

open class BaseResponse(

        var status: TypeResponseStatus = TypeResponseStatus.FAILED,
        var errors: ArrayList<String>? = null
)
{
    fun isFailed(): Boolean
    {
        return status == TypeResponseStatus.FAILED
    }

    fun getErrorMessage(): String?
    {
        if (this.errors != null && this.errors!!.size > 0)
        {
            return StringManager.listOfStringToSingle(this.errors!!)
        }

        return null
    }

    fun getError(): RuntimeException?
    {
        if (this.status == TypeResponseStatus.SUCCESS)
        {
            return null
        }

        val message = getErrorMessage() ?: "Неизвестная ошибка сервера"
        return MyServerError(message)
    }
}