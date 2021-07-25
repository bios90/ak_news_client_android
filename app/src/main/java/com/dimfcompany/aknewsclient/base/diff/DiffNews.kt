package com.dimfcompany.aknewsclient.base.diff

import com.dimfcompany.aknewsclient.logic.models.ModelNews
import com.dimfcompany.aknewsclient.logic.utils.areDatesEqualForDiff

class DiffNews(items_new: List<ModelNews>, items_old: List<ModelNews>) : BaseDiff<ModelNews>(items_new, items_old)
{
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        if (!areDatesEqualForDiff(item_new.updated, item_old.updated))
        {
            return false
        }

        if (item_new.name?.equals(item_old.name) == false)
        {
            return false
        }

        if (item_new.category != item_old.category)
        {
            return false
        }

        if (item_new.text?.equals(item_old.text) == false)
        {
            return false
        }

        if (item_new.author_name?.equals(item_old.author_name) == false)
        {
            return false
        }

        if (item_new.author_email?.equals(item_old.author_email) == false)
        {
            return false
        }

        if (item_new.images?.size != item_old.images?.size)
        {
            return false
        }

        if (item_new.files?.size != item_old.files?.size)
        {
            return false
        }

        return true
    }
}