package com.example.mavericassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mavericassignment.R
import com.example.mavericassignment.model.MovieData

class MovieListAdapter(mContext: Context):
     PagedListAdapter<MovieData, MovieListAdapter.ViewHolder>(MovieListDiffUtil){
    var data: ArrayList<MovieData> = ArrayList<MovieData>()

    var onItemClick: ((MovieData) -> Unit)? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val textView: TextView
        val imageView: ImageView
        lateinit var movie: MovieData
        var pos: Int = 0

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.text_view)
            imageView = view.findViewById(R.id.image_view)
            itemView.setOnClickListener(this)
        }

        fun setData(movieData: MovieData, position: Int) {
            movie = movieData
            pos = position
            textView.text = movieData.Title
            Glide.with(itemView.context).load(movieData.Poster).into(imageView)
        }

        override fun onClick(v: View) {
            onItemClick?.invoke(movie)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position], position)
    }

    companion object {
        private val MovieListDiffUtil = object : DiffUtil.ItemCallback<MovieData>() {
            override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean =
                oldItem.imdbID == newItem.imdbID
            override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean =
                newItem == oldItem
        }
    }

}