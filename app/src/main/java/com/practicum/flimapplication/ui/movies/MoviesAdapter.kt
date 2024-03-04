package com.practicum.flimapplication.ui.movies

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.flimapplication.domain.models.Movie

class MoviesAdapter(private val clickListener: MovieClickListener) : RecyclerView.Adapter<MovieViewHolder>() {

    var movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(parent, clickListener)

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    interface MovieClickListener {
        fun onMovieClick(movie: Movie)
        fun onFavoriteToggleClick(movie: Movie)
    }
}