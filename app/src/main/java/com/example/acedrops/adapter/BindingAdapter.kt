package com.example.acedrops.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.acedrops.R

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url : String){
    this.load(url){
        placeholder(R.drawable.placeholder)
        crossfade(true)
    }
}