package hgh.project.baemin_clone

import android.app.Application
import android.content.Context

class BaeminCloneApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext =this
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