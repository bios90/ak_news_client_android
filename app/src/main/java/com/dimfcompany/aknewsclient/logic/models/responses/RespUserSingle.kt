package com.dimfcompany.aknewsclient.logic.models.responses

import com.dimfcompany.aknewsclient.logic.models.ModelUser
import com.google.gson.annotations.SerializedName

class RespUserSingle(
        @SerializedName("data")
        val user: ModelUser? = null
) : BaseResponse()