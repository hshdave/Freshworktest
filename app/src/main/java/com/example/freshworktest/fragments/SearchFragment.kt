package com.example.freshworktest.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.freshworktest.R
import com.example.freshworktest.adapter.GifAdapter
import com.example.freshworktest.dao.GifsDao
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import com.example.freshworktest.presenter.ViewPresenter
import com.example.freshworktest.presenter.database.AppDatabase
import com.example.freshworktest.presenterImpl.SearchFragmentPresenterImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_fragment.*
import retrofit2.Response

class SearchFragment : Fragment(), ViewPresenter.SearchFragmentView {


    private var searchPresenterImpl : SearchFragmentPresenterImpl? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var adapter : GifAdapter?=null
    private var arrayList : ArrayList<Gifsmodel.Data>? = null


    private var db: AppDatabase? = null
    private var gifsDao : GifsDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()

        println("onStart Called from Search")
    }

    override fun onResume() {
        super.onResume()

        println("onResume Called from Search")
    }

    fun initUI()
    {
        gridLayoutManager = GridLayoutManager(this.context,2)
        search_recycler.setHasFixedSize(true)
        search_recycler.itemAnimator = DefaultItemAnimator()
        search_recycler.layoutManager = gridLayoutManager
        search_recycler.fitsSystemWindows = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        searchPresenterImpl = SearchFragmentPresenterImpl(this)
        searchPresenterImpl!!.loadTrendingGif()

        db = activity?.applicationContext?.let { AppDatabase.getAppDataBase(context = it) }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        val inflater = inflater
        inflater.inflate(R.menu.searchmenu, menu)
        val searchItem = menu?.findItem(R.id.searchGifs)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchItem.collapseActionView()

                if (query != null) {
                    searchPresenterImpl!!.loadSearchData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenterImpl!!.onStop()
    }

    override fun validateError() {
        println("validateError")
    }

    override fun onSuccess(reposnseModel: Response<Gifsmodel>) {
        if (reposnseModel.body() != null)
        {

            activity?.runOnUiThread(Runnable {
                arrayList = reposnseModel.body()!!.data.toCollection(ArrayList())

                adapter = GifAdapter(arrayList!!){
                        data ->  addToFavClicked(data) }

                search_recycler.adapter = adapter

                adapter!!.notifyDataSetChanged()
            })

        }
    }

    override fun onError(throwable: Throwable) {
        println("Error ${throwable.message}")
    }

    private fun addToFavClicked(data : Gifsmodel.Data)
    {
        val gifsroom = Gifsroom(data.id,data.images.original.url)

        Observable.fromCallable {
            db = activity?.applicationContext?.let { AppDatabase.getAppDataBase(context = it) }
            gifsDao = db?.gifDao()
            with(gifsDao)
            {
                this?.insert(gifsroom)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        Toast.makeText(activity?.applicationContext,"Added to Fav ${data.id}",Toast.LENGTH_LONG).show()
    }


}