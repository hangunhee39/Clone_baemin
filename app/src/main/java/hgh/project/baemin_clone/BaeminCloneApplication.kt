package hgh.project.baemin_clone

import android.app.Application
import android.content.Context
import hgh.project.baemin_clone.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaeminCloneApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext =this

        startKoin {
            androidContext(this@BaeminCloneApplication)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext =null
    }

    companion object {
        var appContext: Context? =null  //appContext 가 필요하다
            private set
    }
}