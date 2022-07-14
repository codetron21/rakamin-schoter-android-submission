package com.codetron.newsapp

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import com.codetron.newsapp.features.list.NewsListFragment

class MainActivity : AppCompatActivity(), LoadDataListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var textError: TextView
    private lateinit var fragmentContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainer.id, NewsListFragment::class.java, null)
                .commit()
        }
    }

    override fun onLoading(isShow: Boolean) {
        progressBar.isVisible = isShow
    }

    override fun onError(message: String?) {
        textError.isVisible = !message.isNullOrBlank()
        textError.text = message
    }

    private fun setupView() {
        progressBar = findViewById(R.id.progress_bar)
        textError = findViewById(R.id.text_error)
        fragmentContainer = findViewById(R.id.fragment_container)
    }

}

interface LoadDataListener {
    fun onLoading(isShow: Boolean)
    fun onError(message: String?)
}