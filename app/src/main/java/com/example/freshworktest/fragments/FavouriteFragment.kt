package com.example.freshworktest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.freshworktest.R
import com.example.freshworktest.adapter.FavRecycleAdapter
import com.example.freshworktest.adapter.GifAdapter
import com.example.freshworktest.dao.GifsDao
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import com.example.freshworktest.presenter.ViewPresenter
import com.example.freshworktest.presenter.database.AppDatabase
import com.example.freshworktest.presenterImpl.FavFragmentPresenterImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.search_recycler
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() , ViewPresenter.FavouriteFragmentView{

    private var favFragmentPresenterImpl : FavFragmentPresenterImpl? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var adapter : FavRecycleAdapter?=null
    private var arrayList : ArrayList<Gifsroom>? = null

    private var db: AppDatabase? = null
    private var gifsDao : GifsDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        println("onResume Called from Fav")
        favFragmentPresenterImpl!!.loadFavouriteGifs()
        adapter?.notifyDataSetChanged()
    }


    override fun onStart() {
        super.onStart()
        println("onResume Called from Fav")
    }

    fun initUI()
    {
        gridLayoutManager = GridLayoutManager(this.context,2)
        fav_recyclerView.setHasFixedSize(true)
        fav_recyclerView.itemAnimator = DefaultItemAnimator()
        fav_recyclerView.layoutManager = gridLayoutManager
        fav_recyclerView.fitsSystemWindows = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        println("onViewCreated called!")

        favFragmentPresenterImpl = FavFragmentPresenterImpl(activity?.applicationContext,this)
    }



    override fun getFavouriteGifs(gifs: List<Gifsroom>)
    {

        activity?.runOnUiThread(Runnable {
            if (gifs.isNotEmpty())
            {
                arrayList = gifs.toCollection(ArrayList())

                adapter = FavRecycleAdapter(arrayList!!)

                fav_recyclerView.adapter = adapter

                adapter!!.notifyDataSetChanged()
            }
        })


    }

    private fun removeFromDatabase(gifs :Gifsroom)
    {
        Observable.fromCallable {
            db = activity?.applicationContext?.let { AppDatabase.getAppDataBase(context = it) }
            gifsDao = db?.gifDao()
            with(gifsDao)
            {
                this?.deleteGifs(gifs.id)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


}