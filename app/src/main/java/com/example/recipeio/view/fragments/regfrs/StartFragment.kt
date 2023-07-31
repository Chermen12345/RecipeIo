package com.example.recipeio.view.fragments.regfrs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentStartBinding
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.view.activities.HomeActivity


class StartFragment : Fragment() {
    //TODO variable viewbinding
    private lateinit var binding: FragmentStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO inits
        //init binding
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO launch functions

        //fun to go to login fragment to register or signIn the account
        startLogin()

        userNotNull()

    }
    //TODO all functions
    //from start fragment user goes to login fragment
    private fun startLogin(){
        binding.btStartLogin.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
    }
    private fun userNotNull(){
        if (AUTH.currentUser!=null){
            val intent = Intent(context,HomeActivity::class.java)
            startActivity(intent)
        }
    }


}