package com.practicum.flimapplication.presentation

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.practicum.flimapplication.R
import com.practicum.flimapplication.presentation.movies.PosterView

class PosterPresenter(
    private val view: PosterView,
    private val imageUrl: String,
) {

    fun onCreate() {
        view.setupPosterImage(imageUrl)
    }
}