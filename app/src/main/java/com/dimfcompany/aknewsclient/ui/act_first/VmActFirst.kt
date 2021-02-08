package com.dimfcompany.aknewsclient.ui.act_first

import android.util.Log
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.asOptional
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.dimfcompany.aknewsclient.base.extensions.hideKeyboard
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.logic.ValidationManager
import com.dimfcompany.aknewsclient.logic.utils.BtnAction
import com.dimfcompany.aknewsclient.logic.utils.BtnActionWithText
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderAlerter
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.aknewsclient.ui.act_main.ActMain
import com.dimfcompany.aknewsclient.ui.act_register.ActRegister
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


class VmActFirst : BaseVm()
{
    val bs_email: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_password: BehaviorSubject<Optional<String>> = BehaviorSubject.create()

    private fun checkLogin()
    {
        val is_logged = SharedPrefsManager.pref_current_user.get().value != null
        if (is_logged)
        {
            BuilderIntent()
                    .setActivityToStart(ActMain::class.java)
                    .addOnStartAction(
                        {
                            ps_to_finish.onNext(Optional(null))
                        })
                    .sendInVm(this@VmActFirst)
        }
    }

    inner class ViewListener
    {
        fun clickedLogin()
        {
            hideKeyboard()

            val email = bs_email.value?.value
            val password = bs_password.value?.value
            val fb_token = SharedPrefsManager.pref_fb_token.get().value

            val validation_data = ValidationManager.validateLogin(email, password)
            if (!validation_data.is_valid)
            {
                validation_data.sendErrorInVm(this@VmActFirst)
                return
            }

            base_networker.makeLogin(email!!, password!!, fb_token,
                {

                    SharedPrefsManager.pref_current_user.asConsumer().accept(it.asOptional())
                    checkLogin()
                })
        }

        fun clickedForgotPass()
        {
            BuilderDialogMy()
                    .setTitle(getStringMy(R.string.password_recover))
                    .setText(getStringMy(R.string.for_password_recover))
                    .setViewId(R.layout.la_dialog_with_et)
                    .setBtnOkWithText(BtnActionWithText(getStringMy(R.string.recover),
                        {
                            if (!ValidationManager.isEmail(it))
                            {
                                val builder = BuilderAlerter.getRedBuilder(getStringMy(R.string.enter_valid_email))
                                ps_to_show_alerter.onNext(builder)
                                return@BtnActionWithText
                            }

                            base_networker.makePassReset(it!!,
                                {
                                    val builder = BuilderAlerter.getGreenBuilder("На почту $it отправлено письмо с инструкцией по восстановлению")
                                    ps_to_show_alerter.onNext(builder)
                                })
                        }))
                    .setBtnCancel(BtnAction.getDefaultCancel())
                    .sendInVm(this@VmActFirst)
        }


        fun clickedRegister()
        {
            BuilderIntent()
                    .setActivityToStart(ActRegister::class.java)
                    .setOkAction(
                        {
                            val email = it?.getStringExtra(Constants.Extras.EMAIL)
                            if (email != null)
                            {
                                bs_email.onNext(email.asOptional())
                            }

                            if (it?.getBooleanExtra(Constants.Extras.REGISTER_MADE, false) == true)
                            {
                                val builder_aleter = BuilderAlerter.getGreenBuilder("Поздравляем вы зарегистрированы в приложении Хронос. Вы сможете войти после ободрения от администратора")
                                ps_to_show_alerter.onNext(builder_aleter)
                            }
                        })
                    .sendInVm(this@VmActFirst)
        }
    }
}