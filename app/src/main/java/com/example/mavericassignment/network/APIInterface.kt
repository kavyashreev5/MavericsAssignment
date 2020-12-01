package com.example.mavericassignment.network

import com.example.mavericassignment.model.ListData
import com.example.mavericassignment.model.MovieDetails
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    //b9bd48a6
    @GET("?apikey=b9bd48a6")
    fun getMovieByTitle(@Query("s") title: String,
                        @Query("type") type: String):
            Call<ListData>

    @GET("?")
    fun getMovieById(@Query("apikey") apikey: String,
                     @Query("i") imdbID: String)
            : Call<MovieDetails>

}