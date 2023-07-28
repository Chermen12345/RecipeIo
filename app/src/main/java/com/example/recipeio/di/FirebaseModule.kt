package com.example.recipeio.di

import com.example.recipeio.consts.FirebaseConsts.AUTH
import com.example.recipeio.consts.FirebaseConsts.REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val module = module {
    single<FirebaseAuth>{
        AUTH
    }
    single<DatabaseReference> {
        REF
    }
}