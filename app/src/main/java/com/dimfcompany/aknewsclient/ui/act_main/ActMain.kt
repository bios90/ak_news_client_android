package com.dimfcompany.aknewsclient.ui.act_main

import android.os.Bundle
import android.view.Gravity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.adapters.AdapterRvEvents
import com.dimfcompany.aknewsclient.base.data_binding.makeRoundMy
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.databinding.ActMainBinding
import com.dimfcompany.aknewsclient.databinding.ActRegisterBinding
import com.dimfcompany.aknewsclient.ui.act_register.VmActRegister
import com.rucode.autopass.logic.utils.images.GlideManager

class ActMain : BaseActivity()
{
    lateinit var vm_act_main: VmActMain
    lateinit var bnd_act_main: ActMainBinding
    val adapter = AdapterRvEvents()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_main = DataBindingUtil.setContentView(this, R.layout.act_main)
        vm_act_main = getVmOfType(VmActMain::class.java)
        setBaseVmActions(vm_act_main)

        setEvents()
        setListeners()
        setRecycler()

        runActionWithDelay(lifecycleScope,100,
            {
                bnd_act_main.laFakeStatus.setHeight(getStatusBarHeight())
            })
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        is_light_nav_bar = false
    }

    private fun setListeners()
    {
        bnd_act_main.tvProfile.setOnClickListener(
            {
                bnd_act_main.laDrawer.openDrawer(GravityCompat.START)
            })

        bnd_act_main.tvFilter.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedFilter()
            })

        bnd_act_main.tvPrivacy.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedPrivacyPolicy()
            })

        bnd_act_main.cvAvatar.imgImg.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedAvatar()
            })

        bnd_act_main.tvLogout.setOnClickListener(
            {
                vm_act_main.ViewListener().clickedExit()
            })
    }

    private fun setEvents()
    {
        vm_act_main.bs_user
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_main.tvName.text = it.getFullName()
                        val url = it.url_avatar ?: return@subscribe
                        GlideManager.loadImage(bnd_act_main.cvAvatar.imgImg, url)
                    })
                .disposeBy(composite_disposable)

        vm_act_main.bs_filter_data
                .mainThreaded()
                .subscribe(
                    {
                        bnd_act_main.tvType.text = it.category?.getNameForDisplay() ?: getStringMy(R.string.all_news)
                        bnd_act_main.tvDate.text = it.getDateText()
                    })
                .disposeBy(composite_disposable)

        vm_act_main.bs_events
                .mainThreaded()
                .subscribe(
                    {
                        adapter.setItems(it)
                    })
                .disposeBy(composite_disposable)
    }

    override fun onBackPressed()
    {
        if(bnd_act_main.laDrawer.isDrawerOpen(GravityCompat.START))
        {
            bnd_act_main.laDrawer.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    private fun setRecycler()
    {
        adapter.listener = vm_act_main.ViewListener()
        bnd_act_main.recEvents.adapter = adapter
        bnd_act_main.recEvents.layoutManager = LinearLayoutManager(this)
        bnd_act_main.recEvents.setDivider(getColorMy(R.color.transparent), dp2pxInt(8f))
    }

}