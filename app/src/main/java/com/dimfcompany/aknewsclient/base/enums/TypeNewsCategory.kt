package com.dimfcompany.aknewsclient.base.enums

import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.extensions.getStringMy
import com.google.gson.annotations.SerializedName

enum class TypeNewsCategory
{
    @SerializedName("ops")
    OPS,

    @SerializedName("hr")
    HR,

    @SerializedName("qa")
    QA,

    @SerializedName("financial_economical")
    FINANCIAL_ECONOMICAL,

    @SerializedName("development")
    DEVELOPMENT,

    @SerializedName("marketing")
    MARKETING,

    @SerializedName("it")
    IT,

    @SerializedName("administration")
    ADMINISTRATION,

    @SerializedName("using")
    USING,

    @SerializedName("gen_director")
    GEN_DIRECTOR;

    companion object
    {
        fun initFromPosition(pos: Int): TypeNewsCategory
        {
            return TypeNewsCategory.values().get(pos)
        }
    }

    fun getNameForServer(): String
    {
        return this.name.toLowerCase()
    }

    fun getNameForDisplay(): String
    {
        return when (this)
        {
            OPS -> "OPS"
            HR -> "H.R."
            QA -> "Q.A."
            FINANCIAL_ECONOMICAL -> "Финансово-экономический отдел"
            DEVELOPMENT -> "Отдел Развития"
            MARKETING -> "Маркетинг"
            IT -> "I.T."
            ADMINISTRATION -> "Административный отдел"
            USING -> "Отдел Эксплуатации"
            GEN_DIRECTOR -> "Генеральный директор"
        }
    }

    fun getPos(): Int
    {
        return TypeNewsCategory.values().indexOf(this)
    }

    fun getFawIcon(): String
    {
        return when (this)
        {
            OPS -> getStringMy(R.string.faw_chart_pie)
            HR -> getStringMy(R.string.faw_search_plus)
            QA -> getStringMy(R.string.faw_bug)
            FINANCIAL_ECONOMICAL -> getStringMy(R.string.faw_wallet)
            DEVELOPMENT -> getStringMy(R.string.faw_arrow_circle_up)
            MARKETING -> getStringMy(R.string.faw_cart)
            IT -> getStringMy(R.string.faw_ethernet)
            ADMINISTRATION -> getStringMy(R.string.faw_user_tie)
            USING -> getStringMy(R.string.faw_mitten)
            GEN_DIRECTOR -> getStringMy(R.string.faw_user_spy)
        }
    }
}