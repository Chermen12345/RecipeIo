package com.example.recipeio.view.fragments.homefrs

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeio.R
import com.example.recipeio.databinding.FragmentNotificationBinding
import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts.AUTH
import com.example.recipeio.utils.Consts.REF
import com.example.recipeio.view.activities.HomeActivity
import com.example.recipeio.view.activities.HomeActivity.Companion.CHANNEL_ID
import com.example.recipeio.view.adapters.NotAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class NotificationFragment : Fragment() , NotAdapter.OnClick{

    private lateinit var binding: FragmentNotificationBinding

    private val listOfNots = arrayListOf<User>()
    private lateinit var adapter: NotAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNotifications()




    }
    private fun getNotifications(){
        REF.child("users/${AUTH.currentUser!!.uid}/notifications").addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfNots.clear()


                    for (ds in snapshot.children){
                        val value = ds.getValue(User::class.java)
                        value?.let {user ->
                            listOfNots.add(0,value)
                        }

                    }


                    adapter = NotAdapter(listOfNots,this@NotificationFragment)
                    binding.apply {
                        rcnotifications.adapter = adapter
                        rcnotifications.layoutManager = LinearLayoutManager(context)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    override fun onNotClick(user: User) {

    }

}