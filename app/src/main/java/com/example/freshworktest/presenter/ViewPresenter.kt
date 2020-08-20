package com.example.freshworktest.presenter

import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import retrofit2.Response

interface ViewPresenter {

    interface SearchFragmentView{
        fun validateError()
        fun onSuccess(reposnseModel: Response<Gifsmodel>)
        fun onError(throwable: Throwable)
        fun checkInternet(): Boolean
    }

    interface SearchFragmentPresenter{
        fun loadSearchData(searchKey : String,offset : Int)
        fun loadTrendingGif(offset : Int)
        fun onStop()
    }

    interface FavouriteFragmentView{
        fun getFavouriteGifs(gifs : List<Gifsroom>)
        fun validateError()
        fun checkInternet(): Boolean
    }
    interface FavouriteFragmentPresnter{
        fun loadFavouriteGifs()
        fun onStop()
    }

}