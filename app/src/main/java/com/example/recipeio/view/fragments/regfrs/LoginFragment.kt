package com.example.recipeio.view.fragments.regfrs

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
import com.example.recipeio.presenter.LoginView

import com.example.recipeio.view.activities.HomeActivity

import kotlinx.coroutines.launch


class LoginFragment : Fragment(), LoginView {
    //binding variable
    private lateinit var binding: FragmentLoginBinding
    //presenter init
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
        //navigation
        goToSignUp()
        goToForgotPassFr()


        //login
        presenter.attach(this)
        login()
    }

    //TODO navigation funs

    //user doesnt have an account
    private fun goToSignUp(){
        binding.apply {
            tvGoToSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }
    //user logged in successfully
    override fun goToHomeActivity() {
        val intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
    }
    //user forgot password and goes to forgot pass fragment
    private fun goToForgotPassFr(){
        binding.tvForgotPass.setOnClickListener {
            val bundle = Bundle()
            val email = binding.edEmailLogin.text.toString()
            bundle.putString("email_forgot_pass",email)
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassFragment,bundle)
        }
    }





    //TODO main function login
    private fun login(){
        binding.apply {
            btLogin.setOnClickListener {
                val email = edEmailLogin.text.toString()
                val pass = edPassLogin.text.toString()
                lifecycleScope.launch { presenter.login(email,pass) }
            }
        }
    }



    //TODO utils
    //show progress bar until the login is successful
    override fun showProgress() {
        binding.prbar.visibility = View.VISIBLE
    }

    //toast message
    override fun message(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }
}