package com.codetron.newsapp.features.list.controller

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.codetron.newsapp.features.list.views.NewsSourceModel_
import com.codetron.newsapp.features.list.views.newsHeaderView
import com.codetron.newsapp.features.list.views.newsItemView
import com.codetron.newsapp.model.Article
import com.codetron.newsapp.model.Source

class NewsListController(
    private val sourceClickListener: ((String) -> Unit)? = null,
    private val itemClickListener: ((String) -> Unit)? = null,
) : EpoxyController() {

    private var articles = mutableListOf<Article>()
    private var sources = mutableListOf<Source>()
    private var title1: String? = null
    private var title2: String? = null

    fun addTitles(title1: String, title2: String) {
        this.title1 = title1
        this.title2 = title2
        requestModelBuild()
    }

    fun updateArticles(articles: List<Article>) {
        this.articles.clear()
        this.articles.addAll(articles)
        requestModelBuild()
    }

    fun updateSources(sources: List<Source>) {
        this.sources.clear()
        this.sources.addAll(sources)
        requestModelBuild()
    }

    fun updateCheckedSources(id: String) {
        val updateList = this.sources.map {
            it.isChecked = it.id == id
            it
        }
        this.sources.clear()
        this.sources.addAll(updateList)

        requestModelBuild()
    }

    fun emptyData() {
        this.articles.clear()
        this.sources.clear()
        requestModelBuild()
    }

    override fun buildModels() {
        if (sources.isEmpty()) return

        title1?.let {
            newsHeaderView {
                this.id(it)
                this.textHeader(it)
            }
        }

        val sourceViews = sources.map { source ->
            NewsSourceModel_()
                .id(source.id)
                .title(source.name.toString())
                .stateChecked(source.isChecked)
                .listener {
                    sourceClickListener?.invoke(source.id.toString())
                }
        }

        carousel {
            this.id("sources_carousel")
            this.models(sourceViews)
            this.numViewsToShowOnScreen(3.15F)
            this.padding(Carousel.Padding.dp(0, 8))
        }

        if (articles.isEmpty()) return

        title2?.let {
            newsHeaderView {
                this.id(it)
                this.textHeader(it)
            }
        }

        articles.forEach { article ->
            newsItemView {
                this.id(article.url)
                this.image(article.imageUrl)
                this.title(article.title.orEmpty())
                this.description(article.description.orEmpty())
                this.sources(article.source?.name.orEmpty())
                this.listener {
                    article.url?.let { it1 -> this@NewsListController.itemClickListener?.invoke(it1) }
                }
            }
        }
    }
}