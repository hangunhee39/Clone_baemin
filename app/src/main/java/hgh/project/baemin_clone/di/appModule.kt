package hgh.project.baemin_clone.di

import hgh.project.baemin_clone.screen.main.home.HomeViewModel
import hgh.project.baemin_clone.screen.main.my.MyViewModel
import hgh.project.baemin_clone.util.provider.DefaultResourcesProvider
import hgh.project.baemin_clone.util.provider.ResourceProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single { provideRetrofit(get(), get()) }

    single<ResourceProvider> {DefaultResourcesProvider(androidApplication())}

    single { Dispatchers.IO }
    single { Dispatchers.Main }
}