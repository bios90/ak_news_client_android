package com.dimfcompany.aknewsclient.logic.models.responses

import com.dimfcompany.aknewsclient.logic.models.ModelEvent
import com.google.gson.annotations.SerializedName

class RespEvents
    (
        @SerializedName("data")
        val events: ArrayList<ModelEvent>? = null
) : BaseResponse()
