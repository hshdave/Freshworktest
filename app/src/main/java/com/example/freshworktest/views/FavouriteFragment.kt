package com.example.freshworktest.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.freshworktest.R
import com.example.freshworktest.adapter.FavRecycleAdapter
import com.example.freshworktest.dao.GifsDao
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.network.NetworkConnection
import com.example.freshworktest.presenter.ViewPresenter
import com.example.freshworktest.presenter.database.AppDatabase
import com.example.freshworktest.presenterImpl.FavFragmentPresenterImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.search_fragment.*

class FavouriteFragment : Fragment() , ViewPresenter.FavouriteFragmentView{

    private var favFragmentPresenterImpl : FavFragmentPresenterImpl? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var adapter : FavRecycleAdapter?=null
    private var arrayList : ArrayList<Gifsroom>? = null


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
        arrayList = ArrayList()
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
            if (gifs.isNotEmpty())
            {
                activity?.runOnUiThread(Runnable {

                    arrayList = gifs.toCollection(ArrayList())

                    adapter = FavRecycleAdapter(arrayList!!)

                    fav_recyclerView.adapter = adapter

                    adapter!!.notifyDataSetChanged()
                })

            }


    }

    override fun validateError() {
        noFavInternetLayout.visibility = View.VISIBLE
        Toast.makeText(activity?.applicationContext,"Please check your internet connection!", Toast.LENGTH_LONG)
            .show()
    }

    override fun checkInternet(): Boolean {
        return NetworkConnection.isNetworkConnected(activity?.applicationContext!!)
    }


}