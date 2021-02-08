package com.dimfcompany.aknewsclient.ui.act_first

import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.extensions.connectBoth
import com.dimfcompany.aknewsclient.base.extensions.getColorMy
import com.dimfcompany.aknewsclient.databinding.ActFirstBinding
import dagger.android.DaggerActivity
import javax.inject.Inject

class ActFirst : BaseActivity()
{
    lateinit var vm_act_first: VmActFirst
    lateinit var bnd_act_first: ActFirstBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_first = DataBindingUtil.setContentView(this, R.layout.act_first)
        vm_act_first = getVmOfType(VmActFirst::class.java)
        setBaseVmActions(vm_act_first)

        setListeners()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.red)
        is_light_status_bar = true
        is_light_nav_bar = true
    }

    private fun setListeners()
    {
        connectBoth(bnd_act_first.etEmail.getBsText(), vm_act_first.bs_email, composite_disposable)
        connectBoth(bnd_act_first.etPassword.getBsText(), vm_act_first.bs_password, composite_disposable)

        bnd_act_first.tvLogin.setOnClickListener(
            {
                vm_act_first.ViewListener().clickedLogin()
            })

        bnd_act_first.tvRegister.setOnClickListener(
            {
                vm_act_first.ViewListener().clickedRegister()
            })

        bnd_act_first.tvForgotPassword.setOnClickListener(
            {
                vm_act_first.ViewListener().clickedForgotPass()
            })
    }
}