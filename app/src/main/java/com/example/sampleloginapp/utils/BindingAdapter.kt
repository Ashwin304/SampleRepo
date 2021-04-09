package com.example.sampleloginapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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

@BindingAdapter("date")
fun dateConvert(view: TextView, date: String) {

    val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    val date1: Date = originalFormat.parse(date)
    val formattedDate: String = targetFormat.format(date1)
    view.text = formattedDate
}


@BindingAdapter("norm_date")

fun normalDateConvert(view: TextView, date: String){
    val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat("dd LLLL, yyyy")
    val date1: Date = originalFormat.parse(date)
    val formattedDate: String = targetFormat.format(date1)
    view.text = formattedDate
}
