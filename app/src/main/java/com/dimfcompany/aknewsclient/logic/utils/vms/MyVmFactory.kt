package com.dimfcompany.aknewsclient.logic.utils.vms

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dimfcompany.aknewsclient.base.BaseActivity
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MyVmFactory @Inject constructor(private val activity: BaseActivity)
{
    fun <T : ViewModel> createFactory(): ViewModelProvider.Factory
    {
        val factory = object : ViewModelProvider.Factory
        {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T
            {
                return modelClass.newInstance()
            }
        }

        return factory
    }

    fun <T : ViewModel> getViewModel(vm_class: Class<T>): T
    {
        val factory = createFactory<T>()
        return ViewModelProvider(activity, factory).get(vm_class)
    }
}