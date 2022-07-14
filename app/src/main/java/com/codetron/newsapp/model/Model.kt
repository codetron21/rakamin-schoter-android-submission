package com.codetron.newsapp.model

import com.squareup.moshi.Json

data class Article(
    @field:Json(name = "author") val author: String? = null,
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "source") val source: Source? = null,
    @field:Json(name = "publishedAt") val date: String? = null,
    @field:Json(name = "urlToImage") val imageUrl: String? = null,
    @field:Json(name = "url") val url: String? = null,
)

data class Source(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "category") val category: String? = null,
    @field:Json(name = "language") val language: String? = null,
    @field:Json(name = "country") val country: String? = null,
    var isChecked:Boolean = false
)

data class ArticleData(
    @field:Json(name = "status") val status: String,
    @field:Json(name = "totalResults") val total: Int,
    @field:Json(name = "articles") val data: List<Article>
)

data class SourcesData(
    @field:Json(name = "status") val status: String,
    @field:Json(name = "sources") val data: List<Source>
)

data class ErrorData(
    @field:Json(name = "status") val status: String? = null,
    @field:Json(name = "code") val code: String? = null,
    @field:Json(name = "message") val message: String? = null,
)