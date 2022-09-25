package com.example.koin

import android.app.Application
import com.example.domain.di.datasourceModule
import com.example.domain.di.networkModule
import com.example.domain.di.repoModule
import com.example.domain.di.useCaseModule
import com.example.koin.di.appModule
import com.example.koin.di.viewModelModule
import com.facebook.soloader.SoLoader
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        SoLoader.init(this, false)

//        if (BuildConfig.DEBUG) {
//            val client = AndroidFlipperClient.getInstance(this)
//            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//            client.start()
//        }
    }
}
/*
Testing needed for -

1. Datasource
2. Repository
3. UseCase
4. ViewModel
5. Modules
 */