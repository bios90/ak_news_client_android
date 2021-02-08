package com.dimfcompany.aknewsclient.base.extensions

import android.animation.Animator
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

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
