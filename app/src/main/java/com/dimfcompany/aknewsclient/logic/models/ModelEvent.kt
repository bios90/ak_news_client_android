package com.dimfcompany.aknewsclient.logic.models

import com.dimfcompany.aknewsclient.base.ObjectWithDates
import com.dimfcompany.aknewsclient.base.ObjectWithId
import com.dimfcompany.aknewsclient.base.enums.TypeEventCategory
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class ModelEvent(
        override var id: Long? = null,
        var date: Date? = null,
        var category: TypeEventCategory?,
        var news:ArrayList<ModelNews>? = arrayListOf(),
        override var created: Date?,
        override var updated: Date?,
        override var deleted: Date?
) : Serializable, ObjectWithId, ObjectWithDates