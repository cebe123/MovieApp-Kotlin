package com.example.movie.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.movie.databinding.FragmentFirstBinding
import com.example.movie.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Clear the database initially within a coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.clearDatabase()
        }

        viewModel.fetchWeather()

        viewModel.weatherData.observe(viewLifecycleOwner) { (city, temp) ->
            binding.textViewTemperature.text = "$temp°C" // Update UI to show city and temperature
            binding.textViewCity.text = city
           // binding.imageButton = icon
        }

        // Set up button click listener
        binding.button.setOnClickListener {
            viewModel.onButtonClick()
        }

        // Observe movies from database and update UI
        viewModel.moviesFromDb.observe(viewLifecycleOwner) { movies ->
            val titles = movies.joinToString("\n") { it.name }
            binding.listText.text = titles.ifEmpty { "No data available" }
        }

        // Observe error messages and display them if any
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.listText.text = "An error occurred: $it"
            }
        }
    }
}
