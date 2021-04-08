package com.example.sampleloginapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("cicle_crop_image")
fun loadCircleImage(view: ImageView, url: String){
    Glide.with(view).load(url).apply(RequestOptions.circleCropTransform())
            .into(view)

}

@BindingAdapter("image")
fun loadImage(view: ImageView, url: String){
    Glide.with(view).load(url)
        .into(view)

}