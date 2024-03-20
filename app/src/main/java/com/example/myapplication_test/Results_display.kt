package com.example.myapplication_test


import Movielist
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search.back_button

class Results_display  : AppCompatActivity(), MovieAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_display)

        back_button.setOnClickListener {
            startActivity(Intent(applicationContext, Search::class.java))
            finish()
        }


        // 영화 리사이클러뷰 어댑터 생성
        recyclerView = findViewById(R.id.movie_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Results_display 액티비티에서 전달된 데이터 가져오기
        val intent = intent
        val movieTitles = intent.getStringArrayListExtra("titles")
        val movieOverviews = intent.getStringArrayListExtra("overviews")
        val moviePosterpaths = intent.getStringArrayListExtra("posterpaths")

        Log.d("MyTag", "전달된 데이터 내용:$movieTitles") // 결과 정상

        // 리사이클러뷰 요소 리스트 생성
        val movieList = ArrayList<Movielist>()
        if (movieTitles != null && movieOverviews != null && moviePosterpaths != null) {
            for (i in movieTitles.indices) {

                // **리스트 요소 추가 필요**
                movieList.add(Movielist(movieTitles[i], movieOverviews[i], moviePosterpaths[i]))

            }
        }

        adapter = MovieAdapter(movieList, this)
        Log.d("MyTag", "전달된 데이터 사이즈:"+ movieTitles?.size.toString()) // 결과 정상

        recyclerView.adapter = adapter




        // tmdb 응답값 설명
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

    // 클릭된 아이템의 정보를 받아 상세화면으로 전환하는 메소드
    override fun onItemClick(movie: Movielist) {
        val intent = Intent(this, MovieDetail::class.java).apply {
            putExtra("movie", movie) // 클릭된 아이템의 정보를 Intent에 추가하여 전달
        }
        startActivity(intent) // 상세화면 액티비티로 전환
    }

}