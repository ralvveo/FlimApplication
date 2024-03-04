package com.practicum.flimapplication.domain.api

import com.practicum.flimapplication.domain.models.Movie
import com.practicum.flimapplication.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}