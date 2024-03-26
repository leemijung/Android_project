package com.example.myapplication_test


import Movielist
import Tvlist
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search.back_button

class Results_display  : AppCompatActivity(), MovieAdapter.OnItemClickListener, TvAdapter.OnItemClickListener {

    private lateinit var MovierecyclerView: RecyclerView
    private lateinit var TvrecyclerView: RecyclerView

    private lateinit var Movieadapter: MovieAdapter
    private lateinit var Tvadapter: TvAdapter

    private var movieRecyclerViewState: Parcelable? = null
    private var tvRecyclerViewState: Parcelable? = null

    var movieList = ArrayList<Movielist>() // 영화 리스트 배열 저장
    var tvList = ArrayList<Tvlist>() // 드라마 리스트 배열 저장


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_display)

        back_button.setOnClickListener {
            startActivity(Intent(applicationContext, Search::class.java))
            finish()
        }


        // 영화 리사이클러뷰 어댑터 생성
        MovierecyclerView = findViewById(R.id.movie_recyclerView)
        MovierecyclerView.layoutManager = LinearLayoutManager(this)
        // 드라마 리사이클러뷰 어댑터 생성
        TvrecyclerView = findViewById(R.id.drama_recyclerView)
        TvrecyclerView.layoutManager = LinearLayoutManager(this)

        // savedInstanceState에서 리사이클러뷰 상태 복원
        if (savedInstanceState != null) {
            movieRecyclerViewState = savedInstanceState.getParcelable("movieRecyclerViewState")
            val savedMovieList = savedInstanceState.getParcelableArrayList<Movielist>("movieList")
            if (savedMovieList != null) {
                movieList.addAll(savedMovieList)
            }

            tvRecyclerViewState = savedInstanceState.getParcelable("tvRecyclerViewState")
            val savedTvList = savedInstanceState.getParcelableArrayList<Tvlist>("tvList")
            if (savedTvList != null) {
                tvList.addAll(savedTvList)
            }
        }

        Movieadapter = MovieAdapter(movieList, this)
        MovierecyclerView.adapter = Movieadapter
        Tvadapter = TvAdapter(tvList, this)
        TvrecyclerView.adapter = Tvadapter

        // 리사이클러뷰 상태 복원
        movieRecyclerViewState?.let { MovierecyclerView.layoutManager?.onRestoreInstanceState(it) }
        tvRecyclerViewState?.let { TvrecyclerView.layoutManager?.onRestoreInstanceState(it) }

        // Results_display 액티비티에서 전달된 데이터 가져오기
        val intent = intent
        val movieTitles = intent.getStringArrayListExtra("titles")
        val movieOverviews = intent.getStringArrayListExtra("overviews")
        val moviePosterpaths = intent.getStringArrayListExtra("posterpaths")
        val tvTitles = intent.getStringArrayListExtra("titles2")
        val tvOverviews = intent.getStringArrayListExtra("overviews2")
        val tvPosterpaths = intent.getStringArrayListExtra("posterpaths2")



        Log.d("MyTag", "전달된 영화 데이터 내용:$movieTitles") // 결과 정상
        Log.d("MyTag", "전달된 드라마 데이터 내용:$tvTitles")

        // 리사이클러뷰 요소 리스트 생성
        if (movieTitles != null && movieOverviews != null && moviePosterpaths != null) {
            for (i in movieTitles.indices) {
                // 리스트 요소 추가
                movieList.add(Movielist(movieTitles[i], movieOverviews[i], moviePosterpaths[i]))
            }
        }
        if (tvTitles != null && tvOverviews != null && tvPosterpaths != null) {
            for (i in tvTitles.indices) {
                // 리스트 요소 추가
                tvList.add(Tvlist(tvTitles[i], tvOverviews[i], tvPosterpaths[i]))
            }
        }

        Log.d("MyTag", "전달된 데이터 사이즈:"+ movieTitles?.size.toString()) // 결과 정상


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



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 리사이클러뷰 상태 저장
        val movieRecyclerViewState = MovierecyclerView.layoutManager?.onSaveInstanceState()
        val tvRecyclerViewState = TvrecyclerView.layoutManager?.onSaveInstanceState()

        outState.putParcelable("movieLayoutManagerState", movieRecyclerViewState)
        outState.putParcelable("tvLayoutManagerState", tvRecyclerViewState)

        // 영화 목록 및 TV 목록 데이터 저장
        outState.putParcelableArrayList("movieList", movieList)
        outState.putParcelableArrayList("tvList", tvList)
    }


    // 클릭된 아이템의 정보를 받아 상세화면으로 전환하는 메소드
    override fun onItemClick(movie: Movielist) {
        val intent = Intent(this, MovieDetail::class.java).apply {
            putExtra("movie", movie) // 클릭된 아이템의 정보를 Intent에 추가하여 전달
        }
        movieRecyclerViewState = MovierecyclerView.layoutManager?.onSaveInstanceState()
        startActivity(intent) // 상세화면 액티비티로 전환
    }
    override fun onItemClick(tv: Tvlist) {
        val intent = Intent(this, TvDetail::class.java).apply {
            putExtra("tv", tv) // 클릭된 아이템의 정보를 Intent에 추가하여 전달
        }
        tvRecyclerViewState = TvrecyclerView.layoutManager?.onSaveInstanceState()
        startActivity(intent) // 상세화면 액티비티로 전환
    }

    override fun onResume() {
        super.onResume()
        // 화면 전환 후 다시 돌아왔을 때, 리사이클러뷰 상태 복원
        movieRecyclerViewState?.let { MovierecyclerView.layoutManager?.onRestoreInstanceState(it) }
        tvRecyclerViewState?.let { TvrecyclerView.layoutManager?.onRestoreInstanceState(it) }

    }

}