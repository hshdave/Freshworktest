package com.example.freshworktest.presenter

import android.content.Context
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import retrofit2.Response

interface ViewPresenter {

    interface SearchFragmentView{
        fun validateError()
        fun onSuccess(reposnseModel: Response<Gifsmodel>)
        fun onError(throwable: Throwable)
    }

    interface SearchFragmentPresenter{
        fun loadSearchData(searchKey : String)
        fun loadTrendingGif()
        fun onStop()
    }

    interface FavouriteFragmentView{
        fun getFavouriteGifs(gifs : List<Gifsroom>)
    }
    interface FavouriteFragmentPresnter{
        fun loadFavouriteGifs()
        fun onStop()
    }

}