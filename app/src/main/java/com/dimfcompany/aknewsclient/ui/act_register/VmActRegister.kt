package com.dimfcompany.aknewsclient.ui.act_register

import android.content.Intent
import android.util.Log
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.logic.ValidationManager
import com.dimfcompany.aknewsclient.logic.utils.BtnAction
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.aknewsclient.logic.utils.files.MyFileItem
import io.reactivex.subjects.BehaviorSubject

class VmActRegister : BaseVm()
{
    val bs_first_name: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_last_name: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_email: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_password_1: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_password_2: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_avatar: BehaviorSubject<Optional<ObjWithImageUrl>> = BehaviorSubject.create()

    inner class ViewListener
    {
        fun clickedPrivacyPolicy()
        {
            BuilderDialogMy()
                    .setViewId(R.layout.la_dialog_scrollable_tv)
                    .setTitle(getStringMy(R.string.privacy_policy))
                    .setText(getStringMy(R.string.privacy_policy_text))
                    .setBtnOk(BtnAction(getStringMy(R.string.its_clear), {}))
                    .sendInVm(this@VmActRegister)
        }

        fun clickedAvatar()
        {
            checkAndPickImage(
                {
                    bs_avatar.onNext(it.asOptional())
                })
        }

        fun clickedRegister()
        {
            hideKeyboard()

            val first_name = bs_first_name.value?.value
            val last_name = bs_last_name.value?.value
            val email = bs_email.value?.value
            val password_1 = bs_password_1.value?.value
            val password_2 = bs_password_2.value?.value
            val avatar = bs_avatar.value?.value as? MyFileItem

            val validation_data = ValidationManager.validateRegister(first_name, last_name, email, password_1, password_2)
            if (!validation_data.is_valid)
            {
                validation_data.sendErrorInVm(this@VmActRegister)
                return
            }

            base_networker.makeRegister(first_name!!, last_name!!, email!!, password_1!!, avatar,
                {
                    val intent = Intent()
                    intent.myPutExtra(Constants.Extras.REGISTER_MADE, true)
                    intent.myPutExtra(Constants.Extras.EMAIL, email)
                    ps_to_finish.onNext(intent.asOptional())
                })
        }
    }
}