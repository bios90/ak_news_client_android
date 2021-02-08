package com.dimfcompany.aknewsclient.logic.models

import com.dimfcompany.aknewsclient.base.ObjectWithDates
import com.dimfcompany.aknewsclient.base.ObjectWithId
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class ModelUser(
        override var id: Long? = null,
        @SerializedName("created_at")
        override var created: Date? = null,
        @SerializedName("updated_at")
        override var updated: Date? = null,
        @SerializedName("deleted_at")
        override var deleted: Date? = null,
        var first_name: String? = null,
        var last_name: String? = null,
        var email: String? = null,
        var email_verified: Boolean? = false,
        var url_avatar: String? = null,
        var is_admin: Boolean? = false,
) : ObjectWithId, ObjectWithDates
{
    fun getFullName(): String
    {
        return "$last_name $first_name"
    }
}