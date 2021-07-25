package com.dimfcompany.aknewsclient.ui.act_event_show

import android.util.Log
import androidx.lifecycle.viewModelScope

import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.ObjWithFile
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.base.adapters.AdapterRvNewsToWatch
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.logic.ValidationManager
import com.dimfcompany.aknewsclient.logic.models.*
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.aknewsclient.logic.utils.pdf.PdfCreator
import com.dimfcompany.aknewsclient.ui.act_filter_news.ActFilterNews
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class VmActEventShow : BaseVm()
{
    val bs_event: BehaviorSubject<ModelEvent> = BehaviorSubject.create()
    val bs_news: BehaviorSubject<ArrayList<ModelNews>> = BehaviorSubject.create()
    val bs_filter_data: BehaviorSubject<FilterDataNews> = BehaviorSubject.createDefault(FilterDataNews())

    init
    {
        setEvents()
    }

    override fun viewAttached()
    {
        loadEvent()
        loadNews()
    }

    private fun setEvents()
    {
        bs_event
                .subscribe(
                    {
                        ps_to_toggle_overlay.onNext(false)
                    })
                .disposeBy(composite_disposable)

        bs_filter_data
                .skip(1)
                .subscribe(
                    {
                        loadNews()
                    })
                .disposeBy(composite_disposable)
    }

    private fun getEventIdExtra(): Long
    {
        val event_id = intent_extra.getLongExtraMy(Constants.Extras.EVENT_ID)
        return event_id!!
    }

    private fun loadEvent()
    {
        base_networker.getEventById(getEventIdExtra(),
            {
                bs_event.onNext(it)
            })
    }

    private fun loadNews()
    {
        val filter_data = bs_filter_data.value ?: FilterDataNews()
        filter_data.event_id = getEventIdExtra()

        base_networker.getNews(filter_data,
            {
                bs_news.onNext(it)
            })
    }

    inner class ViewListener : AdapterRvNewsToWatch.Listener
    {
        fun clickedGeneratePdf()
        {
            val news = bs_news.value ?: return
            val event = bs_event.value ?: return

            ps_show_hide_progress.onNext(true)
            viewModelScope.launch(block =
            {
                val creater = PdfCreator()
                val file = creater.generateDocument(news, event)
                openAnyFile(file.getUriForShare())
                ps_show_hide_progress.onNext(false)
            })
        }

        fun clickedFilter()
        {
            val current_filter = bs_filter_data.value ?: FilterDataEvent()
            BuilderIntent()
                    .setActivityToStart(ActFilterNews::class.java)
                    .addParam(Constants.Extras.FILTER_DATA, current_filter)
                    .setOkAction(
                        {
                            val filter_data = it?.getSerializableExtra(Constants.Extras.FILTER_DATA) as? FilterDataNews
                            filter_data ?: return@setOkAction

                            bs_filter_data.onNext(filter_data)
                        })
                    .sendInVm(this@VmActEventShow)
        }

        override fun clickedCard(news: ModelNews)
        {

        }

        override fun cardClicked(item: ObjWithFile)
        {
            val file = item as? ModelFile ?: return
            base_vm_helper.checkAndOpenFile(file)
        }

        override fun clickedImage(img: ObjWithImageUrl, news: ModelNews)
        {
            val images = news.images ?: return
            var pos = news.images?.indexOf(img)
            pos = if (pos != 0 && pos!! >= 0) pos else 0

            ps_to_show_images_slider.onNext(Pair(images, pos))
        }

        override fun clickedEmail(news: ModelNews)
        {
            if (!ValidationManager.isEmail(news.author_email))
            {
                Log.e("ViewListener", "clickedEmail: Error not an email")
                return
            }

            val user_name = SharedPrefsManager.pref_current_user.get().value?.getFullName()
            val subj = "Вопрос из приложения AkNews"
            val text = "Пользователь: $user_name\nНовость: ${news.name}"

            emailIntent(news.author_email, text, subj)
        }
    }
}