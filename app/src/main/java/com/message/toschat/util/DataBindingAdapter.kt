package com.message.toschat.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class DataBindingAdapter {

    companion object {

        @JvmStatic
        @BindingAdapter("agentImageUrl")
        fun setImageUri(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                    .load(imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view)
        }
    }

}