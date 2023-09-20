package com.geekstudio.composetest.data.remote

import com.geekstudio.composetest.data.api.RssApi
import com.geekstudio.composetest.data.dto.Rss
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNewsRss(private val rssService: RssApi) {

    /**
     * 언어 타입에 따라 RSS 들고 오기
     */
    fun execute(languageType: RssApi.LanguageType): Flow<Rss> {
        return flow {
            val item = when (languageType) {
                RssApi.LanguageType.EN -> {
                    rssService.getEnNewsList()
                }

                RssApi.LanguageType.KR -> {
                    rssService.getKrNewsList()
                }
            }

            emit(item)
        }
    }
}

