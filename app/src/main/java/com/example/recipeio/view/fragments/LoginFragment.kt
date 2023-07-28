package com.example.recipeio.view.fragments

import android.content.Context
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
import com.example.recipeio.databinding.FragmentLoginBinding
import com.example.recipeio.presenter.LoginPresenterImpl

import com.example.recipeio.view.activities.HomeActivity
import com.example.recipeio.view.interfaces.LoginView
import kotlinx.coroutines.launch


class LoginFragment : Fragment(),LoginView {
    private lateinit var binding: FragmentLoginBinding
    private val presenter = LoginPresenterImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToSignUp()



        presenter.attach(this)
        login()
    }
    private fun goToSignUp(){
        binding.apply {
            tvGoToSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }








    private fun login(){
        binding.apply {
            btLogin.setOnClickListener {
                val email = edEmailLogin.text.toString()
                val pass = edPassLogin.text.toString()
                lifecycleScope.launch { presenter.login(email,pass) }
            }
        }
    }

    override fun goToHomeActivity() {
        val intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
    }

    override fun message(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }
}