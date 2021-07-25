package com.dimfcompany.aknewsclient.ui.act_filter

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.enums.TypeEventCategory
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.databinding.ActFilterBinding
import com.dimfcompany.aknewsclient.logic.utils.formatToString
import java.util.*

class ActFilter : BaseActivity()
{
    lateinit var vm_filter: VmFilter
    lateinit var bnd_act_filter: ActFilterBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_filter = DataBindingUtil.setContentView(this, R.layout.act_filter)
        vm_filter = getVmOfType(VmFilter::class.java)
        setEvents()

        setBaseVmActions(vm_filter)
        setListeners()
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        is_light_nav_bar = true
        is_full_screen = true
        is_below_nav_bar = true
    }

    private fun setEvents()
    {
        vm_filter.bs_date_from
                .mainThreaded()
                .subscribe(
                    {
                        setDateToTv(it.value, bnd_act_filter.tvDateFrom)
                    })
                .disposeBy(composite_disposable)

        vm_filter.bs_date_to
                .mainThreaded()
                .subscribe(
                    {
                        setDateToTv(it.value, bnd_act_filter.tvDateTo)
                    })
                .disposeBy(composite_disposable)

        vm_filter
                .bs_category
                .mainThreaded()
                .subscribe(
                    {
                        val pos = when (it.value)
                        {
                            TypeEventCategory.AK -> 0
                            TypeEventCategory.CSL -> 2
                            else -> 1
                        }

                        bnd_act_filter.rgEventCategory.setCheckedAtPosition(pos, true)

                    })
                .disposeBy(composite_disposable)
    }

    private fun setDateToTv(date: Date?, tv: TextView)
    {
        val text = date?.formatToString() ?: "-"
        tv.text = text
    }

    private fun setListeners()
    {
        connectBoth(bnd_act_filter.etSearch.getBsText(), vm_filter.bs_search_text, composite_disposable)

        bnd_act_filter.tvApply.setOnClickListener(
            {
                vm_filter.ViewListener().clickedSave()
            })

        bnd_act_filter.tvDateFrom.setOnClickListener(
            {
                vm_filter.ViewListener().clickedDateFrom()
            })

        bnd_act_filter.tvDateTo.setOnClickListener(
            {
                vm_filter.ViewListener().clickedDateTo()
            })

        bnd_act_filter.tvClearDates.setOnClickListener(
            {
                vm_filter.ViewListener().clickedClearDates()
            })

        bnd_act_filter.rgEventCategory.setOnCheckedChangeListener(
            { rg, checked_id ->

                val pos = rg.getCheckedPosition()

                when (pos)
                {
                    0 -> vm_filter.ViewListener().clickedCategory(TypeEventCategory.AK)
                    2 -> vm_filter.ViewListener().clickedCategory(TypeEventCategory.CSL)
                    else -> vm_filter.ViewListener().clickedCategory(null)
                }
            })
    }
}