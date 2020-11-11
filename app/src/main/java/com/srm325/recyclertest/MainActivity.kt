package com.aliakberaakash.cutiehacksproject2020

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.aliakberaakash.cutiehacksproject2020.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URI


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navHostFragment : NavHostFragment
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpDataBinding()
        setUpNavigation()
        val info: Intent = getIntent()
        val email:String? = info.getStringExtra("Email")
        val userName:String? = info.getStringExtra("Name")
        val image: String? = info.getStringExtra("profile")
        val imageUri = URI.create(image)
        val data = hashMapOf(
                "name" to userName,
                "profile" to imageUri
        )
        db.collection("users").document(email.toString())
                .set(data)
                .addOnSuccessListener { documentReference ->
                    Log.d("success","DocumentSnapshot written with ID: ${email.toString()}")
                }
                .addOnFailureListener { e ->
                    Log.w("Error adding document", e)
                }

    }

    private fun setUpDataBinding(){
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
    }

    private fun setUpNavigation(){
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(this,navController)
        NavigationUI.setupWithNavController(bottom_nav, navController)

    }


}