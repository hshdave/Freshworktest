package com.example.freshworktest.presenterImpl

import androidx.lifecycle.ViewModel
import com.example.freshworktest.network.Apiclient
import com.example.freshworktest.presenter.ViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class SearchFragmentPresenterImpl : ViewPresenter.SearchFragmentPresenter, ViewModel {

    var searchFragmentView : ViewPresenter.SearchFragmentView? = null

    @NonNull
    var disposable : Disposable? = null

    constructor(searchFragmentView: ViewPresenter.SearchFragmentView?) {
        this.searchFragmentView = searchFragmentView
    }


    override fun loadSearchData(searchKey: String,offset : Int) {

        if (searchFragmentView!!.checkInternet()) {

            val apiclient = Apiclient()
            disposable = apiclient.getSearchGIFS(searchKey, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listResponse ->
                    val responseCode = listResponse.code()

                    when (responseCode) {
                        200, 201, 202 -> {
                            searchFragmentView!!.onSuccess(listResponse)
                        }
                        401 -> {
                        }
                        402 -> {
                        }
                        500 -> {
                        }
                        501 -> {
                        }
                    }
                }
        }else
        {
            searchFragmentView!!.validateError()
        }


    }

    override fun loadTrendingGif(offset : Int) {
        if (searchFragmentView!!.checkInternet()) {
            val apiclient = Apiclient()
            disposable = apiclient.getTrendingGIFS(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listResponse ->
                    val responseCode = listResponse.code()

                    when(responseCode) {
                        200, 201, 202 -> { searchFragmentView!!.onSuccess(listResponse) }
                        401 -> { }
                        402 -> { }
                        500 -> { }
                        501 -> { }
                    }
                }
        }else
        {
            searchFragmentView!!.validateError()
        }

    }

    override fun onStop() {
        if (disposable != null)
        {
            disposable!!.dispose()
        }
    }


}