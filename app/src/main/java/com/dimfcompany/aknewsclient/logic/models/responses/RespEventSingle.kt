package com.dimfcompany.aknewsclient.logic.models.responses

import com.dimfcompany.aknewsclient.logic.models.ModelEvent
import com.google.gson.annotations.SerializedName

class RespEventSingle
    (
        @SerializedName("data")
        val event: ModelEvent? = null
) : BaseResponse()
