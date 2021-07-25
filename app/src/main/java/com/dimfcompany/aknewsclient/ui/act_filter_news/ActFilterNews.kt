package com.dimfcompany.aknewsclient.ui.act_filter_news

import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.enums.TypeNewsCategory
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.databinding.ActFilterNewsBinding
import com.dimfcompany.aknewsclient.databinding.LaChebWithIconBinding
import com.dimfcompany.aknewsclient.logic.models.TypeSort

class ActFilterNews : BaseActivity()
{
    lateinit var vm_filter: VmFilterNews
    lateinit var bnd_act_filter: ActFilterNewsBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_filter = DataBindingUtil.setContentView(this, R.layout.act_filter_news)
        vm_filter = getVmOfType(VmFilterNews::class.java)
        setCheckBoxes()
        setEvents()

        setBaseVmActions(vm_filter)
        setListeners()
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_status_bar = false
        is_full_screen = true
        is_below_nav_bar = true
        is_light_nav_bar = true
    }

    private fun setCheckBoxes()
    {
        val categs_with_null = TypeNewsCategory
                .values()
                .toCollection(ArrayList<TypeNewsCategory?>())
        categs_with_null.add(0, null)

        for (categ in categs_with_null)
        {
            val bnd_lar_cheb: LaChebWithIconBinding = DataBindingUtil.inflate(layoutInflater, R.layout.la_cheb_with_icon, bnd_act_filter.lalForCheckboxes, false)
            bnd_lar_cheb.tvIcon.text = categ?.getFawIcon()
            bnd_lar_cheb.cheb.text = categ?.getNameForDisplay() ?: getStringMy(R.string.all)
            bnd_lar_cheb.tvIcon.setOnClickListener(
                {
                    bnd_lar_cheb.cheb.performClick()
                })

            bnd_lar_cheb.cheb.setOnClickListener(
                {
                    vm_filter.ViewListener().clickedCategory(categ)
                })

            bnd_act_filter.lalForCheckboxes.addView(bnd_lar_cheb.root)
        }
    }

    fun setEvents()
    {
        vm_filter.bs_sort
                .mainThreaded()
                .subscribe(
                    {
                        val pos = when (it.value)
                        {
                            TypeSort.BY_NAME -> 0
                            TypeSort.BY_CATEGORY -> 1
                            TypeSort.BY_AUTHOR -> 2
                            else -> return@subscribe
                        }

                         bnd_act_filter.rgSort.setCheckedAtPosition(pos, true)
                    })
                .disposeBy(composite_disposable)

        vm_filter.bs_selected_categs
                .mainThreaded()
                .subscribe(
                    { checked_categs ->

                        val cheb_all = (bnd_act_filter.lalForCheckboxes.get(0) as ViewGroup).get(1) as CheckBox
                        if (checked_categs.isEmpty())
                        {
                            cheb_all.setValueWithoutClick(true)
                        }
                        else
                        {
                            cheb_all.setValueWithoutClick(false)
                        }

                        TypeNewsCategory.values().forEach(
                            {

                                val cheb = (bnd_act_filter.lalForCheckboxes.get(it.getPos() + 1) as ViewGroup).get(1) as CheckBox
                                if (checked_categs.contains(it))
                                {
                                    cheb.setValueWithoutClick(true)
                                }
                                else
                                {
                                    cheb.setValueWithoutClick(false)
                                }
                            })
                    })
                .disposeBy(composite_disposable)
    }

    fun setListeners()
    {
        connectBoth(bnd_act_filter.etSearch.getBsText(), vm_filter.bs_search_text, composite_disposable)

        bnd_act_filter.rgSort.setOnCheckedChangeListener(
            { rg, checked_id ->

                val pos = rg.getCheckedPosition() ?: 0
                val sort = TypeSort.initFromPos(pos)
                vm_filter.ViewListener().clickedSort(sort)
            })

        bnd_act_filter.tvApply.setOnClickListener(
            {
                vm_filter.ViewListener().clickedSave()
            })
    }
}