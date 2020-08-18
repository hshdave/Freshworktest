package com.example.freshworktest.presenterImpl

import com.example.freshworktest.model.Gifsmodel
import com.example.freshworktest.network.Apiclient
import com.example.freshworktest.presenter.ViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class SrarchpresenterImpl : ViewPresenter.SearchPresener {

    var searchView : ViewPresenter.SearchView? = null

    @NonNull
    var disposable : Disposable? = null

    constructor(searchView: ViewPresenter.SearchView?) {
        this.searchView = searchView
    }


    override fun loadSearchData(searchKey: String) {
        searchView!!.showProgressbar()

        val apiclient = Apiclient()
        disposable = apiclient.getSearchGIFS(searchKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listResponse -> searchView!!.hideProgressbar()
                val responseCode = listResponse.code()

                when(responseCode) {
                    200, 201, 202 -> { searchView!!.onSuccess(listResponse) }
                    401 -> { }
                    402 -> { }
                    500 -> { }
                    501 -> { }
                }
            }
    }

    override fun loadTrendingGif() {
        searchView!!.showProgressbar()

        val apiclient = Apiclient()
        disposable = apiclient.getTrendingGIFS()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listResponse -> searchView!!.hideProgressbar()
                val responseCode = listResponse.code()

                when(responseCode) {
                    200, 201, 202 -> { searchView!!.onSuccess(listResponse) }
                    401 -> { }
                    402 -> { }
                    500 -> { }
                    501 -> { }
                }
            }
    }

    override fun onStop() {
        if (disposable != null)
        {
            disposable!!.dispose()
        }
    }


}