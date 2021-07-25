package com.dimfcompany.aknewsclient.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.aknewsclient.base.ObjectWithId
import com.dimfcompany.aknewsclient.base.diff.BaseDiff
import java.lang.RuntimeException

abstract class BaseRvAdapter<M : ObjectWithId, VB : ViewDataBinding> : RecyclerView.Adapter<BaseViewHolder<M, VB>>()
{
    var items: ArrayList<M> = arrayListOf()
    var action_on_create: ((VB) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<M, VB>
    {
        val inflater = LayoutInflater.from(parent.context)
        val bnd: VB = DataBindingUtil.inflate(inflater, layout_id, parent, false)
        action_on_create?.invoke(bnd)
        return BaseViewHolder(bnd)
    }

    override fun getItemCount(): Int
    {
        return items.size
    }

    fun setItems(rec_info: FeedDisplayInfo<M>)
    {
        if (rec_info.load_behavior == LoadBehavior.FULL_RELOAD)
        {
            this.items = ArrayList(rec_info.items)
            notifyDataSetChanged()
            return
        }
//        else if (rec_info.load_behavior == LoadBehavior.UPDATE_IDS)
//        {
//            this.items = ArrayList(rec_info.items)
//            val poses = this.items.getPosOfObjectByIdMulti(rec_info.load_behavior.ids_to_update)
//            poses.forEach(
//                {
//                    notifyItemChanged(it)
//                })
//        }
        else
        {
            if (diff_class == null)
            {
                throw RuntimeException("**** Error if not full update need DiffUtil ****")
            }
            val diff_callback = diff_class!!.getDeclaredConstructor(List::class.java, List::class.java).newInstance(items, rec_info.items)
            val diff_result = DiffUtil.calculateDiff(diff_callback)
            diff_result.dispatchUpdatesTo(this)
            this.items = ArrayList(rec_info.items)
        }
    }

    abstract val layout_id: Int
    abstract val diff_class: (Class<out BaseDiff<M>>)?
}

interface BaseRvListener<T>
{
    fun cardClicked(item: T)
}

open class BaseViewHolder<M : ObjectWithId, VB : ViewDataBinding>(val bnd: VB) : RecyclerView.ViewHolder(bnd.root)
