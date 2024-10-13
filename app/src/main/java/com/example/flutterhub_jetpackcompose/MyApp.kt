package com.example.flutterhub_jetpackcompose

import android.app.Application
import com.example.flutterhub_jetpackcompose.models.UserModel
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()


        Hawk.init(this).build()


    }

}