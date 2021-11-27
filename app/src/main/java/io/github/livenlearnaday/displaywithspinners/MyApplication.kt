package io.github.livenlearnaday.displaywithspinners

import android.app.Application
import android.util.Log

import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree


//for Timber logging and others
@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : DebugTree() {
                //Add the line number to the tag
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ": " + element.lineNumber
                }
            })
        } else {
            //Release mode
            Timber.plant(ReleaseLogTree())
        }

    }

    private class ReleaseLogTree : Timber.Tree() {
        override fun log(
            priority: Int, tag: String?, message: String,
            throwable: Throwable?
        ) {
            if (priority == Log.DEBUG || priority == Log.VERBOSE || priority == Log.INFO) {
                return
            }
            if (priority == Log.ERROR) {
                if (throwable == null) {
                    Timber.e(message)
                } else {
                    Timber.e(throwable, message)
                }
            }
        }
    }
}