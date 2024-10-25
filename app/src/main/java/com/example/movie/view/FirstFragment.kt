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
import javax.inject.Inject

/**
 * Günün trend olan filmlerini listeleyen bir Fragment.
 *
 * Bu fragment, [MainViewModel] kullanarak TMDB API'sinden film verilerini alır ve
 * bunları ekranda görüntüler. Ayrıca olası hataları da ele alır.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    @Inject
    lateinit var viewModel : MainViewModel

    private lateinit var binding: FragmentFirstBinding
    //private val viewModel: MainViewModel by viewModels() // Inject ViewModel using by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * [MainViewModel] içindeki `posts` LiveData'sını gözlemler.
         * Yeni bir veri geldiğinde, film başlıklarını alır ve `listText` TextView'ına ayarlar.
         */
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            val titles = posts.results.joinToString("\n") { it.title }
            binding.listText.text = titles
        }

        /**
         * [MainViewModel] içindeki `error` LiveData'sını gözlemler.
         * Bir hata oluştuğunda, hata mesajını `listText` TextView'ına ayarlar.
         */
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                val errorString = "An error occurred: $it"
                binding.listText.text = errorString
            }
            //TODO: Hata ortadan kalkınca veri güncelleme durumu eklenecek
        }
    }
}