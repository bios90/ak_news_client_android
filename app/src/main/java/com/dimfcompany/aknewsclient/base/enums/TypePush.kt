package com.dimfcompany.aknewsclient.base.enums

enum class TypePush
{
    BAN,
    NEWS_ADDED;

    companion object
    {
        fun initFromSting(str: String): TypePush?
        {
            try
            {
                val type = TypePush.valueOf(str)
                return type
            }
            catch (e: IllegalArgumentException)
            {
                try
                {
                    val type = TypePush.valueOf(str.toLowerCase())
                    return type
                }
                catch (e: IllegalArgumentException)
                {
                    try
                    {
                        val type = TypePush.valueOf(str.toUpperCase())
                        return type
                    }
                    catch (e: IllegalArgumentException)
                    {

                    }
                }
            }

            return null
        }
    }
}