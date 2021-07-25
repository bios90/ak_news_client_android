package com.dimfcompany.aknewsclient.base.diff

import com.dimfcompany.aknewsclient.logic.models.ModelEvent
import com.dimfcompany.aknewsclient.logic.utils.areDatesEqualForDiff

class DiffEvents(items_new: List<ModelEvent>, items_old: List<ModelEvent>) : BaseDiff<ModelEvent>(items_new, items_old)
{
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        if (!areDatesEqualForDiff(item_new.updated, item_old.updated))
        {
            return false
        }

        if (item_new.category != item_old.category)
        {
            return false
        }

        if (item_new.news?.size != item_old.news?.size)
        {
            return false
        }

        return true
    }
}