package com.codetron.newsapp.features.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetron.newsapp.api.NewsApiService
import com.codetron.newsapp.model.Article
import com.codetron.newsapp.model.Source
import com.codetron.newsapp.utils.errorMessage
import kotlinx.coroutines.*

class NewsListViewModel(
    private val newsApiService: NewsApiService
) : ViewModel() {

    private val coroutine = CoroutineScope(Dispatchers.Main.immediate)
    private val jobs = setOf<Job>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _sources = MutableLiveData<List<Source>>()
    val sources: LiveData<List<Source>> get() = _sources

    override fun onCleared() {
        super.onCleared()
        cleanUp()
    }

    fun getSources() {
        _error.value = null
        _loading.value = true

        coroutine.launch {
            runCatching {
                withContext(Dispatchers.IO) { newsApiService.getSources() }
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _sources.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = response.errorBody()?.errorMessage()
                }
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }.also {
            jobs.plus(it)
        }
    }

    fun getTopHeadlineNewsWithSource(id: String) {
        coroutine.launch {
            _articles.value = emptyList()
            _loading.value = true

            runCatching {
                withContext(Dispatchers.IO) { newsApiService.getTopHeadlinesWithSource(id) }
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _articles.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = response.errorBody()?.errorMessage()
                }
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }.also {
            jobs.plus(it)
        }
    }

    fun getTopHeadlineNews() {
        coroutine.launch {
            _error.value = null
            _loading.value = true

            runCatching {
                withContext(Dispatchers.IO) { newsApiService.getTopHeadlines() }
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _articles.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = response.errorBody()?.errorMessage()
                }
            }.onFailure {
                _error.value = it.message
            }

            _loading.value = false
        }.also {
            jobs.plus(it)
        }
    }

    private fun cleanUp() {
        if (jobs.isEmpty()) return
        jobs.forEach { it.cancel() }
    }

    companion object Factory {
        private const val TAG = "NewsListViewModel"

        @Suppress("UNCHECKED_CAST")
        fun viewModelFactory(service: NewsApiService) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NewsListViewModel(service) as T
            }
        }
    }

}