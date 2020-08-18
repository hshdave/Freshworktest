package com.example.freshworktest.presenter

import com.example.freshworktest.model.Gifsmodel
import retrofit2.Response

interface ViewPresenter {

    interface SearchView{
        fun validateError()
        fun showProgressbar()
        fun hideProgressbar()
        fun onSuccess(reposnseModel: Response<Gifsmodel>)
        fun onError(throwable: Throwable)
    }

    interface SearchPresener{
        fun loadSearchData(searchKey : String)
        fun loadTrendingGif()
        fun onStop()
    }
}