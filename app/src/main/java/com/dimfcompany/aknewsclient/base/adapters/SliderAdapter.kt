package com.dimfcompany.aknewsclient.base.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.databinding.ItemSliderImageBinding
import com.rucode.autopass.logic.utils.images.GlideManager
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter : SliderViewAdapter<SliderItemVh>()
{
    private var items: ArrayList<out ObjWithImageUrl> = arrayListOf()
    var listener: ((ObjWithImageUrl) -> Unit)? = null

    override fun getCount(): Int
    {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderItemVh
    {
        val inflater = LayoutInflater.from(parent!!.context)
        val bnd_slider_item: ItemSliderImageBinding = DataBindingUtil.inflate(inflater, R.layout.item_slider_image, parent, false)
        return SliderItemVh(bnd_slider_item)
    }

    override fun onBindViewHolder(viewHolder: SliderItemVh?, position: Int)
    {
        val item = items.get(position)

        viewHolder?.bindObj(item)
        viewHolder?.bnd_slider_image?.root?.setOnClickListener(
            {
                listener?.invoke(item)
            })
    }

    fun setItems(items: ArrayList<out ObjWithImageUrl>)
    {
        this.items = items
        notifyDataSetChanged()
    }

}

class SliderItemVh(val bnd_slider_image: ItemSliderImageBinding) : SliderViewAdapter.ViewHolder(bnd_slider_image.root)
{
    fun bindObj(obj: ObjWithImageUrl)
    {
        val img = bnd_slider_image.root as ImageView
//        GlideManager.loadImage(img, "https://picsum.photos/${(100..500).random()}")
        GlideManager.loadImage(img, obj.image_url)

    }
}