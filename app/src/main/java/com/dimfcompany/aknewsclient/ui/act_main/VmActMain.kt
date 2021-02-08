package com.dimfcompany.aknewsclient.ui.act_main

import android.content.Intent
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.BusMainEvents
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.asOptional
import com.dimfcompany.aknewsclient.base.extensions.disposeBy
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.logic.models.ModelUser
import com.dimfcompany.aknewsclient.logic.utils.BtnAction
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.aknewsclient.ui.act_first.ActFirst
import io.reactivex.subjects.BehaviorSubject

class VmActMain : BaseVm()
{
    val bs_user: BehaviorSubject<ModelUser> = BehaviorSubject.create()

    init
    {
        setEvents()
    }

    private fun setEvents()
    {
        SharedPrefsManager.pref_current_user.asObservable()
                .subscribe(
                    {
                        if (it.value == null)
                        {
                            BuilderIntent()
                                    .setActivityToStart(ActFirst::class.java)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .sendInVm(this)
                        }
                        else
                        {
                            bs_user.onNext(it.value)
                        }
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener
    {
        fun clickedPrivacyPolicy()
        {
            showPrivacyPolicy()
        }

        fun clickedAvatar()
        {
            checkAndPickImage(
                {
                    val user_id = SharedPrefsManager.pref_current_user.get().value?.id ?: return@checkAndPickImage
                    base_networker.updateUserInfo(user_id, null, null, null, it,
                        {
                            SharedPrefsManager.pref_current_user.asConsumer().accept(it.asOptional())
                        })
                })
        }

        fun clickedExit()
        {
            BuilderDialogMy()
                    .setViewId(R.layout.la_dialog_simple)
                    .setTitle(getStringMy(R.string.exit))
                    .setText(getStringMy(R.string.exit_current_account))
                    .setBtnOk(BtnAction(getStringMy(R.string.exit),
                        {
                            BusMainEvents.ps_to_logout.onNext(Any())
                        }))
                    .setBtnCancel(BtnAction.getDefaultCancel())
                    .sendInVm(this@VmActMain)
        }

        fun clickedFilter()
        {

        }
    }
}