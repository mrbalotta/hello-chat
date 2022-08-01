package br.com.bhavantis.chatroom.registration.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.bhavantis.chatroom.R
import br.com.bhavantis.chatroom.registration.controller.RegistrationViewModel

class RegistrationFragment : Fragment() {
    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var edit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getLiveData().observe(this) { onUserRegistered() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val registrationBtn = view.findViewById<Button>(R.id.registerBtn)
        edit = view.findViewById(R.id.name)
        registrationBtn.setOnClickListener {
            onRegistrate()
        }
    }

    private fun onRegistrate() {
        Log.d("ALE", "onRegistrate")
        val text = edit.text
        if(text.isNotEmpty()) {
            edit.text.clear()
            viewModel.registrate(text.toString())
        }
    }

    private fun onUserRegistered() {
        findNavController().navigate(R.id.registration_to_contacts)
    }
}