package com.example.flutterhub_jetpackcompose

import android.app.Application
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()


        Hawk.init(this).build()


    }

}