package com.dimfcompany.aknewsclient.logic.models

import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.enums.TypeEventCategory
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.dimfcompany.aknewsclient.logic.utils.formatToString
import java.io.Serializable
import java.util.*

class FilterDataEvent(
        var search_text: String? = null,
        var date_from: Date? = null,
        var date_to: Date? = null,
        var category: TypeEventCategory? = null
) : Serializable
{
    fun getDateText(): String
    {
        if (date_from != null && date_to != null)
        {
            return "${date_from!!.formatToString()} - ${date_to!!.formatToString()}"
        }
        else if (date_from != null)
        {
            return "c ${date_from!!.formatToString()}"
        }
        else if (date_to != null)
        {
            return "до ${date_to!!.formatToString()}"
        }
        else
        {
            return getStringMy(R.string.any_dates)
        }
    }
}