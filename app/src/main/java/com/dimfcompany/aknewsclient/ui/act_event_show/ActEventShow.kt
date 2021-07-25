package com.dimfcompany.aknewsclient.ui.act_event_show

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.ViewWithOverlay
import com.dimfcompany.aknewsclient.base.adapters.AdapterRvNewsToWatch
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.databinding.ActEventShowBinding
import com.dimfcompany.aknewsclient.logic.utils.formatToString

class ActEventShow : BaseActivity(), ViewWithOverlay
{
    lateinit var vm_event_show: VmActEventShow
    lateinit var bnd_act_event_show: ActEventShowBinding
    var adapter_news = AdapterRvNewsToWatch()
    override var view_overlay: View? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_event_show = DataBindingUtil.setContentView(this, R.layout.act_event_show)
        view_overlay = bnd_act_event_show.viewOverlay
        vm_event_show = getVmOfType(VmActEventShow::class.java)

        setEvents()
        setBaseVmActions(vm_event_show)

        setRecylcer()
        setListeners()
    }

    fun setNavStatus()
    {
        color_status_bar = getColorMy(R.color.white)
        color_nav_bar = getColorMy(R.color.white)
        is_light_status_bar = true
        is_light_nav_bar = true
    }

    private fun setEvents()
    {
        vm_event_show.bs_event
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_event_show.tvDate.text = it.date?.formatToString()
                        bnd_act_event_show.tvType.text = it.category?.getNameForDisplay() ?: "-"

                        val color = it.category?.getColor() ?: getColorMy(R.color.red)
                        color_status_bar = color
                        bnd_act_event_show.larTop.setBackgroundColor(color)
                        applyStatusNavColors()
                    })
                .disposeBy(composite_disposable)

        vm_event_show.bs_news
                .mainThreaded()
                .subscribe(
                    {
                        adapter_news.setItems(FeedDisplayInfo(it, LoadBehavior.FULL_RELOAD))
                    })
                .disposeBy(composite_disposable)
    }

    private fun setListeners()
    {
        bnd_act_event_show.tvFilter.setOnClickListener(
            {
                vm_event_show.ViewListener().clickedFilter()
            })

        bnd_act_event_show.tvGeneratePdf.setOnClickListener(
            {
                vm_event_show.ViewListener().clickedGeneratePdf()
            })
    }

    private fun setRecylcer()
    {
        adapter_news.listener = vm_event_show.ViewListener()
        bnd_act_event_show.recNews.adapter = adapter_news
        bnd_act_event_show.recNews.layoutManager = LinearLayoutManager(this)
        bnd_act_event_show.recNews.setDivider(getColorMy(R.color.transparent), dp2pxInt(8f))
    }
}