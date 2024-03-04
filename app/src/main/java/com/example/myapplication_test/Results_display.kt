package com.example.myapplication_test


import Movielist
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.results_display_list.view.overviewTextView
import kotlinx.android.synthetic.main.results_display_list.view.posterImageView
import kotlinx.android.synthetic.main.results_display_list.view.titleTextView

class Results_display  : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_display)

        // 영화list 어댑터
        recyclerView = findViewById(R.id.movie_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val movieTitles = Search.titles // Search파일에서 영화제목 배열 받아옴
        val movieOverviews = Search.overviews // Search파일에서 영화제목 배열 받아옴
        val moviePosterpaths = Search.poster_paths // Search파일에서 영화제목 배열 받아옴

        val movieList = ArrayList<Movielist>()

        for (i in movieTitles.indices) {
            Log.d("MyTag", movieTitles[i])

            movieList.add(Movielist(movieTitles[i], movieOverviews[i], moviePosterpaths[i]))

        }

        adapter = MovieAdapter(movieList)
        Log.d("MyTag", movieList.size.toString()) //왜 0이지..?


        recyclerView.adapter = adapter




        // **tmdb 응답 값 처리코드 작성해야 함**
        /*
    adult: 해당 영화가 성인용으로 지정되었는지를 나타내는 부울 값입니다.
    backdrop_path: 영화 배경 화면의 이미지 경로를 나타냅니다.
    genre_ids: 해당 영화의 장르 식별자(ID) 목록을 나타냅니다.
    id: 해당 영화의 고유 식별자(ID)를 나타냅니다.
    original_language: 해당 영화의 원본 언어를 나타냅니다.
    original_title: 해당 영화의 원본 제목을 나타냅니다.
    overview: 해당 영화의 개요(줄거리)를 나타냅니다.
    popularity: 해당 영화의 인기도를 나타내는 수치입니다.
    poster_path: 영화 포스터의 이미지 경로를 나타냅니다.
    release_date: 해당 영화의 개봉일을 나타냅니다.
    title: 해당 영화의 제목을 나타냅니다.
    video: 해당 영화에 비디오가 있는지 여부를 나타내는 부울 값입니다.
    vote_average: 해당 영화의 평균 평점을 나타내는 수치입니다.
    vote_count: 해당 영화에 대한 평가(투표) 수를 나타내는 정수 값입니다.

    */








    }



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
            Log.d("MyTag","끝")

            //else{
            //    holder.imagePoster.setImageResource(R.drawable.placeholder_poster)
            //}
        }



        override fun getItemCount(): Int {
            //Log.d("MyTag", movies.size.toString()) //왜 0이지..?

            return movies.size
        }


    }


}