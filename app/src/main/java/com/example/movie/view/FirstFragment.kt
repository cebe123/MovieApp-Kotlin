package com.example.movie.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movie.databinding.FragmentFirstBinding
import com.example.movie.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A Fragment that displays a list of movie titles fetched from the MainViewModel.
 * This Fragment uses Data Binding and observes LiveData for UI updates.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    /**
     * The ViewModel instance associated with this Fragment.
     * Injected by Hilt using the 'by viewModels()' delegate.
     */
    private val viewModel: MainViewModel by viewModels()

    /**
     * The binding object for the Fragment's layout.
     * Initialized in onCreateView.
     */
    private lateinit var binding: FragmentFirstBinding

    /**
     * Creates and returns the view hierarchy of the Fragment.
     * Inflates the layout using Data Binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }



    /**
     * Called after the view hierarchy is created.
     * Sets up LiveData observers to update the UI with movie titles or error messages.
     */

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            viewModel.onButtonClick()
        }

        // Observe the 'posts' LiveData from the ViewModel
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            try {
                // Extract movie titles and join them into a string
                val titles = posts?.results?.joinToString("\n") { it.title }
                // Update the TextView with the titles or "No data available"
                binding.listText.text = titles ?: "No data available"
            } catch (e: Exception) {
                // Handle any exceptions during data processing
                binding.listText.text = "Error loading data"
            }
        }

        // Observe the 'error' LiveData from the ViewModel
        viewModel.error.observe(viewLifecycleOwner) { error ->
            // Update the TextView with the error message if present
            error?.let {
                binding.listText.text = "An error occurred: $it"
            }
        }
    }
}