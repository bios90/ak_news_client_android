package com.dimfcompany.aknewsclient.base.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.ObjWithFile
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.base.diff.DiffNews
import com.dimfcompany.aknewsclient.base.extensions.dp2pxInt
import com.dimfcompany.aknewsclient.base.extensions.getColorMy
import com.dimfcompany.aknewsclient.base.extensions.setDivider
import com.dimfcompany.aknewsclient.base.extensions.toVisibility
import com.dimfcompany.aknewsclient.databinding.ItemNewsToWatchBinding
import com.dimfcompany.aknewsclient.logic.models.ModelNews

class AdapterRvNewsToWatch : RecyclerView.Adapter<VhNewsToWatch>()
{
    interface Listener : BaseRvListener<ObjWithFile>
    {
        fun clickedCard(news: ModelNews)
        fun clickedImage(img: ObjWithImageUrl, news: ModelNews)
        fun clickedEmail(news: ModelNews)
    }

    private var items: ArrayList<ModelNews> = arrayListOf()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhNewsToWatch
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_slider_item: ItemNewsToWatchBinding = DataBindingUtil.inflate(inflater, R.layout.item_news_to_watch, parent, false)
        val vh = VhNewsToWatch(bnd_slider_item)
        return vh
    }

    override fun onBindViewHolder(holder: VhNewsToWatch, position: Int)
    {
        val item = items.get(position)
        val previous_category = items.getOrNull(position - 1)?.category
        val show_category = item.category != previous_category
        holder.bindNews(item,show_category)

        holder.adapter_files.listener = listener
        holder.adapter_slider.listener = (
                {
                    listener?.clickedImage(it, item)
                })

        holder.bnd_item_news.root.setOnClickListener(
            {
                listener?.clickedCard(item)
            })

        holder.bnd_item_news.tvAuthorEmail
                .setOnClickListener(
                    {
                        listener?.clickedEmail(item)
                    })
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    fun setItems(rec_info: FeedDisplayInfo<ModelNews>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.items = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }
        else
        {
            val diff = DiffNews(rec_info.items, items)
            val diff_result = DiffUtil.calculateDiff(diff)
            diff_result.dispatchUpdatesTo(this)
            this.items = ArrayList(rec_info.items)
        }
    }
}

class VhNewsToWatch(val bnd_item_news: ItemNewsToWatchBinding) : RecyclerView.ViewHolder(bnd_item_news.root)
{
    val adapter_files = AdapterRvFiles()
    val adapter_slider = SliderAdapter()

    init
    {
        bnd_item_news.recFiles.adapter = adapter_files
        bnd_item_news.recFiles.layoutManager = LinearLayoutManager(bnd_item_news.root.context)
        bnd_item_news.recFiles.setDivider(getColorMy(R.color.transparent), dp2pxInt(4f))

        bnd_item_news.slider.setSliderAdapter(adapter_slider)
    }


    fun bindNews(news: ModelNews, show_category: Boolean)
    {
        val images = news.images ?: arrayListOf()
        adapter_slider.setItems(images)

        if (images.size > 0)
        {
            bnd_item_news.slider.visibility = View.VISIBLE
            bnd_item_news.dividerTop.visibility = View.GONE
        }
        else
        {
            bnd_item_news.slider.visibility = View.GONE
            bnd_item_news.dividerTop.visibility = View.VISIBLE
        }

        bnd_item_news.tvCategoryName.text = news.category?.getNameForDisplay()
        bnd_item_news.tvCategoryIcon.text = news.category?.getFawIcon()
        bnd_item_news.larCategory.visibility = show_category.toVisibility()

        bnd_item_news.tvName.text = news.name
        bnd_item_news.tvAuthorName.text = news.author_name
        bnd_item_news.tvAuthorEmail.text = news.author_email
        bnd_item_news.tvText.text = news.text

        val files = news.files ?: arrayListOf()
        adapter_files.items = files
        bnd_item_news.tvFilesTitle.visibility = (files.size > 0).toVisibility()
    }
}