package com.dimfcompany.aknewsclient.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.logic.utils.MessagesManager
import com.dimfcompany.aknewsclient.logic.utils.images.ImageCameraManager
import com.dimfcompany.aknewsclient.logic.utils.vms.MyVmFactory
import com.justordercompany.barista.logic.utils.PermissionManager
import com.justordercompany.barista.ui.dialogs.DialogBottomSheetRounded
import com.rucode.autopass.logic.utils.images.GlideManager
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity()
{
    @Inject
    lateinit var vm_factory: MyVmFactory

    @Inject
    lateinit var messages_manager: MessagesManager

    @Inject
    lateinit var permission_manager: PermissionManager

    @Inject
    lateinit var image_camera_manager: ImageCameraManager

    var color_status_bar: Int = getColorMy(R.color.red)
    var is_light_status_bar: Boolean = false
    var color_nav_bar: Int = getColorMy(R.color.red)
    var is_light_nav_bar: Boolean = false
    var is_full_screen: Boolean = false
    var is_below_nav_bar: Boolean = false

    val composite_disposable = CompositeDisposable()

    private var base_vms: ArrayList<BaseVm> = arrayListOf()

    fun <T : ViewModel> getVmOfType(vm_class: Class<T>): T
    {
        return vm_factory.getViewModel(vm_class)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        applyStatusNavColors()
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        applyStatusNavColors()
        AppClass.top_activity = this
    }

    override fun onPause()
    {
        clearTopActivity()
        super.onPause()
    }

    override fun onDestroy()
    {
        base_vms.clear()
        composite_disposable.dispose()
        clearTopActivity()
        super.onDestroy()
    }

    fun setBaseVmActions(base_vm: BaseVm)
    {
        if (base_vms.contains(base_vm))
        {
            throw RuntimeException("**** Error base vm actions already setted")
        }

        base_vms.add(base_vm)

        base_vm.ps_intent_builded
                .mainThreaded()
                .subscribe(
                    {
                        it.startActivity(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_alerter
                .mainThreaded()
                .subscribe(
                    {
                        it.show(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_dialog
                .mainThreaded()
                .subscribe(
                    {
                        it.build(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_request_permissions
                .mainThreaded()
                .subscribe(
                    {
                        it.build(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_bottom_dialog
                .mainThreaded()
                .subscribe(
                    {
                        val dialog = DialogBottomSheetRounded(it)
                        dialog.show(this.supportFragmentManager, null)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_pick_action
                .mainThreaded()
                .subscribe(
                    {
                        when (it)
                        {
                            ImageCameraManager.TypePick.CAMERA_IMAGE ->
                            {
                                image_camera_manager.pickCameraImage(it.action_success)
                            }

                            ImageCameraManager.TypePick.GALLERY_IMAGE ->
                            {
                                image_camera_manager.pickGalleryImage(it.action_success)
                            }

                            ImageCameraManager.TypePick.PDF ->
                            {
                                image_camera_manager.pickPdfFile(it.action_success, it.action_error)
                            }

                            ImageCameraManager.TypePick.ANY ->
                            {
                                image_camera_manager.pickAnyFile(it.action_success, it.action_error)
                            }
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_show_hide_progress
                .mainThreaded()
                .subscribe(
                    {
                        if (it)
                        {
                            messages_manager.showProgressDialog()
                        }
                        else
                        {
                            messages_manager.dismissProgressDialog()
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_disable_screen_touches
                .mainThreaded()
                .subscribe(
                    {
                        if (it)
                        {
                            messages_manager.disableScreenTouches()
                        }
                        else
                        {
                            messages_manager.enableScreenTouches(false)
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_finish
                .mainThreaded()
                .subscribe(
                    {
                        if (it.value != null)
                        {
                            setResult(Activity.RESULT_OK, it.value)
                        }
                        finish()
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_images_slider
                .mainThreaded()
                .subscribe(
                    {
                        showImagesCarousel(it)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_date_dialog
                .mainThreaded()
                .subscribe(
                    {
                        it.show(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_toggle_overlay
                .mainThreaded()
                .subscribe(
                    {
                        if (this is ViewWithOverlay)
                        {
                            if (it)
                            {
                                this.showOverlay()
                            }
                            else
                            {
                                this.hideOverlay()
                            }
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.intent_extra = intent

        base_vm.viewAttached()
    }

    private fun clearTopActivity()
    {
        val current_top = AppClass.top_activity
        if (this.equals(current_top))
        {
            AppClass.top_activity = null
        }
    }

    fun applyStatusNavColors()
    {
        if (is_full_screen)
        {
            this.window?.makeFullScreen(is_below_nav_bar)
        }

        this.window?.setStatusBarColorMy(color_status_bar)
        this.window?.setNavBarColor(color_nav_bar)
        this.window?.setStatusLightDark(is_light_status_bar)
        this.window?.setNavBarLightDark(is_light_nav_bar)
    }

    private fun showImagesCarousel(data: Pair<ArrayList<out ObjWithImageUrl>, Int?>)
    {
        val image_strs = data.first.map({ it.image_url }).filterNotNull()

        val pos = data.second ?: 0
        StfalconImageViewer.Builder(this, image_strs,
            { view, url ->
                GlideManager.loadImage(view, url)
            })
                .withHiddenStatusBar(false)
                .allowSwipeToDismiss(true)
                .withStartPosition(pos)
                .show()
    }
}