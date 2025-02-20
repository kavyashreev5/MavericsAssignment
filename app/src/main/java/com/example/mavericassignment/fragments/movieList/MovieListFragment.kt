package com.example.mavericassignment.fragments.movieList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mavericassignment.R
import com.example.mavericassignment.Utils
import com.example.mavericassignment.adapter.MovieListAdapter
import com.example.mavericassignment.dataSource.MovieDataSourceFactory
import com.example.mavericassignment.databinding.FragmentMovieListBinding
import com.example.mavericassignment.model.MovieData

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

                        val factory : MovieDataSourceFactory by lazy {
                            MovieDataSourceFactory(context!!, p0)
                        }
                        mViewModel.mutableLiveData = factory.mutableLiveData

                        val config = PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .build()

                        mViewModel.movieList = LivePagedListBuilder(factory, config)
                            .build()

                    mViewModel.getData().observe(viewLifecycleOwner, object : Observer<PagedList<MovieData>> {
                        override fun onChanged(t: PagedList<MovieData>?) {
                            if (t != null && t.size > 0) {
                                mBinding.noData.visibility = GONE
                                mBinding.movieList.visibility = VISIBLE

                                mBinding.movieList.apply {
                                    mBinding.movieList.layoutManager =
                                        GridLayoutManager(context, 2)

                                    movieListAdapter.submitList(t)
                                    mBinding.movieList.adapter = movieListAdapter
                                    movieListAdapter.onItemClick = { movieData ->
                                        val bundle = Bundle()
                                        bundle.putString("ID", movieData.imdbID)
                                        findNavController().navigate(
                                            R.id.action_FirstFragment_to_SecondFragment,
                                            bundle
                                        )
                                    }
                                }
                            }else{
                                mBinding.noData.visibility = VISIBLE
                                mBinding.movieList.visibility = GONE
                            }

                        }
                    })
                    /*val request = RetrofitFactory.buildService(APIInterface::class.java)
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
                                       movieListAdapter.submitList(it1)
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
                    })*/
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        context?.let { Utils.hideSoftKeyBoard(it, mBinding.root) }
    }
}