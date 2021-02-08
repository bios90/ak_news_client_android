package com.justordercompany.barista.logic.utils

import android.Manifest
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.extensions.disposeBy
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import javax.inject.Inject

class PermissionBlockedFinally : RuntimeException("**** Permission blocked finally ****")
class PermissionBlockedNow : RuntimeException("**** Permission blocked now ****")

class PermissionManager @Inject constructor(private val activity: BaseActivity)
{
    companion object
    {
        val permissions_location = listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissions_camera = listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissions_files = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    val rx_permissions: RxPermissions

    init
    {
        rx_permissions = RxPermissions(activity)
    }

    fun checkAndRequest(permissions: List<String>): Completable
    {
        return rx_permissions.requestEachCombined(*permissions.toTypedArray())
                .filter(
                    {
                        if (it.granted)
                        {
                            return@filter true
                        }

                        if (it.shouldShowRequestPermissionRationale)
                        {
                            throw PermissionBlockedNow()
                        }

                        throw PermissionBlockedFinally()
                    })
                .ignoreElements()
    }
}

class BuilderPermRequest()
{
    private var permissions: List<String>? = null
    private var action_available: (() -> Unit)? = null
    private var action_blocked_now: (() -> Unit)? = null
    private var action_blocked_finally: (() -> Unit)? = null

    fun setPermissions(permissions: List<String>) = apply(
        {
            this.permissions = permissions
        })

    fun setActionAvailable(action: () -> Unit) = apply(
        {
            this.action_available = action
        })

    fun setActionBlockedNow(action: () -> Unit) = apply(
        {
            this.action_blocked_now = action
        })

    fun setActionBlockedFinally(action: () -> Unit) = apply(
        {
            this.action_blocked_finally = action
        })

    fun build(base_activity: BaseActivity)
    {
        if (permissions == null)
        {
            throw RuntimeException("**** Error no permissions passed to check ****")
        }

        base_activity.permission_manager
                .checkAndRequest(permissions!!)
                .subscribe(
                    {
                        action_available?.invoke()
                    },
                    {
                        if (it is PermissionBlockedNow)
                        {
                            action_blocked_now?.invoke()
                        }
                        else if (it is PermissionBlockedFinally)
                        {
                            action_blocked_finally?.invoke()
                        }
                    })
                .disposeBy(base_activity.composite_disposable)
    }

    fun sendInVm(vm: BaseVm)
    {
        vm.ps_request_permissions.onNext(this)
    }
}