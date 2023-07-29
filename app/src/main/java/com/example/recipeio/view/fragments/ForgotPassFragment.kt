package com.example.recipeio.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentForgotPassBinding
import com.example.recipeio.presenter.ForgotPassPresenterImpl
import com.example.recipeio.presenter.ForgotPassView
import kotlinx.coroutines.launch


class ForgotPassFragment : Fragment(), ForgotPassView {
    private lateinit var binding: FragmentForgotPassBinding
    private val presenter = ForgotPassPresenterImpl()
    lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPassBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attach(this)
        resetPass()
        getEmail()

    }

    private fun resetPass(){
        binding.apply {
            btrecoverpass.setOnClickListener {
                val email = edEmailRecover.text.toString()
                lifecycleScope.launch {
                    presenter.forgotPass(email)
                }

            }
        }
    }

    override fun goBack() {
        findNavController().navigate(R.id.action_forgotPassFragment_to_loginFragment)
    }

    override fun message(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


    private fun getEmail(){
        val args = this.arguments
        val email = args?.getString("email_forgot_pass")
        binding.edEmailRecover.setText(email.toString())
    }

}