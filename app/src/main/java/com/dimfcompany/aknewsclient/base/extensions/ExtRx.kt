package com.dimfcompany.aknewsclient.base.extensions

import com.dimfcompany.aknewsclient.base.MyUnknownError
import com.dimfcompany.aknewsclient.base.ParsingError
import com.dimfcompany.aknewsclient.logic.models.responses.BaseResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.ResponseBody
import retrofit2.Response

fun Disposable.disposeBy(cd: CompositeDisposable)
{
    cd.add(this)
}

fun Completable.mainThreaded(): Completable
{
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.mainThreaded(): Observable<T>
{
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.mainThreaded(): Single<T>
{
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}


fun BehaviorSubject<Optional<String>>.acceptIfNotMatches(opt_str: Optional<String>)
{
    val current_br_text = this.value?.value
    val accepted_text = opt_str.value

    if (current_br_text == null && accepted_text == null)
    {
        return
    }

    if (accepted_text.equals(current_br_text))
    {
        return
    }

    this.onNext(opt_str)
}

fun <T> Response<ResponseBody>.toObjOrThrow(clazz: Class<T>): T
{
    val response_as_str = this.getBodyAsStr()

    if (response_as_str == null)
    {
        throw MyUnknownError()
    }

    val base_response = response_as_str.toObjOrNullGson(BaseResponse::class.java)

    if (base_response == null)
    {
        throw ParsingError()
    }

    if (base_response.isFailed())
    {
        val error = base_response.getError()
        if (error != null)
        {
            throw error
        }
    }

    val obj = response_as_str.toObjOrNullGson(clazz)

    if (obj == null)
    {
        throw ParsingError()
    }

    return obj
}

fun <T> T.addParseCheckerForObj(action: (T) -> Boolean): T
{
    if (!action(this))
    {
        throw ParsingError()
    }

    return this
}

fun <T> connectBoth(first: BehaviorSubject<T>, second: BehaviorSubject<T>, cd: CompositeDisposable?)
{
    val disposable_1 = first.distinctUntilChanged()
            .subscribe(
                {
                    second.onNext(it)
                })

    val disposable_2 = second.distinctUntilChanged()
            .subscribe(
                {
                    first.onNext(it)
                })

    cd?.let(
        {
            disposable_1.disposeBy(cd)
            disposable_2.disposeBy(cd)
        })
}


