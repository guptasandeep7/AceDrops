package com.example.acedrops.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.acedrops.R
import com.google.android.material.button.MaterialButton

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url: String?) {
    if (url != null)
        this.load(url) {
            placeholder(R.drawable.placeholder)
            crossfade(true)
        }
}

@BindingAdapter("setImage")
fun ImageView.setImage(rId: Int) {
    this.setImageResource(rId)
}

@BindingAdapter("toStringText")
fun TextView.toStringText(long: Long) {
    this.text = "${resources.getString(R.string.Rs)}$long"
}

@BindingAdapter("setBackground")
fun TextView.setBackground(int: Int) {
    if (int < 2) this.setBackgroundResource(R.drawable.ic_delete)
    else this.setBackgroundResource(R.drawable.ic_minus)
}

@BindingAdapter("toText")
fun TextView.toText(int: Int) {
    this.text = int.toString()
}

@BindingAdapter("wishlistStatus")
fun MaterialButton.wishlistStatus(status: Int) {
    if (status == 0) this.text = resources.getString(R.string.save_for_later)
    else this.text = resources.getString(R.string.remove_from_wishlist)
}

