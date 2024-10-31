package com.example.movie.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Posts
import com.example.movie.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for fetching and managing movie posts.
 * This ViewModel uses Hilt for dependency injection and coroutines for asynchronous operations.
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    /**
     * Private MutableLiveData to hold the movie posts.
     * This is exposed as LiveData for observation by the UI.
     */
    private val _posts = MutableLiveData<Posts?>()
    val posts: LiveData<Posts?> = _posts

    fun onButtonClick() {
        fetchPosts()
    }


    /**
     * Private MutableLiveData to hold error messages.
     * This is exposed as LiveData for observation by the UI.
     */
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Fetches movie posts from the repository using a coroutine.
     * Updates the _posts LiveData with the fetched posts or the _error LiveData with an error message.
     */


    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                // Log the start of the fetch operation
                Log.d(MainViewModel::class.java.simpleName, "Fetching posts...")
                // Fetch posts from the repository and update the _posts LiveData
                _posts.value = repository.getPosts()
                // Log the successful completion of the fetch operation
                Log.d(MainViewModel::class.java.simpleName, "Posts fetched successfully")
            } catch (e: Exception) {
                // Update the _error LiveData with the error message
                _error.postValue("An error occurred: ${e.message}")
                // Log the error that occurred during the fetch operation
                Log.d(MainViewModel::class.java.simpleName, "Error fetching posts: ${e.message}")
            }
        }

    }
}