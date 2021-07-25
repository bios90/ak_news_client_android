package com.dimfcompany.aknewsclient.di.application

import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.ui.ActSplash
import com.dimfcompany.aknewsclient.ui.act_event_show.ActEventShow
import com.dimfcompany.aknewsclient.ui.act_filter.ActFilter
import com.dimfcompany.aknewsclient.ui.act_filter_news.ActFilterNews
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

    @ContributesAndroidInjector(modules = [ModuleActFilter::class])
    abstract fun contributeActFilter(): ActFilter

    @ContributesAndroidInjector(modules = [ModuleActEventShow::class])
    abstract fun contributeActEventShow(): ActEventShow

    @ContributesAndroidInjector(modules = [ModuleActFilterNews::class])
    abstract fun contributeActFilterNews(): ActFilterNews
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

@Module
abstract class ModuleActFilter
{
    @Binds
    abstract fun bindBaseActivity(act: ActFilter): BaseActivity
}

@Module
abstract class ModuleActEventShow
{
    @Binds
    abstract fun bindBaseActivity(act: ActEventShow): BaseActivity
}

@Module
abstract class ModuleActFilterNews
{
    @Binds
    abstract fun bindBaseActivity(act: ActFilterNews): BaseActivity
}