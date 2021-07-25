package com.dimfcompany.aknewsclient.logic.models

import com.dimfcompany.aknewsclient.base.enums.TypeNewsCategory
import java.io.Serializable
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashSet

class FilterDataNews(
        var event_id: Long? = null,
        var search_text: String? = null,
        var sort: TypeSort = TypeSort.BY_CATEGORY,
        var categories: HashSet<TypeNewsCategory>? = null
) : Serializable

enum class TypeSort
{
    BY_NAME,
    BY_CATEGORY,
    BY_AUTHOR;

    companion object
    {
        fun initFromPos(pos: Int): TypeSort
        {
            return when (pos)
            {
                0 -> TypeSort.BY_NAME
                1 -> TypeSort.BY_CATEGORY
                2 -> TypeSort.BY_AUTHOR
                else -> throw RuntimeException("*** Error unknown pos ***")
            }
        }
    }

    fun getNameForServer(): String
    {
        return when (this)
        {
            BY_NAME -> "name"
            BY_CATEGORY -> "category"
            BY_AUTHOR -> "author_name"
        }
    }
}