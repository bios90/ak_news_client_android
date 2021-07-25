package com.dimfcompany.aknewsclient.base

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.dimfcompany.aknewsclient.di.application.ModuleVmBase
import com.dimfcompany.aknewsclient.logic.utils.BaseVmHelper
import com.dimfcompany.aknewsclient.logic.utils.BtnAction
import com.dimfcompany.aknewsclient.logic.utils.MessagesManager
import com.dimfcompany.aknewsclient.logic.utils.builders.*
import com.dimfcompany.aknewsclient.logic.utils.files.MyFileItem
import com.dimfcompany.aknewsclient.logic.utils.images.ImageCameraManager
import com.dimfcompany.aknewsclient.networking.BaseNetworker
import com.dimfcompany.aknewsclient.networking.apis.ApiAuth
import com.dimfcompany.aknewsclient.networking.apis.ApiGlobal
import com.dimfcompany.aknewsclient.networking.apis.ApiNews
import com.dimfcompany.aknewsclient.ui.act_event_show.ActEventShow
import com.justordercompany.barista.logic.utils.BuilderPermRequest
import com.justordercompany.barista.logic.utils.PermissionManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

open class BaseVm : ViewModel()
{
    @Inject
    lateinit var api_auth: ApiAuth

    @Inject
    lateinit var api_global: ApiGlobal

    @Inject
    lateinit var api_news: ApiNews

    @Inject
    lateinit var base_networker: BaseNetworker

    @Inject
    lateinit var base_vm_helper: BaseVmHelper

    var composite_disposable = CompositeDisposable()

    lateinit var intent_extra: Intent
    val ps_intent_builded: PublishSubject<BuilderIntent> = PublishSubject.create()
    val ps_to_show_dialog: PublishSubject<BuilderDialogMy> = PublishSubject.create()
    val ps_request_permissions: PublishSubject<BuilderPermRequest> = PublishSubject.create()
    val ps_to_show_bottom_dialog: PublishSubject<BuilderDialogBottom> = PublishSubject.create()
    val ps_pick_action: PublishSubject<ImageCameraManager.TypePick> = PublishSubject.create()
    val ps_show_hide_progress: PublishSubject<Boolean> = PublishSubject.create()
    val ps_disable_screen_touches: PublishSubject<Boolean> = PublishSubject.create()
    val ps_to_show_alerter: PublishSubject<BuilderAlerter> = PublishSubject.create()
    val ps_to_finish: PublishSubject<Optional<Intent>> = PublishSubject.create()
    val ps_to_show_images_slider: PublishSubject<Pair<ArrayList<out ObjWithImageUrl>, Int?>> = PublishSubject.create()
    val ps_to_show_date_dialog: PublishSubject<BuilderDateDialog> = PublishSubject.create()
    val ps_to_toggle_overlay: PublishSubject<Boolean> = PublishSubject.create()

    init
    {
        AppClass.app_component
                .getComponentVm(ModuleVmBase(this))
                .inject(this)
    }

    open fun viewAttached()
    {

    }

    fun checkPermissions(permissions: List<String>, action_success: () -> Unit)
    {
        val text_blocked = getStringMy(R.string.for_full_work_need_permissions)

        BuilderPermRequest()
                .setPermissions(permissions)
                .setActionBlockedNow(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedNow(text_blocked,
                            {
                                checkPermissions(permissions, action_success)
                            })
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionBlockedFinally(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedFinally(text_blocked)
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionAvailable(
                    {
                        action_success()
                    })
                .sendInVm(this)
    }

    fun checkAndPickImage(action_picked: (MyFileItem) -> Unit)
    {
        checkPermissions(PermissionManager.permissions_camera,
            {
                BuilderDialogBottom()
                        .addBtn(BtnAction(getStringMy(R.string.gallery),
                            {
                                val pick = ImageCameraManager.TypePick.GALLERY_IMAGE
                                pick.action_success = action_picked
                                ps_pick_action.onNext(pick)
                            }, getStringMy(R.string.faw_images)))
                        .addBtn(BtnAction(getStringMy(R.string.camera),
                            {
                                val pick = ImageCameraManager.TypePick.CAMERA_IMAGE
                                pick.action_success = action_picked
                                ps_pick_action.onNext(pick)
                            }, getStringMy(R.string.faw_camera)))
                        .sendInVm(this)
            })
    }

    fun showPrivacyPolicy()
    {
        BuilderDialogMy()
                .setViewId(R.layout.la_dialog_scrollable_tv)
                .setTitle(getStringMy(R.string.privacy_policy))
                .setText(getStringMy(R.string.privacy_policy_text))
                .setBtnOk(BtnAction(getStringMy(R.string.its_clear), {}))
                .sendInVm(this)
    }

    fun toEventShow(event_id:Long)
    {
        BuilderIntent()
                .setActivityToStart(ActEventShow::class.java)
                .addParam(Constants.Extras.EVENT_ID, event_id)
                .sendInVm(this)
    }
}