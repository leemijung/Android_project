package com.example.myapplication_test


import Movielist
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.results_display_list.view.*

class MovieAdapter(private val movies: List<Movielist>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // results_display_list 요소와 연결

        val textTitle: TextView = itemView.titleTextView
        val textOverview: TextView = itemView.overviewTextView
        val imagePoster: ImageView = itemView.posterImageView
        // **스크롤 리스트에 요소 더 추가? **



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.results_display_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        holder.textTitle.text = movie.title
        holder.textOverview.text = movie.overview

        //Log.d("MyTag", movie.title)
        Log.d("MyTag", movie.poster_path.toString())

        // 포스터이미지 로드
        if (!movie.poster_path.isNullOrBlank()) {
            val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
        Picasso.get().load(posterUrl).into(holder.imagePoster)
        }


        //else{
        //    holder.imagePoster.setImageResource(R.drawable.placeholder_poster)
        //}
    }



    override fun getItemCount(): Int {
        return movies.size
    }


}
