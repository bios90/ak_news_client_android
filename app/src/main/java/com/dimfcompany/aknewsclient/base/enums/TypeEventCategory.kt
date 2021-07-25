package com.dimfcompany.aknewsclient.base.enums

import android.graphics.drawable.Drawable
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.extensions.getColorMy
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderBg
import com.google.gson.annotations.SerializedName

enum class TypeEventCategory
{
    @SerializedName("ak")
    AK,

    @SerializedName("csl")
    CSL;

    companion object
    {
        fun initFromPosition(pos: Int): TypeEventCategory
        {
            return TypeEventCategory.values().get(pos)
        }
    }

    fun getPos(): Int
    {
        return TypeEventCategory.values().indexOf(this)
    }

    fun getNameForServer(): String
    {
        return when (this)
        {
            AK -> "ak"
            CSL -> "csl"
        }
    }

    fun getNameForDisplay(): String
    {
        return when (this)
        {
            AK -> getStringMy(R.string.news_ak)
            CSL -> getStringMy(R.string.news_csl)
        }
    }

    fun getColor(): Int
    {
        return when (this)
        {
            AK -> getColorMy(R.color.red_ak)
            CSL -> getColorMy(R.color.blue_yum)
        }
    }

    fun getBubbleBg(): Drawable
    {
        val color = getColor()
        return BuilderBg.getSimpleDrawable(999f, color)
    }
}