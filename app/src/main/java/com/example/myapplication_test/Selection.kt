package com.example.myapplication_test

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.selection.*
import kotlinx.android.synthetic.main.short_description.*

class Selection  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selection)

        //오늘 하루 어떠셨나요? 말풍선 작동
        val fadeInAnimator = ObjectAnimator.ofFloat(frist_button, View.ALPHA, 0f, 1f)
        fadeInAnimator.duration = 1000 // 애니메이션 지속 시간 (밀리초)
        fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()

        val fadeOutAnimator = ObjectAnimator.ofFloat(frist_button, View.ALPHA, 1f, 0f)
        fadeOutAnimator.duration = 1000
        fadeOutAnimator.interpolator = AccelerateDecelerateInterpolator()

        fadeOutAnimator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                fadeInAnimator.start()
            }
        })

        fadeOutAnimator.start()


        //추천 받고 싶은 방법을 선택해주세요 말풍선 작동
        val fadeInAnimator2 = ObjectAnimator.ofFloat(frist_button2, View.ALPHA, 0f, 1f)
        fadeInAnimator2.duration = 1000 // 애니메이션 지속 시간 (밀리초)
        fadeInAnimator2.interpolator = AccelerateDecelerateInterpolator()

        val fadeOutAnimator2 = ObjectAnimator.ofFloat(frist_button2, View.ALPHA, 1f, 0f)
        fadeOutAnimator2.duration = 1000
        fadeOutAnimator2.interpolator = AccelerateDecelerateInterpolator()

        fadeOutAnimator2.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                fadeInAnimator2.start()
            }
        })

        fadeOutAnimator2.start()



        //기분 or 검색 택1 버튼
        mood_button.setOnClickListener {
            startActivity(Intent(applicationContext, Mood::class.java))
            finish()
        }
        search_button.setOnClickListener {
            startActivity(Intent(applicationContext, Search::class.java))
            finish()
        }
    }
}