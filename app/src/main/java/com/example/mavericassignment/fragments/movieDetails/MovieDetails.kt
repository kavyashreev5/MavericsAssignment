package com.example.mavericassignment.fragments.movieDetails

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mavericassignment.R
import com.example.mavericassignment.Utils
import com.example.mavericassignment.databinding.FragmentMovieDetailsBinding
import com.example.mavericassignment.fragments.movieList.MovieListVM
import com.example.mavericassignment.model.MovieDetails
import com.example.mavericassignment.model.Ratings
import com.example.mavericassignment.network.APIInterface
import com.example.mavericassignment.network.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieDetails : Fragment() {

    lateinit var mBinding: FragmentMovieDetailsBinding
    lateinit var mViewModel: MovieDetailVM
    var imdbID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = arguments
        imdbID = args?.getString("ID")
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Movie Details"
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
    }

    private fun setViewModel() {
        activity?.let {
            mViewModel =
                ViewModelProviders.of(it).get(MovieDetailVM::class.java)
        }

        imdbID?.let {
            RetrofitFactory.apiService.getMovieById("b9bd48a6", it).enqueue(object : Callback<MovieDetails> {
                override fun onResponse(
                    call: Call<MovieDetails>,
                    response: Response<MovieDetails>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var movieDetails: MovieDetails = response.body()!!
                            mBinding.textView.text = movieDetails.Title
                            mBinding.textView2.text = movieDetails.Year
                            context?.let {
                                Glide.with(it).load(movieDetails.Poster).into(mBinding.imageView)
                            }
                            mBinding.textView3.text = movieDetails.Runtime
                            mBinding.textView5.text = "*" + movieDetails.imdbRating
                            mBinding.textView7.text = movieDetails.Plot
                            mBinding.languages.text = movieDetails.Language
                            var ratings = movieDetails.Ratings
                            if (ratings != null && ratings.size > 0) {
                                mBinding.score.text = ratings[0].Value
                                mBinding.popularity.text = ratings[1].Value
                                mBinding.reviews.text = ratings[2].Value
                            }
                            mBinding.director.text = movieDetails.Director
                            mBinding.actor.text = movieDetails.Actors
                            mBinding.writer.text = movieDetails.Writer
                        }
                    }
                }

                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }

    override fun onResume() {
        super.onResume()
        context?.let { Utils.hideSoftKeyBoard(it, mBinding.root) }
    }
}