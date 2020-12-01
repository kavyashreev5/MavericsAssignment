package com.example.mavericassignment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mavericassignment.fragments.movieList.MovieListVM

class ViewModelFactory( val context: Context) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListVM(context) as T
    }

}
