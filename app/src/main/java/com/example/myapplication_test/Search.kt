package com.example.myapplication_test

import ChatMessage
import ChatRequest
import Movie
import TmdbSearchResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.search.*
import kotlinx.coroutines.*

class Search  : AppCompatActivity() {

    // Results_display로 넘어가는 배열값
    // **리사이클러뷰에 요소 추가시 여기에 추가해야함**
    var titles = mutableListOf<String>()
    var overviews = mutableListOf<String>()
    var posterpaths = mutableListOf<String>()

    // GPT-3.5 Turbo API 요청에 필요한 데이터 모델
    data class GPTRequest(
        val model: String,
        val prompt: String,
        val maxTokens: Int
    )

    // TMDB API 요청 - 동기화된 코드
    suspend fun communicateWithTM(query: String): Unit? {
        val includeAdult = false
        val language = "ko-KR" // 한국어
        val page = 1
        val apiKey = "95dbc652cce0ae9d409c0e8feaf97d2b"// "0e209ce1c01b25eda1f1d3b69327e72a"


        return try {
            val response = RetrofitClient.instance_2.searchMovies(query, includeAdult, language, page, apiKey)
            if (response.isSuccessful) {
                val result = response.body().toString()
                // gpt 응답 값 전체 출력
                Log.d("MyTag", result)


                // 응답문자열 파싱 -> 데이터추출
                fun parseTmdbSearchResponse(responseString: String): TmdbSearchResponse {
                    val regex = Regex("""TmdbSearchResponse\(page=(\d+), results=\[(.*?)\], total_pages=(\d+), total_results=(\d+)\)""")

                    val matchResult = regex.find(responseString)
                        ?: throw IllegalArgumentException("Invalid response string format")

                    val (pageStr, resultsStr, totalPagesStr, totalResultsStr) = matchResult.destructured

                    val page = pageStr.toInt()
                    val totalPages = totalPagesStr.toInt()
                    val totalResults = totalResultsStr.toInt()

                    // tmdb 응답값 예시 )
                    // Movie(adult=false, backdrop_path=/h5pAEVma835u8xoE60kmLVopLct.jpg, genre_ids=[16, 10751, 14], id=16859, original_language=ja, original_title=魔女の宅急便, overview=A young witch, on her mandatory year of independent life, finds fitting into a new community difficult while she supports herself by running an air courier service., popularity=45.235, poster_path=/Aufa4YdZIv4AXpR9rznwVA5SEfd.jpg, release_date=1989-07-29, title=Kiki's Delivery Service, video=false, vote_average=7.818, vote_count=3771)

                    // 결과 목록 파싱
                    val movies = mutableListOf<Movie>()
                    val movieRegex = Regex("""overview=(.*?),.*?title=(.*?),""")

                    movieRegex.findAll(resultsStr).forEach { movieMatchResult ->
                        val (overview, title) = movieMatchResult.destructured
                        movies.add(Movie(false,
                            "/fxYazFVeOCHpHwuqGuiqcCTw162.jpg",
                            listOf(14, 16, 10751),
                            8392,
                            "ja",
                            "となりのトトロ",
                            overview,
                            51.043,
                            "/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                            "1988-04-16",
                            title,
                            false,
                            8.07,
                            7441))
                    }

                    return TmdbSearchResponse(page, movies, totalPages, totalResults)
                }

                // 응답 문자열을 파싱하여 TmdbSearchResponse 객체 생성
                val tmdbSearchResponse: TmdbSearchResponse = parseTmdbSearchResponse(result)
                tmdbSearchResponse.let { handleTmdbResponse(it) }  // 배열에 값 넣는 함수



            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MyTag", "Error communicating with TM: ${e.message}", e)
            null
        }


/* 이전코드(TMDB API 요청) - 쓰면 안됨

        RetrofitClient.instance_2.searchMovies(query, includeAdult, language, page, apiKey)
            .enqueue(object : Callback<TmdbSearchResponse> {
                override fun onResponse(
                    call: Call<TmdbSearchResponse>,
                    response: Response<TmdbSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body().toString()
                        // gpt 응답 값 전체 출력
                        Log.d("MyTag", result)


                        // 응답문자열 파싱 -> 데이터추출
                        fun parseTmdbSearchResponse(responseString: String): TmdbSearchResponse {
                            val regex =
                                Regex("""TmdbSearchResponse\(page=(\d+), results=\[(.*?)\], total_pages=(\d+), total_results=(\d+)\)""")

                            val matchResult = regex.find(responseString)
                                ?: throw IllegalArgumentException("Invalid response string format")

                            val (pageStr, resultsStr, totalPagesStr, totalResultsStr) = matchResult.destructured

                            val page = pageStr.toInt()
                            val totalPages = totalPagesStr.toInt()
                            val totalResults = totalResultsStr.toInt()

                            //Movie(adult=false, backdrop_path=/h5pAEVma835u8xoE60kmLVopLct.jpg, genre_ids=[16, 10751, 14], id=16859, original_language=ja, original_title=魔女の宅急便, overview=A young witch, on her mandatory year of independent life, finds fitting into a new community difficult while she supports herself by running an air courier service., popularity=45.235, poster_path=/Aufa4YdZIv4AXpR9rznwVA5SEfd.jpg, release_date=1989-07-29, title=Kiki's Delivery Service, video=false, vote_average=7.818, vote_count=3771)

                            // 결과 목록 파싱
                            val movies = mutableListOf<Movie>()
                            val movieRegex = Regex("""overview=(.*?),.*?title=(.*?),""")


                            movieRegex.findAll(resultsStr).forEach { movieMatchResult ->
                                val (overview, title) = movieMatchResult.destructured
                                movies.add(
                                    Movie(
                                        false,
                                        "/fxYazFVeOCHpHwuqGuiqcCTw162.jpg",
                                        listOf(14, 16, 10751),
                                        8392,
                                        "ja",
                                        "となりのトトロ",
                                        overview,
                                        51.043,
                                        "/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                                        "1988-04-16",
                                        title,
                                        false,
                                        8.07,
                                        7441
                                    )
                                )
                            }

                            return TmdbSearchResponse(page, movies, totalPages, totalResults)
                        }
                        // Movie 객체를 생성하는 함수

                        // 응답 문자열을 파싱하여 TmdbSearchResponse 객체 생성
                        val tmdbSearchResponse: TmdbSearchResponse = parseTmdbSearchResponse(result)
                        tmdbSearchResponse.let { handleTmdbResponse(it) }
                        //Log.d("MyTag", titles.toString())


                    } else {
                        // 실패 처리
                        val errorMessage = response.errorBody()?.string()
                        Log.d("MyTag", "Failed: $errorMessage")
                    }

                }

                override fun onFailure(call: Call<TmdbSearchResponse>, t: Throwable) {
                    // 실패 처리
                    Log.e("MyTag", "Error: ${t.message}", t)
                }

            })

 */

    }

    // 배열에 값 넣기
    fun handleTmdbResponse(response: TmdbSearchResponse) {
        response.let {
            for (movie in it.results) {
                titles.add(movie.title)
                overviews.add(movie.overview)
                posterpaths.add(movie.poster_path ?: "")
                // **더 추가 필요**

            }
        }
    }



    inner class MainLogic(question: String) {

        val messages = listOf(
            ChatMessage(role = "user", content = question)
        )

        val request = ChatRequest(
            messages = messages,
            model = "gpt-3.5-turbo"
        )


        // API와 통신하는 함수 - 동기화된 코드
        suspend fun communicateWithAPI(requestData: GPTRequest) {
            try {
                val response = RetrofitClient.instance.getCompletion(request)
                if (response.isSuccessful) {
                    val result = response.body()
                    // gpt 응답 값 전체 출력
                    Log.d("MyTag", result.toString())

                    val movieTitles = result?.choices!![0].message.content.split("\n")
                        .map { it.substringAfter(". ").trim() } // 각 줄에서 번호와 영화 제목을 분리하고 영화 제목만 추출
                        .toTypedArray()

                    // 코루틴 이용해서 동기화
                    val deferredList = movieTitles.map { query ->
                        lifecycleScope.async {
                            communicateWithTM(query)
                        }
                    }

                    // 모든 communicateWithTM 호출이 완료될 때까지 대기
                    deferredList.forEach { deferred ->
                        deferred.await()
                    }

                    // 모든 호출이 완료된 후에 로그를 출력
                    Log.d("MyTag", "타이틀 사이즈: "+titles.size) // 결과 정상
                    Log.d("MyTag", "타이틀 내용: $titles") // 결과 정상


                    if (titles.size >= 5) { // **숫자 조절필요** 현재 gpt에 10개를 요청했으니까 그것보단 적어야 함
                        val intent = Intent(this@Search, Results_display::class.java)
                        intent.putStringArrayListExtra("titles", ArrayList(titles))
                        intent.putStringArrayListExtra("overviews", ArrayList(overviews))
                        intent.putStringArrayListExtra("posterpaths", ArrayList(posterpaths))
                        startActivity(intent) // Results_display 화면으로 넘어감
                        finish()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.d("MyTag", "Failed: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("MyTag", "Error communicating with API: ${e.message}", e)
            }
        }


        /* 이전코드(GPT API 요청) - 쓰면 안됨
        // API와 통신하는 함수
        fun communicateWithAPI(requestData: GPTRequest) {
            // GPT-3.5 Turbo API 요청
            RetrofitClient.instance.getCompletion(request).enqueue(object : Callback<ChatCompletionResponse> {
                override fun onResponse(call: Call<ChatCompletionResponse>, response: Response<ChatCompletionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        // gpt 응답 값 전체 출력
                        Log.d("MyTag", result.toString())

                        val movieTitles = result?.choices!![0].message.content.split("\n")
                            .map { it.substringAfter(". ").trim() } // 각 줄에서 번호와 영화 제목을 분리하고 영화 제목만 추출
                            .toTypedArray()


                        // **tmdb api 요청 코드**
                        //for (query in movieTitles) {
                        //    communicateWithTM(query)
                        //}

                        //val countDownLatch = CountDownLatch(movieTitles.size) // 비동기 작업
                        GlobalScope.launch {
                            val deferredList = movieTitles.map { query ->
                                GlobalScope.async {
                                    communicateWithTM(query)
                                }
                            }

                            // 모든 communicateWithTM 호출이 완료될 때까지 대기
                            deferredList.forEach { deferred ->
                                deferred.await()
                            }

                            // 모든 호출이 완료된 후에 로그를 출력합니다.
                            Log.d("MyTag", "타이틀 사이즈: "+titles.size)

                            // 이후에 화면 전환 등의 로직을 수행합니다.
                        }

                        /*
                        // **tmdb api 요청 코드**
                        GlobalScope.launch {
                            movieTitles.forEach { query ->
                                communicateWithTM(query)
                            }
                        }
*/
                        // 모든 요청이 완료될 때까지 대기
                        //countDownLatch.await()

                        Log.d("MyTag", "타이틀 사이즈: "+titles.size) //왜 0이 나옴..?
                        // 화면 전환
                        if (titles.size >= 3) {
                            val intent = Intent(this@Search, Results_display::class.java)
                            intent.putStringArrayListExtra("titles", ArrayList(titles))
                            intent.putStringArrayListExtra("overviews", ArrayList(overviews))
                            intent.putStringArrayListExtra("posterpaths", ArrayList(posterpaths))
                            Log.d("MyTag", "전달할 데이터 내용:$titles")

                            startActivity(intent)
                            finish()
                        }


                    } else {
                        // 실패 처리
                        val errorMessage = response.errorBody()?.string()
                        Log.d("MyTag", "Failed: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<ChatCompletionResponse>, t: Throwable) {
                    // 실패 처리
                    Log.e("MyTag", "Error: ${t.message}", t)
                }
            })
        }

         */

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        back_button.setOnClickListener {
            startActivity(Intent(applicationContext, Selection::class.java))
            finish()
        }


        // GPT-3.5 Turbo API 요청 데이터 설정
        val requestData = GPTRequest(
            model = "gpt-3.5-turbo-0613",
            prompt = "Once upon a time",
            maxTokens = 50
        )


        //메인 기능 실행
        go_button.setOnClickListener {

            // question: 사용자가 직접 입력한 문장
            val question = et_msg?.text.toString()
            et_msg.setText("")
            val appendedText = "아무런 코멘트 없이 오로지 영화제목만 10개 말해줘."
            val questionText = "$question $appendedText"

            Log.d("MyTag", questionText)


            // API와 통신
            val mainLogic = MainLogic(questionText)

            GlobalScope.launch {
                mainLogic.communicateWithAPI(requestData)
            }

        }

    }

}