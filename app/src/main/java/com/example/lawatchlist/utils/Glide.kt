package com.example.lawatchlist.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.example.lawatchlist.R

@GlideModule
open class GlideAppModule : AppGlideModule() {
}

object ImageLoader: GlideAppModule() {

    fun load(imageView: ImageView, url: String?) {
                Glide.with(
                    imageView.context,
                ).load("http://image.tmdb.org/t/p/w780$url")
                  //  .placeholder(R.drawable.empty_placeholder)
                    .error(R.drawable.empty_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)


    }
}


