package com.example.mavericassignment.fragments.movieList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mavericassignment.dataSource.MovieDataSource
import com.example.mavericassignment.dataSource.MovieDataSourceFactory
import com.example.mavericassignment.model.ListData
import com.example.mavericassignment.model.MovieData

class MovieListVM(private val context: Context) : ViewModel() {

    var movieList : LiveData<PagedList<MovieData>> = MutableLiveData<PagedList<MovieData>>()
    var mutableLiveData = MutableLiveData<MovieDataSource>()


    fun getData() : LiveData<PagedList<MovieData>>{
        return movieList
    }

}