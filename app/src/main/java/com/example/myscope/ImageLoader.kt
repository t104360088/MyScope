package com.example.myscope

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object ImageLoader {
    fun loadImage(imageView: ImageView?, url: String?, type: ImageType = ImageType.Avatar){
        if (imageView == null) return

        when(type){
            ImageType.Avatar->{
                Glide.with(imageView.context).load(url).apply(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.avatar)
                        .error(android.R.drawable.ic_menu_report_image)
                        .dontAnimate())
                    .into(imageView)
            }
            else->{
                Glide.with(imageView.context).load(url).apply(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .dontAnimate())
                    .into(imageView)
            }
        }
    }
}

enum class ImageType {
    Avatar,
    Background,
    Article
}