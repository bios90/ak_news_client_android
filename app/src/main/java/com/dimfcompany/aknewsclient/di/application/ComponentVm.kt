package com.dimfcompany.aknewsclient.di.application

import com.dimfcompany.aknewsclient.base.BaseVm
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ModuleVmBase::class])
interface ComponentVm
{
    fun inject(base_vm: BaseVm)
}