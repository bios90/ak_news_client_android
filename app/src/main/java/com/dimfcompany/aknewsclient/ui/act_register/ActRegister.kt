package com.dimfcompany.aknewsclient.ui.act_register

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.extensions.connectBoth
import com.dimfcompany.aknewsclient.base.extensions.disposeBy
import com.dimfcompany.aknewsclient.base.extensions.getColorMy
import com.dimfcompany.aknewsclient.databinding.ActRegisterBinding
import com.rucode.autopass.logic.utils.images.GlideManager

class ActRegister : BaseActivity()
{
    lateinit var vm_act_register: VmActRegister
    lateinit var bnd_act_register: ActRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_register = DataBindingUtil.setContentView(this, R.layout.act_register)
        vm_act_register = getVmOfType(VmActRegister::class.java)
        setBaseVmActions(vm_act_register)

        setEvents()
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
        connectBoth(bnd_act_register.etFirstName.getBsText(), vm_act_register.bs_first_name, composite_disposable)
        connectBoth(bnd_act_register.etLastName.getBsText(), vm_act_register.bs_last_name, composite_disposable)
        connectBoth(bnd_act_register.etEmail.getBsText(), vm_act_register.bs_email, composite_disposable)
        connectBoth(bnd_act_register.etPassword1.getBsText(), vm_act_register.bs_password_1, composite_disposable)
        connectBoth(bnd_act_register.etPassword2.getBsText(), vm_act_register.bs_password_2, composite_disposable)

        bnd_act_register.tvPrivacy.setOnClickListener(
            {
                vm_act_register.ViewListener().clickedPrivacyPolicy()
            })

        bnd_act_register.cvAvatar.imgImg.setOnClickListener(
            {
                vm_act_register.ViewListener().clickedAvatar()
            })

        bnd_act_register.tvRegister.setOnClickListener(
            {
                vm_act_register.ViewListener().clickedRegister()
            })
    }

    private fun setEvents()
    {
        vm_act_register.bs_avatar
                .subscribe(
                    {
                        val url = it.value?.image_url ?: return@subscribe
                        GlideManager.loadImage(bnd_act_register.cvAvatar.imgImg, url)
                    })
                .disposeBy(composite_disposable)
    }
}