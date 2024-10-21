package com.example.movie

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        mainViewModel.posts.observe(this) { posts ->
            textView.text = posts.results[0].title
        }

        mainViewModel.error.observe(this) { errorMessage ->
            textView.text = errorMessage
        }
    }
}