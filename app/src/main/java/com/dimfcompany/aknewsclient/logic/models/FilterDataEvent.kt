package com.dimfcompany.aknewsclient.logic.models

import com.dimfcompany.aknewsclient.base.enums.TypeEventCategory
import java.io.Serializable
import java.util.*

class FilterData(
        var search_text: String? = null,
        var date_from: Date? = null,
        var date_to: Date? = null,
        var category: TypeEventCategory? = null
) : Serializable