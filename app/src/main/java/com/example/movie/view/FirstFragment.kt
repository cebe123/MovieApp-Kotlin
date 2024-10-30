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

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            try {
                val titles = posts?.results?.joinToString("\n") { it.title }
                binding.listText.text = titles ?: "No data available"
            } catch (e: Exception) {
                binding.listText.text = "Error loading data"
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { it ->
                binding.listText.text = "An error occurred: $it"
            }
        }
    }
}
