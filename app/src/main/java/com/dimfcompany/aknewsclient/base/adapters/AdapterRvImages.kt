package com.dimfcompany.aknewsclient.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.ObjWithImageUrl
import com.dimfcompany.aknewsclient.databinding.ItemSquareImgSimpleBinding
import com.rucode.autopass.logic.utils.images.GlideManager

class AdapterRvImages : RecyclerView.Adapter<VhSquareImage>()
{
    var items: ArrayList<out ObjWithImageUrl> = arrayListOf()
        set(value)
        {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhSquareImage
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_square_img: ItemSquareImgSimpleBinding = DataBindingUtil.inflate(inflater, R.layout.item_square_img_simple, parent, false)
        return VhSquareImage(bnd_square_img)
    }

    override fun onBindViewHolder(holder: VhSquareImage, position: Int)
    {
        val obj = items.get(position)
        holder.bindImg(obj)
    }

    override fun getItemCount(): Int
    {
        return items.size
    }
}

class VhSquareImage(val bnd_square_img: ItemSquareImgSimpleBinding) : RecyclerView.ViewHolder(bnd_square_img.root)
{
    fun bindImg(obj: ObjWithImageUrl)
    {
        GlideManager.loadImage(bnd_square_img.img, obj.image_url)
    }
}