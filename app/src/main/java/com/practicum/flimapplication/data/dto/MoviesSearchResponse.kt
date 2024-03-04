package com.practicum.flimapplication.data.dto

import com.practicum.flimapplication.domain.models.Movie


class MoviesSearchResponse(val searchType: String,
                           val expression: String,
                           val results: List<MovieDto>) : Response()