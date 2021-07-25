package com.dimfcompany.aknewsclient.ui.act_filter

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.enums.TypeEventCategory
import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.asOptional
import com.dimfcompany.aknewsclient.base.extensions.runActionWithDelay
import com.dimfcompany.aknewsclient.logic.models.FilterDataEvent
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDateDialog
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderIntent
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class VmFilter : BaseVm()
{
    val bs_date_from: BehaviorSubject<Optional<Date>> = BehaviorSubject.createDefault(Optional(null))
    val bs_date_to: BehaviorSubject<Optional<Date>> = BehaviorSubject.createDefault(Optional(null))
    val bs_search_text: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_category: BehaviorSubject<Optional<TypeEventCategory>> = BehaviorSubject.create()



    override fun viewAttached()
    {
        val filter_data = intent_extra.getSerializableExtra(Constants.Extras.FILTER_DATA) as? FilterDataEvent
        if (filter_data != null)
        {
            runActionWithDelay(viewModelScope, 100,
                {
                    bs_date_from.onNext(filter_data.date_from.asOptional())
                    bs_date_to.onNext(filter_data.date_to.asOptional())
                    bs_category.onNext(filter_data.category.asOptional())
                    bs_search_text.onNext(filter_data.search_text.asOptional())
                })
        }
    }


    inner class ViewListener
    {


        fun clickedCategory(category: TypeEventCategory?)
        {
            bs_category.onNext(category.asOptional())
        }

        fun clickedDateFrom()
        {
            clickedDate(bs_date_from)
        }

        fun clickedDateTo()
        {
            clickedDate(bs_date_to)
        }

        fun clickedClearDates()
        {
            bs_date_from.onNext(Optional(null))
            bs_date_to.onNext(Optional(null))
        }

        fun clickedDate(bs: BehaviorSubject<Optional<Date>>)
        {
            BuilderDateDialog()
                    .setDateMax(Date())
                    .setDateCurrent(bs.value?.value ?: Date())
                    .setActionSuccess(
                        {
                            bs.onNext(it.asOptional())
                        })
                    .sendInVm(this@VmFilter)
        }

        fun clickedSave()
        {
            val filter_data = FilterDataEvent()
            filter_data.date_from = bs_date_from.value?.value
            filter_data.date_to = bs_date_to.value?.value
            filter_data.search_text = bs_search_text.value?.value
            filter_data.category = bs_category.value?.value

            val return_intent = Intent()
            return_intent.putExtra(Constants.Extras.FILTER_DATA, filter_data)
            ps_to_finish.onNext(return_intent.asOptional())
        }
    }
}