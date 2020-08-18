package com.example.freshworktest

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.freshworktest.adapter.Gifadapter
import com.example.freshworktest.model.Gifsmodel
import com.example.freshworktest.presenter.ViewPresenter
import com.example.freshworktest.presenterImpl.SrarchpresenterImpl
import kotlinx.android.synthetic.main.fragment_search_fragment.*
import retrofit2.Response

class SearchFragment : Fragment(), ViewPresenter.SearchView {


    private var searchPresenterImpl : SrarchpresenterImpl? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var adapter : Gifadapter?=null
    private var arrayList : ArrayList<Gifsmodel.Data>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        searchPresenterImpl = SrarchpresenterImpl(this)
        searchPresenterImpl!!.loadTrendingGif()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_fragment, container, false)
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

    override fun showProgressbar() {

    }

    override fun hideProgressbar() {
    }

    override fun onSuccess(reposnseModel: Response<Gifsmodel>) {
        if (reposnseModel.body() != null)
        {
            arrayList = reposnseModel.body()!!.data.toCollection(ArrayList())
            adapter = Gifadapter(arrayList!!)
            search_recycler.adapter = adapter
        }
    }

    override fun onError(throwable: Throwable) {
        println("Error ${throwable.message}")
    }



}