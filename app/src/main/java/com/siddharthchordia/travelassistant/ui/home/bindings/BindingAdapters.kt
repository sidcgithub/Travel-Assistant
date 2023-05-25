package com.siddharthchordia.travelassistant.ui.home.bindings

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.siddharthchordia.travelassistant.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

    Glide.with(imgView.context)
        .load(imgUrl)
        .placeholder(R.drawable.map_image)
        .error(R.drawable.error_image)
        .priority(Priority.HIGH)
        .into(imgView)

}