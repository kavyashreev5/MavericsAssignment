package com.example.mavericassignment.dataSource

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.example.mavericassignment.Utils
import com.example.mavericassignment.Utils.isInternetAvailable
import com.example.mavericassignment.Utils.showProgressBar
import com.example.mavericassignment.model.ListData
import com.example.mavericassignment.model.MovieData
import com.example.mavericassignment.network.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDataSource(private val context: Context, private val query: CharSequence) : PageKeyedDataSource<Int, MovieData>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieData>) {
        if (context.isInternetAvailable()) {
            getMovies(callback)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {
        if (context.isInternetAvailable()) {
            val nextPageNo = params.key + 1
            getMoreMovies(params.key, nextPageNo, callback)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {
        if (context.isInternetAvailable()) {
            val previousPageNo = if (params.key > 1) params.key - 1 else 0
            getMoreMovies(params.key, previousPageNo, callback)
        }
    }

    private fun getMovies(callback: LoadInitialCallback<Int, MovieData>) {

        context.showProgressBar()

        RetrofitFactory.apiService.getMovieByTitle(query as String,"").enqueue(object :Callback<ListData>{

            override fun onResponse(call: Call<ListData>, response: Response<ListData>) {
                Utils.hideProgressBar()
                val movieList = response.body()?.Search
                movieList?.let { callback.onResult(it, null, 2) }
            }

            override fun onFailure(call: Call<ListData>, t: Throwable) {
                Utils.hideProgressBar()
            }

        })

    }

    private fun getMoreMovies(pageNo: Int, previousOrNextPageNo: Int, callback: LoadCallback<Int, MovieData>) {

        RetrofitFactory.apiService.getMovieByTitle(query as String, "movie").enqueue(object : Callback<ListData> {
            override fun onResponse(call: Call<ListData>, response: Response<ListData>) {
                val movieResponse = response.body()?.Search
                movieResponse?.let { callback.onResult(it, previousOrNextPageNo) }
            }

            override fun onFailure(call: Call<ListData>, t: Throwable) {

            }

        })

    }

}