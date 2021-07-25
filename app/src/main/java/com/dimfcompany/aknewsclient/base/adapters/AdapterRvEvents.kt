package com.dimfcompany.aknewsclient.base.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.diff.DiffEvents
import com.dimfcompany.aknewsclient.base.enums.TypeEventCategory
import com.dimfcompany.aknewsclient.base.enums.TypeNewsCategory
import com.dimfcompany.aknewsclient.base.extensions.*
import com.dimfcompany.aknewsclient.databinding.ItemEventBinding
import com.dimfcompany.aknewsclient.logic.models.ModelEvent
import com.dimfcompany.aknewsclient.logic.models.ModelNews
import com.dimfcompany.aknewsclient.logic.models.getNewsOfType
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderBg
import com.dimfcompany.aknewsclient.logic.utils.formatToString
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import java.lang.RuntimeException

class AdapterRvEvents : RecyclerView.Adapter<VhEvents>()
{
    interface Listener
    {
        fun clickedEvent(event: ModelEvent)
    }

    var items: ArrayList<ModelEvent> = arrayListOf()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhEvents
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_item_event: ItemEventBinding = DataBindingUtil.inflate(inflater, R.layout.item_event, parent, false)
        return VhEvents(bnd_item_event)
    }

    override fun onBindViewHolder(holder: VhEvents, position: Int)
    {
        val event = items.get(position)
        holder.bindEvent(event)

        holder.bnd_item_event.root.setOnClickListener(
            {
                listener?.clickedEvent(event)
            })
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    fun setItems(rec_info: FeedDisplayInfo<ModelEvent>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.items = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }
        else
        {
            val diff = DiffEvents(rec_info.items, items)
            val diff_result = DiffUtil.calculateDiff(diff)
            diff_result.dispatchUpdatesTo(this)
            this.items = ArrayList(rec_info.items)
        }
    }
}

class VhEvents(val bnd_item_event: ItemEventBinding) : RecyclerView.ViewHolder(bnd_item_event.root)
{
    val adapter = AdapterRvImages()

    init
    {
        bnd_item_event.rvCarousel.adapter = adapter
        bnd_item_event.rvCarousel.layoutManager = LinearLayoutManager(bnd_item_event.root.context, RecyclerView.HORIZONTAL, false)
        bnd_item_event.rvCarousel.setDivider(getColorMy(R.color.transparent), dp2pxInt(6f), DividerItemDecoration.HORIZONTAL)

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(bnd_item_event.rvCarousel)
        snapHelper.snapToPadding = true

        bnd_item_event.rvCarousel.applyAutoScroll(4000, 3)
    }

    fun bindEvent(event: ModelEvent)
    {
        val news = event.news ?: throw RuntimeException("**** Error event without news ****")

        bnd_item_event.tvDate.text = event.date?.formatToString()
        bnd_item_event.tvNewsCount.text = "Новостей: ${news.size}"

        bnd_item_event.tvType.text = event.category?.getNameForDisplay()
        bnd_item_event.tvType.background = event.category?.getBubbleBg()

        bindImages(news)
        bindCategories(news)
    }

    fun bindCategories(news: ArrayList<ModelNews>)
    {
        TypeNewsCategory.values().forEach(
            { category ->

                val la = bnd_item_event.laForCategs.get(category.getPos()) as ViewGroup
                val tv_icon = la.getChildAt(0) as TextView
                val tv_category = la.getChildAt(1) as TextView
                val tv_titles = la.getChildAt(2) as TextView

                val news_of_type = news.getNewsOfType(category)
                if (news_of_type.size > 0)
                {
                    la.visibility = View.VISIBLE
                    tv_icon.text = category.getFawIcon()
                    tv_category.text = category.getNameForDisplay()


                    val titles = news_of_type.mapIndexed(
                        { index, news ->

                            "${index+1}. ${news.name}"
                        })
                            .joinToString("\n")
                    tv_titles.text = titles
                }
                else
                {
                    la.visibility = View.GONE
                }
            })
    }

    private fun bindImages(news: ArrayList<ModelNews>)
    {
        bnd_item_event.rvCarousel.visibility = View.GONE
        return
        val items = news.map({ it.images })
                .filterNotNull()
                .flatten()
                .toCollection(ArrayList())

        adapter.items = items

        bnd_item_event.rvCarousel.visibility = (items.size > 0).toVisibility()
    }
}