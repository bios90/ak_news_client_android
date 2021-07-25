package com.dimfcompany.aknewsclient.ui.act_filter_news

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.enums.TypeNewsCategory
import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.addOrRemove
import com.dimfcompany.aknewsclient.base.extensions.asOptional
import com.dimfcompany.aknewsclient.base.extensions.runActionWithDelay
import com.dimfcompany.aknewsclient.logic.models.FilterDataNews
import com.dimfcompany.aknewsclient.logic.models.TypeSort
import io.reactivex.subjects.BehaviorSubject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class VmFilterNews : BaseVm()
{
    val bs_search_text: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_selected_categs: BehaviorSubject<HashSet<TypeNewsCategory>> = BehaviorSubject.createDefault(HashSet())
    val bs_sort: BehaviorSubject<Optional<TypeSort>> = BehaviorSubject.createDefault(Optional(TypeSort.BY_CATEGORY))

    override fun viewAttached()
    {
        val filter_data = intent_extra.getSerializableExtra(Constants.Extras.FILTER_DATA) as? FilterDataNews
        if (filter_data != null)
        {
            runActionWithDelay(viewModelScope, 100,
                {
                    bs_search_text.onNext(filter_data.search_text.asOptional())
                    bs_selected_categs.onNext(filter_data.categories ?: hashSetOf())
                    bs_sort.onNext(filter_data.sort.asOptional())
                })
        }
    }

    inner class ViewListener()
    {
        fun clickedSort(sort: TypeSort?)
        {
            bs_sort.onNext(sort.asOptional())
        }

        fun clickedSave()
        {
            val filter_data = FilterDataNews()
            filter_data.search_text = bs_search_text.value?.value
            filter_data.categories = bs_selected_categs.value
            filter_data.sort = bs_sort.value?.value ?: TypeSort.BY_CATEGORY

            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.FILTER_DATA, filter_data)
            ps_to_finish.onNext(return_intent.asOptional())
        }

        fun clickedCategory(category: TypeNewsCategory?)
        {
            val set = bs_selected_categs.value ?: HashSet()

            if (category == null)
            {
                set.clear()
            }
            else
            {
                set.addOrRemove(category)
            }

            if (set.containsAll(TypeNewsCategory.values().toCollection(ArrayList())))
            {
                set.clear()
            }

            bs_selected_categs.onNext(set)
        }
    }
}