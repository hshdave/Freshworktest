package com.example.freshworktest.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.freshworktest.R
import com.example.freshworktest.model.Gifsmodel
import kotlinx.android.synthetic.main.recyclerviewitem.view.*

class Gifadapter(var data : ArrayList<Gifsmodel.Data>) : RecyclerView.Adapter<Gifadapter.GifadapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifadapterViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewitem,parent,false)

        return GifadapterViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: GifadapterViewHolder, position: Int) {
        if (data.size > 0)
        {
            val data = data[position]
            holder.bind(data)
        }
    }

    class GifadapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val imageViewGIF = itemView.gif_ImageView
        val progress = itemView.recycle_progress
        fun bind(data : Gifsmodel.Data)
        {
            progress.visibility = View.VISIBLE

            Glide.with(itemView.context)
                .asGif()
                .load(data.images.original.url)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade(250))
                .into(imageViewGIF)


        }
    }
}