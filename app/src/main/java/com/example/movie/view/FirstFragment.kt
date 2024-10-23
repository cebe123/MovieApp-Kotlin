package com.example.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movie.databinding.FragmentFirstBinding
import com.example.movie.viewModel.MainViewModel

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            val titles = posts.results.joinToString("\n") { it.title }
            binding.listText.text = titles
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
                val  errorString = "An error occured: $error"
                binding.listText.text = errorString
           //Hata ortadan kalkınca veri güncelleme durumu eklenecek
        }

    }
}