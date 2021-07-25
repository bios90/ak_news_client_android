package com.dimfcompany.aknewsclient.ui.act_main

import android.content.Intent
import android.util.Log
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.BusMainEvents
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.base.adapters.AdapterRvEvents
import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.asOptional
import com.dimfcompany.aknewsclient.base.extensions.disposeBy
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.logic.models.FilterDataEvent
import com.dimfcompany.aknewsclient.logic.models.ModelEvent
import com.dimfcompany.aknewsclient.logic.models.ModelUser
import com.dimfcompany.aknewsclient.logic.utils.BtnAction
import com.dimfcompany.aknewsclient.logic.utils.MyPush
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.aknewsclient.ui.act_event_show.ActEventShow
import com.dimfcompany.aknewsclient.ui.act_filter.ActFilter
import com.dimfcompany.aknewsclient.ui.act_first.ActFirst
import io.reactivex.subjects.BehaviorSubject

class VmActMain : BaseVm()
{
    val bs_events: BehaviorSubject<FeedDisplayInfo<ModelEvent>> = BehaviorSubject.create()
    val bs_user: BehaviorSubject<ModelUser> = BehaviorSubject.create()
    val bs_filter_data: BehaviorSubject<FilterDataEvent> = BehaviorSubject.createDefault(FilterDataEvent())

    init
    {
        setEvents()
    }

    override fun viewAttached()
    {
        super.viewAttached()
        checkStartFromPush()
    }

    private fun checkStartFromPush()
    {
        val my_push = intent_extra.getSerializableExtra(Constants.Extras.MY_PUSH) as? MyPush ?: return

        if (my_push.event_id != null)
        {
            toEventShow(my_push.event_id)
        }
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

        bs_filter_data
                .subscribe(
                    {
                        reloadEvents()
                    })
                .disposeBy(composite_disposable)
    }

    private fun reloadEvents()
    {
        val fitler_data = bs_filter_data.value ?: FilterDataEvent()
        base_networker.getEvents(fitler_data,
            {
                val feed_info = FeedDisplayInfo(it, LoadBehavior.UPDATE)
                bs_events.onNext(feed_info)
            })
    }

    inner class ViewListener : AdapterRvEvents.Listener
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
            val current_filter = bs_filter_data.value ?: FilterDataEvent()
            BuilderIntent()
                    .setActivityToStart(ActFilter::class.java)
                    .addParam(Constants.Extras.FILTER_DATA, current_filter)
                    .setOkAction(
                        {
                            val filter_data = it?.getSerializableExtra(Constants.Extras.FILTER_DATA) as? FilterDataEvent
                            filter_data ?: return@setOkAction

                            bs_filter_data.onNext(filter_data)
                        })
                    .sendInVm(this@VmActMain)
        }

        override fun clickedEvent(event: ModelEvent)
        {
            val event_id = event.id ?: return

            toEventShow(event_id)
        }
    }
}