package com.example.wiki.ui.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wiki.R

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, url:String?){
    Log.d("onCreate", "loadImage: $url")
    if(url!=null){
        //load image on each row with Glide library
        Glide.with(view)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                    ): Boolean {
                        Log.d("onCreate","image loading error: ${e.toString()}")
                        view.setImageResource(R.drawable.placeholder)
                        return true
                    }

                    override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(view)


    }



}