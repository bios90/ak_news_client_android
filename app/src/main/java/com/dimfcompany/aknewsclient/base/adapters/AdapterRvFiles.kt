package com.dimfcompany.aknewsclient.base.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.ObjWithFile
import com.dimfcompany.aknewsclient.databinding.ItemFileBinding
import com.dimfcompany.aknewsclient.logic.utils.formatAsFileSize

class AdapterRvFiles : RecyclerView.Adapter<VhFiles>()
{
    var items: ArrayList<out ObjWithFile> = arrayListOf()
        set(value)
        {
            field = value
            notifyDataSetChanged()
        }
    var listener: BaseRvListener<ObjWithFile>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhFiles
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd_item_file: ItemFileBinding = DataBindingUtil.inflate(inflater, R.layout.item_file, parent, false)
        return VhFiles(bnd_item_file)
    }

    override fun onBindViewHolder(holder: VhFiles, position: Int)
    {
        val item = items.get(position)
        holder.bindObjWithFile(item)
        holder.bnd_item_file.larCard.setOnClickListener(
            {
                listener?.cardClicked(item)
            })
    }

    override fun getItemCount(): Int
    {
        return items.size
    }
}

class VhFiles(val bnd_item_file: ItemFileBinding) : RecyclerView.ViewHolder(bnd_item_file.root)
{
    init
    {
        bnd_item_file.tvRemove.visibility = View.GONE
    }

    fun bindObjWithFile(obj: ObjWithFile)
    {
        bnd_item_file.tvFileName.text = obj.getObjFileName()
        bnd_item_file.tvFileSize.text = "${obj.getObjFileSize().formatAsFileSize()} мб"
    }
}