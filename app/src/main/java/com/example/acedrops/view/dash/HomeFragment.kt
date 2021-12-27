package com.example.acedrops.view.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.acedrops.R
import com.example.acedrops.repository.Datastore
import com.example.acedrops.repository.Datastore.Companion.EMAIL_KEY
import com.example.acedrops.repository.Datastore.Companion.NAME_KEY
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val datastore = activity?.let { Datastore(it) }
        val name = view.findViewById<TextView>(R.id.textview_name)

        lifecycleScope.launch {
            if (datastore != null) {
                name.text = datastore.getUserDetails(NAME_KEY)
                name.append("\n${datastore.getUserDetails(EMAIL_KEY)}")
            }
        }
        return view
    }

}