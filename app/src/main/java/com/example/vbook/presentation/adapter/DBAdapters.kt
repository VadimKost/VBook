package com.example.vbook.presentation.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import javax.inject.Inject


@BindingAdapter("android:src", )
fun loadImage(view: ImageView, url: String?) {
    val p= Picasso.with(view.getContext())
    p.setIndicatorsEnabled(true)
    p.load(url).centerCrop().fit().into(view)
}