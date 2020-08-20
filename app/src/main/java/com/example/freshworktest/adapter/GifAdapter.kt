package com.example.freshworktest.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.freshworktest.R
import com.example.freshworktest.model.Gifsmodel
import com.github.ybq.android.spinkit.SpinKitView
import kotlinx.android.synthetic.main.recyclerviewitem.view.*


class GifAdapter( private var data: ArrayList<Gifsmodel.Data>, private var listener: (Gifsmodel.Data) -> Unit) : RecyclerView.Adapter<GifAdapter.GifAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifAdapterViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewitem,parent,false)

        return GifAdapterViewHolder(view)
    }

    override fun getItemCount() = data!!.size

    override fun onBindViewHolder(holder: GifAdapterViewHolder, position: Int) {
        if (data!!.size  > 0)
        {
            val data = data?.get(position)
            if (data != null) {
                listener?.let { holder.bind(data, it) }
            }
        }
    }

    class GifAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        private val imageViewGIF: ImageView = itemView.gif_ImageView
        private val progress: SpinKitView = itemView.spin_kitRItem

        fun bind(data : Gifsmodel.Data, listener:(Gifsmodel.Data)->Unit)
        {

            progress.visibility = View.VISIBLE

            Glide.with(itemView.context)
                .asGif()
                .load(data.images.original.url)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade(250))
                .into(imageViewGIF)
            itemView.favButton.setOnClickListener{listener(data)
               itemView.favButton.isFavorite = true

                progress.visibility = View.GONE
           }
        }

    }
}