package com.practicum.flimapplication.util

import android.content.Context
import com.practicum.flimapplication.data.network.MoviesRepositoryImpl
import com.practicum.flimapplication.data.network.RetrofitNetworkClient
import com.practicum.flimapplication.domain.api.MoviesInteractor
import com.practicum.flimapplication.domain.api.MoviesRepository
import com.practicum.flimapplication.domain.impl.MoviesInteractorImpl
import com.practicum.flimapplication.presentation.movies.MoviesSearchViewModel
import com.practicum.flimapplication.presentation.PosterPresenter
import com.practicum.flimapplication.presentation.movies.PosterView

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }





    fun providePosterPresenter(posterView: PosterView, imageUrl: String): PosterPresenter {
        return PosterPresenter(posterView, imageUrl)
    }
}