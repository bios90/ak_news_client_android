package com.dimfcompany.aknewsclient.base.extensions

import android.animation.Animator
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.min

fun EditText.getNullableText(): String?
{
    val text = this.text.toString().trim()
    if (TextUtils.isEmpty(text))
    {
        return null
    }

    return text
}

fun EditText.acceptIfNotMatches(text: String?)
{
    val current_et_text = this.getNullableText()

    if (current_et_text == null && text == null)
    {
        return
    }

    if (current_et_text.equals(text))
    {
        return
    }

    if (this.text.toString().equals(text))
    {
        return
    }

    this.setText(text)
}

fun AlertDialog.makeTransparentBg()
{
    this.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
}

fun TextView.setTextHtml(text: String)
{
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
    {
        this.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
    }
    else
    {
        this.setText(Html.fromHtml(text))
    }
}

fun View.animateFadeOut(duration: Int = 300)
{
    this.animate().alpha(1f).setDuration(duration.toLong())
            .setListener(object : Animator.AnimatorListener
            {
                override fun onAnimationRepeat(animation: Animator?)
                {
                }

                override fun onAnimationEnd(animation: Animator?)
                {
                }

                override fun onAnimationCancel(animation: Animator?)
                {
                }

                override fun onAnimationStart(animation: Animator?)
                {
                    this@animateFadeOut.visibility = View.VISIBLE
                }
            })
            .setInterpolator(LinearInterpolator()).start()
}

fun View.animateFadeIn(duration: Int, visibility: Int = View.GONE)
{
    this.animate().alpha(0f).setDuration(duration.toLong())
            .setListener(object : Animator.AnimatorListener
            {
                override fun onAnimationRepeat(animation: Animator?)
                {
                }

                override fun onAnimationEnd(animation: Animator?)
                {
                    this@animateFadeIn.visibility = visibility
                }

                override fun onAnimationCancel(animation: Animator?)
                {
                }

                override fun onAnimationStart(animation: Animator?)
                {
                }
            })
            .setInterpolator(LinearInterpolator()).start()
}

fun Dialog.setNavigationBarColor(color: Int)
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val window = this.window ?: return
        val metrics = DisplayMetrics()
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics)

        val dimDrawable = GradientDrawable()

        val navigationBarDrawable = GradientDrawable()
        navigationBarDrawable.shape = GradientDrawable.RECTANGLE
        navigationBarDrawable.setColor(color)

        val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)

        val windowBackground = LayerDrawable(layers)

        windowBackground.setLayerInsetTop(1, metrics.heightPixels)

        window.setBackgroundDrawable(windowBackground)
    }
}

fun Boolean.toVisibility(): Int
{
    if (this)
    {
        return View.VISIBLE
    }

    return View.GONE
}

fun RadioGroup.setCheckedAtPosition(position: Int, only_if_not_same: Boolean = false)
{
    val rb = this.getChildAt(position) as RadioButton
    if (only_if_not_same)
    {
        if (this.getCheckedPosition() != position)
        {
            rb.isChecked = true
        }
    }
    else
    {
        rb.isChecked = true
    }
}

fun RadioGroup.getCheckedPosition(): Int?
{
    if (this.childCount == 0)
    {
        return null
    }

    for (a in 0 until this.childCount)
    {
        val rb = this.getChildAt(a) as RadioButton
        if (rb.isChecked)
        {
            return a
        }
    }

    return null
}

fun RecyclerView.setDivider(color: Int, size: Int, orientation: Int = DividerItemDecoration.VERTICAL)
{
    val drw = GradientDrawable()
    drw.shape = GradientDrawable.RECTANGLE

    val itemDecorator: DividerItemDecoration
    if (orientation == DividerItemDecoration.VERTICAL)
    {
        drw.setSize(0, size)
        itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    }
    else
    {
        drw.setSize(size, 0)
        itemDecorator = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
    }

    drw.setColor(color)

    itemDecorator.setDrawable(drw)
    this.addItemDecoration(itemDecorator)
}

fun RecyclerView.applyAutoScroll(time_delay: Long, count_to_scroll: Int)
{
    this.layoutManager as? LinearLayoutManager ?: return
    this.adapter ?: return

    var need_to_skip = false

    val timer = Timer()
    timer.schedule(object : TimerTask()
    {
        override fun run()
        {
            if (need_to_skip)
            {
                need_to_skip = false
                return
            }
            else if (this@applyAutoScroll.scrollState != RecyclerView.SCROLL_STATE_IDLE)
            {
                need_to_skip = true
            }
            else
            {
                val la_manager = this@applyAutoScroll.layoutManager as LinearLayoutManager

                val pos_to_scroll = run(
                    {
                        val current_pos = la_manager.findLastCompletelyVisibleItemPosition()
                        val max_pos = adapter!!.itemCount - 1

                        if (current_pos + count_to_scroll < max_pos)
                        {
                            return@run current_pos + count_to_scroll
                        }
                        else if (current_pos == max_pos)
                        {
                            return@run 0
                        }
                        else
                        {
                            return@run max_pos
                        }
                    })

                la_manager.smoothScrollToPosition(this@applyAutoScroll, RecyclerView.State(), pos_to_scroll)
                need_to_skip = false
            }
        }
    }, 0, time_delay)
}

fun CheckBox.setValueWithoutClick(is_checked: Boolean)
{
    this.isEnabled = false
    this.isChecked = is_checked
    this.isEnabled = true
}

fun Bitmap.scaleToMaxSize(max_size: Int): Bitmap
{
    val ratio = min((max_size.toFloat() / this.width.toFloat()), (max_size.toFloat() / this.height.toFloat()))
    val new_width = ratio * this.width
    val new_height = ratio * this.height

    val bitmap = Bitmap.createScaledBitmap(this, new_width.toInt(), new_height.toInt(), true)
    return bitmap
}
