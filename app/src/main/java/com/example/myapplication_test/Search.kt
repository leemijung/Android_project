package com.example.myapplication_test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mood.*
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.search.back_button

class Search  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        back_button.setOnClickListener {
            startActivity(Intent(applicationContext, Selection::class.java))
            finish()
        }

    }
}