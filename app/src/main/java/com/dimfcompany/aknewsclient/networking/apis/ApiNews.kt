package com.dimfcompany.aknewsclient.networking.apis

import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.logic.models.FilterDataEvent
import com.dimfcompany.aknewsclient.logic.models.FilterDataNews
import com.dimfcompany.aknewsclient.logic.utils.DateManager
import com.dimfcompany.aknewsclient.logic.utils.formatToString
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNews
{
    @GET(Constants.Urls.GET_EVENTS)
    suspend fun getEvents(
            @Query("category") category: String? = null,
            @Query("date_from") date_from: String? = null,
            @Query("date_to") date_to: String? = null,
            @Query("search") search: String? = null,
    ): Response<ResponseBody>

    @GET(Constants.Urls.GET_NEWS)
    suspend fun getNews(
            @Query("search") search: String?,
            @Query("event_id") event_id: Long?,
            @Query("categs") categs: String?,
            @Query("sort") sort: String?,
    ): Response<ResponseBody>

    @GET(Constants.Urls.GET_EVENT_BY_ID)
    suspend fun getEventById(@Query("event_id") news_id: Long): Response<ResponseBody>
}

suspend fun ApiNews.getEventsByFilter(filter_data: FilterDataEvent): Response<ResponseBody>
{
    return this.getEvents(
        filter_data.category?.getNameForServer(),
        filter_data.date_from?.formatToString(DateManager.FORMAT_FOR_SERVER_LARAVEL),
        filter_data.date_to?.formatToString(DateManager.FORMAT_FOR_SERVER_LARAVEL),
        filter_data.search_text)
}

suspend fun ApiNews.getNewsByFilter(filter_data: FilterDataNews): Response<ResponseBody>
{
    val categ_str = filter_data.categories?.map({ it.getNameForServer() })?.joinToString("-")
    return this.getNews(
        filter_data.search_text,
        filter_data.event_id,
        categ_str,
        filter_data.sort?.getNameForServer())
}
