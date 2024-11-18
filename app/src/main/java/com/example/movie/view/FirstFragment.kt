package com.example.movie.view

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movie.databinding.FragmentFirstBinding
import com.example.movie.di.WeatherService
import com.example.movie.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentFirstBinding

    private var localService: WeatherService? = null
    private var isBound = false

    //service connection and update UI
    private val connection = object : ServiceConnection {
        @SuppressLint("SetTextI18n")
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // Binder'ı al ve LocalService örneğine eriş
            val binder = service as WeatherService.LocalBinder
            localService = binder.getService()
            isBound = true

            // Servisten veri gelirse, UI'yi güncellemeye başla
            localService?.weatherData?.observe(viewLifecycleOwner) { (city, temp) ->
                binding.textViewTemperature.text = "${temp}°C"
                binding.textViewCity.text = city
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            localService = null
            isBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Servisi bağla
        Intent(requireContext(), WeatherService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.moviesFromDb.observe(viewLifecycleOwner) { movies ->
            val titles = movies.joinToString("\n") { it.name }
            binding.listText.text = titles.ifEmpty { "No data available" }
        }

        binding.updateButton.setOnClickListener {
            viewModel.onUpdateButtonClick()
        }

        binding.routeButton.setOnClickListener {
            viewModel.setIntent()
            startActivity(viewModel.sendIntent)
        }
    }
}
