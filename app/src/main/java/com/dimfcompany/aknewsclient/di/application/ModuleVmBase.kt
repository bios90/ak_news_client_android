package com.dimfcompany.aknewsclient.di.application

import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.logic.utils.BaseVmHelper
import com.dimfcompany.aknewsclient.networking.BaseNetworker
import dagger.Module
import dagger.Provides

@Module
class ModuleVmBase(private val base_vm: BaseVm)
{
    @Provides
    fun provideBaseNetworker(): BaseNetworker
    {
        return BaseNetworker(base_vm)
    }

    @Provides
    fun provideBaseVmHelper(): BaseVmHelper
    {
        return BaseVmHelper(base_vm)
    }
}