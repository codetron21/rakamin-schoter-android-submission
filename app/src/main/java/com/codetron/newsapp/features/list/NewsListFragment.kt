package com.codetron.newsapp.features.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.codetron.newsapp.LoadDataListener
import com.codetron.newsapp.MainActivity
import com.codetron.newsapp.R
import com.codetron.newsapp.api.NewsApiService
import com.codetron.newsapp.features.list.controller.NewsListController
import com.codetron.newsapp.utils.toDp

class NewsListFragment : Fragment(R.layout.fragment_news_list) {

    private lateinit var viewModel: NewsListViewModel

    private lateinit var listNews: RecyclerView
    private lateinit var newsListController: NewsListController
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private var loadDataListener: LoadDataListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadDataListener = (requireActivity() as MainActivity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    override fun onDetach() {
        loadDataListener = null
        super.onDetach()
    }

    private fun setup() {
        initView()
        initViewModel()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            loadDataListener?.onError(error)
            if (error != null) {
                newsListController.emptyData()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            swipeRefresh.isRefreshing = false
            loadDataListener?.onLoading(loading)
        }

        viewModel.articles.observe(viewLifecycleOwner) { news ->
            Log.d(TAG, news.toString())
            newsListController.updateArticles(news)
        }

        viewModel.sources.observe(viewLifecycleOwner) { sources ->
            Log.d(TAG, sources.toString())
            newsListController.updateSources(sources)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            NewsListViewModel.viewModelFactory(NewsApiService.instance())
        )[NewsListViewModel::class.java].also {
            it.getTopHeadlineNews()
            it.getSources()
        }
    }

    private fun initView() {
        swipeRefresh = view?.findViewById(R.id.swipe_refresh)
            ?: throw IllegalStateException("View is not initialize")

        listNews = view?.findViewById(R.id.list_news)
            ?: throw IllegalStateException("View is not initialize")

        swipeRefresh.setOnRefreshListener {
            viewModel.getTopHeadlineNews()
            viewModel.getSources()
        }

        listNews.layoutManager = LinearLayoutManager(requireContext())

        newsListController = NewsListController(
            sourceClickListener = { id ->
                Log.d(TAG, id)
                newsListController.updateCheckedSources(id)
                viewModel.getTopHeadlineNewsWithSource(id)
            },
            itemClickListener = { url ->
                Log.d(TAG, url)
                Intent(Intent.ACTION_VIEW).apply {
                    data = url.toUri()
                    startActivity(this)
                }
            })
            .apply {
                addTitles(
                    requireContext().getString(R.string.news_list_title_sources),
                    requireContext().getString(R.string.news_list_title_top_headlines),
                )
            }

        listNews.adapter = newsListController.adapter
        listNews.addItemDecoration(EpoxyItemSpacingDecorator(8F.toDp(requireContext()).toInt()))
    }

    companion object {
        private const val TAG = "NewsListFragment"
    }
}