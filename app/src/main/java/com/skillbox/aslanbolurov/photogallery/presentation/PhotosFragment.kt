package com.skillbox.aslanbolurov.photogallery.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.skillbox.aslanbolurov.photogallery.R
import com.skillbox.aslanbolurov.photogallery.data.App
import com.skillbox.aslanbolurov.photogallery.data.PhotoDao
import com.skillbox.aslanbolurov.photogallery.databinding.FragmentPhotosBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class PhotosFragment : Fragment() {

    companion object {
        fun newInstance() = PhotosFragment()
    }

    private val viewModel: PhotosViewModel by viewModels{
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val photoDao: PhotoDao =
                    (requireActivity().application as App).db.photoDao()
                return PhotosViewModel(photoDao) as T
            }
        }
    }


    private var _binding:FragmentPhotosBinding?=null
    val binding get() = _binding!!

    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentPhotosBinding.inflate(inflater,container,false)

        binding.buttonTakePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_PhotosList_to_PhotoTaking)
        }
        binding.buttonGoogleMaps.setOnClickListener {
            findNavController().navigate(R.id.action_PhotosList_to_GoogleMapsFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter=PhotosListAdapter()

        recyclerView=binding.recyclerView
        recyclerView.adapter=listAdapter

        lifecycleScope.launchWhenStarted {
            viewModel.flowPhotosList.onEach {
                listAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.refreshloadPhotos()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }



}