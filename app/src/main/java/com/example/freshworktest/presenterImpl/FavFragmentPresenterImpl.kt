package com.example.freshworktest.presenterImpl

import android.content.Context
import com.example.freshworktest.dao.GifsDao
import com.example.freshworktest.presenter.ViewPresenter
import com.example.freshworktest.presenter.database.AppDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavFragmentPresenterImpl : ViewPresenter.FavouriteFragmentPresnter {

    private var db: AppDatabase? = null
    private var gifsDao : GifsDao? = null
    private var context : Context? = null

    var favouriteFragmentView : ViewPresenter.FavouriteFragmentView? = null
    @NonNull
    var disposable : Disposable? = null



    constructor(context: Context?, favouriteFragmentView: ViewPresenter.FavouriteFragmentView?) {
        this.context = context
        this.favouriteFragmentView = favouriteFragmentView
    }

    override fun loadFavouriteGifs() {

       disposable = Observable.fromCallable {
            db = context?.applicationContext?.let { AppDatabase.getAppDataBase(context = it) }
            gifsDao = db?.gifDao()

            db?.gifDao()?.getAllGifs()

        }.doOnNext { list ->
           if (list != null) {
               favouriteFragmentView?.getFavouriteGifs(list)
           }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


    override fun onStop() {

        if (disposable != null)
        {
            disposable!!.dispose()
        }
    }
}