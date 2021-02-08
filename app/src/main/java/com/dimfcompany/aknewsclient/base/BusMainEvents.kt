package com.dimfcompany.aknewsclient.base

import com.dimfcompany.aknewsclient.base.extensions.Optional
import com.dimfcompany.aknewsclient.base.extensions.disposeBy
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

object BusMainEvents
{
    val composite_disposable = CompositeDisposable()
    val ps_to_logout: PublishSubject<Any> = PublishSubject.create()

    init
    {
        ps_to_logout.subscribe(
            {
                SharedPrefsManager.pref_current_user.asConsumer().accept(Optional(null))
            })
                .disposeBy(composite_disposable)
    }
}