package com.example.mavericassignment.dataSource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mavericassignment.model.MovieData

class MovieDataSourceFactory(private val context: Context, val p0: CharSequence) : DataSource.Factory<Int, MovieData>() {

    val mutableLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MovieData> {
        val movieDataDataSource = MovieDataSource(context, p0)
        mutableLiveData.postValue(movieDataDataSource)
        return movieDataDataSource
    }

}