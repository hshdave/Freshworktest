package com.example.freshworktest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.freshworktest.R
import com.example.freshworktest.dao.GifsDao
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import com.example.freshworktest.presenter.database.AppDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.recyclerviewitem.view.*
import okhttp3.internal.notifyAll

class FavRecycleAdapter(private var data: ArrayList<Gifsroom>) :  RecyclerView.Adapter<FavRecycleAdapter.FavadapterViewHolder>() {

    private var db: AppDatabase? = null
    private var gifsDao : GifsDao? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavadapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerviewitem,parent,false)

        return FavadapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavadapterViewHolder, position: Int) {
        if (data!!.size > 0)
        {
            holder.favBind(data[position])
            holder.favButton.setOnClickListener {

                removeFromFavClicked(data[position],holder.itemView)
                removeItem(position)

            }
        }
    }

    fun removeItem(position: Int)
    {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }
    override fun getItemCount(): Int = data.size


    private fun removeFromFavClicked(data : Gifsroom,view :View)
    {
        Observable.fromCallable {
            db = view?.context.let { AppDatabase.getAppDataBase(context = it) }
            gifsDao = db?.gifDao()
            with(gifsDao)
            {
                this?.deleteGifs(data.id)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        Toast.makeText(view.context,"Removed from Favourite!", Toast.LENGTH_LONG).show()
    }

    class FavadapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val imageViewGIF = itemView.gif_ImageView
        val progress = itemView.recycle_progress
        val favButton = itemView.favButton

        fun favBind(favGifs : Gifsroom)
        {
            progress.visibility = View.VISIBLE
            itemView.favButton.isFavorite = true

            Glide.with(itemView.context)
                .asGif()
                .load(favGifs.url)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade(250))
                .into(imageViewGIF)

            favButton.isFavorite = true
        }


    }
}