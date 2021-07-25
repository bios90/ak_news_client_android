package com.dimfcompany.aknewsclient.logic.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class NumberFormatter
{
    companion object
    {
        val SYMBOLES_DOTTED = DecimalFormatSymbols().apply(
            {
                this.decimalSeparator = '.'
            })
        val FORMAT_MONEY = DecimalFormat("#,###,##0.##")
        val FORMAT_RATING = DecimalFormat("0.0", SYMBOLES_DOTTED)
        val FORMAT_FILE_SIZE = DecimalFormat("0.##", SYMBOLES_DOTTED)
    }
}

fun Number.formatAsRating(): String
{
    return this.formatWith(NumberFormatter.FORMAT_RATING)
}

fun Number.formatAsMoney(): String
{
    return this.formatWith(NumberFormatter.FORMAT_MONEY)
}

fun Number.formatAsFileSize(): String
{
    return this.formatWith(NumberFormatter.FORMAT_FILE_SIZE)
}

fun Number.formatWith(format: DecimalFormat): String
{
    return format.format(this)
}