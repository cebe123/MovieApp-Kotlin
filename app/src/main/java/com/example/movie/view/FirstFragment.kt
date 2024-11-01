package com.example.movie.view

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Buton tıklanınca API'den veri çek ve veritabanına kaydet
        binding.button.setOnClickListener {
            viewModel.onButtonClick()
        }

        // Veritabanındaki filmler güncellendiğinde UI'ı güncelle
        viewModel.moviesFromDb.observe(viewLifecycleOwner) { movies ->
            val titles = movies.joinToString("\n") { it.name }
            binding.listText.text = titles.ifEmpty { "No data available" }
        }

        // Hata durumunda hata mesajını göster
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.listText.text = "An error occurred: $it"
            }
        }
    }
}
