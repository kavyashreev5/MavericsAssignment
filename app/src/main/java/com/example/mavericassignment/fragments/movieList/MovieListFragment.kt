package com.example.mavericassignment.fragments.movieList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mavericassignment.R
import com.example.mavericassignment.Utils
import com.example.mavericassignment.adapter.MovieListAdapter
import com.example.mavericassignment.databinding.FragmentMovieListBinding
import com.example.mavericassignment.model.ListData
import com.example.mavericassignment.model.MovieData
import com.example.mavericassignment.network.APIInterface
import com.example.mavericassignment.network.RetrofitFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MovieListFragment : Fragment() {

    private lateinit var mViewModel: MovieListVM
    lateinit var mBinding: FragmentMovieListBinding
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Movie List"

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
    }

    private fun setViewModel() {
        activity?.let {
            mViewModel =
                ViewModelProviders.of(it).get(MovieListVM::class.java)
        }
        mBinding.movieName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty() && p0.length > 5) {
                    val request = RetrofitFactory.buildService(APIInterface::class.java)
                    val call = request.getMovieByTitle("b9bd48a6", p0.toString(), "movie")

                    call.enqueue(object : Callback<ListData> {
                        override fun onResponse(
                            call: Call<ListData>,
                            response: Response<ListData>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                mBinding.noData.visibility = GONE
                                mBinding.movieList.visibility = VISIBLE
                                Log.d("Movie List == ", response.body().toString())
                                mBinding.movieList.apply {
                                    mBinding.movieList.layoutManager = GridLayoutManager(context, 2)

                                    movieListAdapter = response.body()!!.Search?.let { it1 ->
                                        MovieListAdapter(
                                            it1
                                        )
                                    }!!

                                    mBinding.movieList.adapter = movieListAdapter
                                    movieListAdapter.onItemClick = { movieData ->
                                        val bundle = Bundle()
                                        bundle.putString("ID", movieData.imdbID)
                                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
                                    }

                                }
                            } else {
                                mBinding.noData.visibility = VISIBLE
                                mBinding.movieList.visibility = GONE
                            }
                        }

                        override fun onFailure(call: Call<ListData>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        context?.let { Utils.hideSoftKeyBoard(it, mBinding.root) }
    }
}