package com.practicum.flimapplication.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.flimapplication.util.Creator
import com.practicum.flimapplication.R
import com.practicum.flimapplication.domain.models.Movie
import com.practicum.flimapplication.presentation.movies.MoviesSearchViewModel
import com.practicum.flimapplication.presentation.movies.MoviesView
import com.practicum.flimapplication.presentation.movies.models.MoviesState
import com.practicum.flimapplication.presentation.movies.models.SingleLiveEvent
import com.practicum.flimapplication.ui.poster.PosterActivity

class MoviesActivity : ComponentActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = MoviesAdapter(
        object : MoviesAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                if (clickDebounce()) {
                    val intent = Intent(this@MoviesActivity, PosterActivity::class.java)
                    intent.putExtra("poster", movie.image)
                    startActivity(intent)
                }
            }

            override fun onFavoriteToggleClick(movie: Movie) {
                // 1
                viewModel.toggleFavorite(movie)
            }

        }
    )

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textWatcher: TextWatcher

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: MoviesSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MoviesSearchViewModel.getViewModelFactory())[MoviesSearchViewModel::class.java]

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.locations)
        progressBar = findViewById(R.id.progressBar)

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeShowToast().observe(this) {
            showToast(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}