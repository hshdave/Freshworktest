package com.example.freshworktest.views

import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshworktest.R
import com.example.freshworktest.adapter.GifAdapter
import com.example.freshworktest.dao.GifsDao
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import com.example.freshworktest.network.NetworkConnection
import com.example.freshworktest.presenter.ViewPresenter
import com.example.freshworktest.presenter.database.AppDatabase
import com.example.freshworktest.presenterImpl.SearchFragmentPresenterImpl
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_fragment.*
import retrofit2.Response

class SearchFragment : Fragment(), ViewPresenter.SearchFragmentView {


    private var searchPresenterImpl : SearchFragmentPresenterImpl? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var adapter : GifAdapter?=null

    private var trendingArrayList : ArrayList<Gifsmodel.Data>? = null
    private var searchArrayList : ArrayList<Gifsmodel.Data>? = null

    private var offset = 0

    private var db: AppDatabase? = null
    private var gifsDao : GifsDao? = null

    private var isSearch : Boolean = false
    private var searchQuery : String = ""

    private var isScrolling : Boolean = false
    private var currentItems : Int = 0
    private var totalItem : Int = 0
    private var firstItem : Int =0

    private var netOffSet : Int = 0
    private var totalCount : Int = 1

    private var progress : SpinKitView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    private fun initUI()
    {
        gridLayoutManager = GridLayoutManager(this.context,2)
        search_recycler.setHasFixedSize(true)
        search_recycler.itemAnimator = DefaultItemAnimator()
        search_recycler.layoutManager = gridLayoutManager
        search_recycler.fitsSystemWindows = true

        trendingArrayList = ArrayList()
        searchArrayList = ArrayList()

        adapter = GifAdapter(trendingArrayList!!){ data ->  addToFavClicked(data) }
         search_recycler.adapter = adapter

        search_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 currentItems = gridLayoutManager!!.childCount
                 totalItem = gridLayoutManager!!.itemCount
                 firstItem  = gridLayoutManager!!.findFirstVisibleItemPosition()


                if (isScrolling && (currentItems + firstItem == totalItem))
                {
                    isScrolling = false
                    if (netOffSet>=offset || netOffSet==0)
                    {
                        offset = netOffSet + 25
                    }

                    if (isSearch && searchQuery != "")
                    {
                        searchPresenterImpl!!.loadSearchData(searchQuery, offset)
                    }else{
                        searchPresenterImpl!!.loadTrendingGif(offset)
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        progress = view.findViewById(R.id.spin_kit)

        searchPresenterImpl = SearchFragmentPresenterImpl(this)
        searchPresenterImpl!!.loadTrendingGif(offset)

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
                    offset=0
                    searchPresenterImpl!!.loadSearchData(query, offset)
                    searchQuery = query
                    isSearch = true
                    searchArrayList?.clear()
                    adapter = GifAdapter(searchArrayList!!){ data ->  addToFavClicked(data) }
                    search_recycler.adapter = adapter
                    adapter!!.notifyDataSetChanged()
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
        noInternetLayout.visibility = View.VISIBLE
        Toast.makeText(activity?.applicationContext,"Please check your internet connection!", Toast.LENGTH_LONG)
            .show()
    }

    override fun onSuccess(reposnseModel: Response<Gifsmodel>) {
        if (totalCount==0)
        {
            return
        }

        progress?.visibility  = View.VISIBLE
        if (reposnseModel.body() != null)
        {
            noInternetLayout.visibility = View.GONE
            activity?.runOnUiThread(Runnable {

                netOffSet = reposnseModel.body()!!.pagination.offset
                totalCount = reposnseModel.body()!!.pagination.totalCount

               val gifArrayList : ArrayList<Gifsmodel.Data> = reposnseModel.body()!!.data.toCollection(ArrayList())

                if (isSearch)
                {
                    searchArrayList?.addAll(gifArrayList)
                }else{
                    trendingArrayList?.addAll(gifArrayList)
                }
                adapter!!.notifyDataSetChanged()
                progress?.visibility  = View.GONE
            })

        }
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(activity?.applicationContext, "Something Went Wrong", Toast.LENGTH_SHORT).show()
    }

    override fun checkInternet(): Boolean {
        return NetworkConnection.isNetworkConnected(activity?.applicationContext!!)
    }

    private fun addToFavClicked(data : Gifsmodel.Data)
    {
        val gifsRoom = Gifsroom(data.id,data.images.original.url)

        Observable.fromCallable {
            db = activity?.applicationContext?.let { AppDatabase.getAppDataBase(context = it) }
            gifsDao = db?.gifDao()
            with(gifsDao)
            {
                this?.insert(gifsRoom)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        Toast.makeText(activity?.applicationContext,"Added to Favourite!",Toast.LENGTH_LONG).show()
    }


}