package com.example.myapplication_test

import ChatCompletionResponse
import ChatMessage
import ChatRequest
import Movie
import TmdbSearchResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.search.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.util.concurrent.CountDownLatch

class Search  : AppCompatActivity() {

    // Results_display 파일로 넘어가는 배열값
    companion object {
        val titles = mutableListOf<String>()
        val overviews = mutableListOf<String>()
        val poster_paths = mutableListOf<String>()
    }

    // GPT-3.5 Turbo API 요청에 필요한 데이터 모델
    data class GPTRequest(
        val model: String,
        val prompt: String,
        val maxTokens: Int
    )

    // TMDB API 요청
    fun communicateWithTM(query: String, onComplete: () -> Unit) {


        val includeAdult = false
        val language = "ko-KR" //"en-US"
        val page = 1
        val apiKey = "0e209ce1c01b25eda1f1d3b69327e72a"

        RetrofitClient.instance_2.searchMovies(query, includeAdult, language, page, apiKey).enqueue(object : Callback<TmdbSearchResponse> {
            override fun onResponse(call: Call<TmdbSearchResponse>, response: Response<TmdbSearchResponse>) {
                if (response.isSuccessful) {
                    val result = response.body().toString()
                    // gpt 응답 값 전체 출력
                    ////Log.d("MyTag", result)


                    // 응답문자열 파싱 -> 데이터추출
                    fun parseTmdbSearchResponse(responseString: String): TmdbSearchResponse {
                        val regex = Regex("""TmdbSearchResponse\(page=(\d+), results=\[(.*?)\], total_pages=(\d+), total_results=(\d+)\)""")

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
                    // Movie 객체를 생성하는 함수

                    // 응답 문자열을 파싱하여 TmdbSearchResponse 객체 생성
                    val tmdbSearchResponse: TmdbSearchResponse = parseTmdbSearchResponse(result)
                    tmdbSearchResponse.let { handleTmdbResponse(it) }
                    Log.d("MyTag", titles.toString())


                } else {
                    // 실패 처리
                    val errorMessage = response.errorBody()?.string()
                    Log.d("MyTag", "Failed: $errorMessage")
                }


                //Log.d("titleMyTag", titles.toString())

            }

            override fun onFailure(call: Call<TmdbSearchResponse>, t: Throwable) {
                // 실패 처리
                Log.e("MyTag", "Error: ${t.message}", t)
            }



        })

        onComplete()

    }
    fun handleTmdbResponse(response: TmdbSearchResponse) {
        for (movie in response.results) {
            titles.add(movie.title)
            overviews.add(movie.overview)
            poster_paths.add(movie.poster_path.toString())
            // **여기에서 필요한 작업을 수행**
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

        // API와 통신하는 함수
        fun communicateWithAPI(requestData: GPTRequest, onComplete: () -> Unit) {
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

                        //val movieTitlesString = movieTitles.joinToString(", ")
                        //Log.d("MyTag", movieTitlesString)


                        // **tmdb api 요청 코드**
                        //for (query in movieTitles) {
                        //    communicateWithTM(query)
                        //}

                        // **tmdb api 요청 코드**
                        val countDownLatch = CountDownLatch(movieTitles.size) // 비동기 작업의 완료를 기다리기 위한 CountDownLatch 생성

                        for (query in movieTitles) {
                            communicateWithTM(query) {
                                countDownLatch.countDown() // 각 요청이 완료될 때마다 CountDownLatch를 감소시킵니다.
                            }
                        }

                        // 모든 요청이 완료될 때까지 대기합니다.
                        countDownLatch.await()

                        // 모든 요청이 완료되면 onComplete 콜백을 호출하여 액티비티를 시작합니다.
                        onComplete()


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

/*
            // API와 통신
            val mainLogic = MainLogic(questionText)
            mainLogic.communicateWithAPI(requestData)
            startActivity(Intent(this, Results_display::class.java))
*/

            // API와 통신
            val mainLogic = MainLogic(questionText)
            mainLogic.communicateWithAPI(requestData) {
                startActivity(Intent(this, Results_display::class.java))
            }

        }

    }

}