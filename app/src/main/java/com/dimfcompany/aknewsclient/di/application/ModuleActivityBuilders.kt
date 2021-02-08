package com.dimfcompany.aknewsclient.di.application

import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.ui.ActSplash
import com.dimfcompany.aknewsclient.ui.act_first.ActFirst
import com.dimfcompany.aknewsclient.ui.act_main.ActMain
import com.dimfcompany.aknewsclient.ui.act_register.ActRegister
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ModuleActivityBuilders
{
    @ContributesAndroidInjector(modules = [ModuleActFirst::class])
    abstract fun contributeActFirst(): ActFirst

    @ContributesAndroidInjector(modules = [ModuleActRegister::class])
    abstract fun contributeActRegister(): ActRegister

    @ContributesAndroidInjector(modules = [ModuleActMain::class])
    abstract fun contributeActMain(): ActMain

    @ContributesAndroidInjector(modules = [ModuleActSplash::class])
    abstract fun contributeActSplash(): ActSplash
}

@Module
abstract class ModuleActFirst
{
    @Binds
    abstract fun bindBaseActivity(act: ActFirst): BaseActivity
}

@Module
abstract class ModuleActRegister
{
    @Binds
    abstract fun bindBaseActivity(act: ActRegister): BaseActivity
}

@Module
abstract class ModuleActMain
{
    @Binds
    abstract fun bindBaseActivity(act: ActMain): BaseActivity
}

@Module
abstract class ModuleActSplash
{
    @Binds
    abstract fun bindBaseActivity(act: ActSplash): BaseActivity
}