package com.example.movie.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Posts
import com.example.movie.model.RetrofitInstance
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {

    //This code defines a read-only property named posts that exposes a LiveData object containing a list of Posts.

    private val _posts = MutableLiveData<Posts>()
    val posts: LiveData<Posts>
        get() = _posts  //getter method for posts,It's automatically called when you access the posts property.


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        getPosts()
    }


     fun getPosts() {
        viewModelScope.launch {

            try {
                val response = RetrofitInstance.api.fetchMovies()
                if (response.isSuccessful) {
                    response.body()?.let { post ->
                        _posts.value = post
                    }
                } else {
                    when (response.code()) {
                        400 -> _error.value = "Bad Request"
                        401 -> _error.value = "Unauthorized"
                        403 -> _error.value = "Forbidden"
                        404 -> _error.value = "Not Found"
                    }
                }
            } catch (e: IOException) {
                _error.value = "Network Error: ${e.message}"

            } catch (e: retrofit2.HttpException) {
                _error.value = "HTTP Error: ${e.message}"

            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"

            }
        }
    }
}