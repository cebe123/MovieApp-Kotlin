package com.example.movie.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Posts
import com.example.movie.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository : Repository

    private val _posts = MutableLiveData<Posts>()
    val posts: LiveData<Posts>
        get() = _posts  //getter method for posts,It's automatically called when you access the posts property.


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        fetchPosts()
    }


    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                _posts.value = repository.getPosts()
            } catch (e: Exception) {
                _error.value = "An error occurred: ${e.message}"
            }
        }
    }

}