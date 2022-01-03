package com.example.acedrops.view.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.SplashScreen
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        val view = binding.root

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/acedrop-seller-1640952554752.appspot.com/o/Aadhar%2Fimage%3A102048?alt=media&token=71c57e57-df57-4ff4-8a9a-81a676105c40", ScaleTypes.FIT))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/acedrop-seller-1640952554752.appspot.com/o/Aadhar%2Fimage%3A102048?alt=media&token=71c57e57-df57-4ff4-8a9a-81a676105c40", ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList)
        return view
    }

}