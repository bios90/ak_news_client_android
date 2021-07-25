package com.dimfcompany.aknewsclient.logic.models.responses

import com.dimfcompany.aknewsclient.logic.models.ModelNews
import com.google.gson.annotations.SerializedName

class RespNews(
        @SerializedName("data")
        val news: ArrayList<ModelNews>? = null
) : BaseResponse()