package com.dimfcompany.aknewsclient.di.application

import com.dimfcompany.aknewsclient.base.AppClass
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDownloader
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules =
[
    AndroidInjectionModule::class,
    ModuleActivityBuilders::class,
    ModuleVmsBuilder::class,
    ModuleNetworking::class
])
interface ComponentApp : AndroidInjector<AppClass>
{
    fun inject(builder_downloader: BuilderDownloader)
    fun getComponentVm(module_vm:ModuleVmBase):ComponentVm
}