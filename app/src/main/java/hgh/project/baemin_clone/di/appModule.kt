package hgh.project.baemin_clone.di

import hgh.project.baemin_clone.data.respository.map.DefaultMapRepository
import hgh.project.baemin_clone.data.respository.map.MapRepository
import hgh.project.baemin_clone.data.respository.restaurant.DefaultRestaurantRepository
import hgh.project.baemin_clone.data.respository.restaurant.RestaurantRepository
import hgh.project.baemin_clone.screen.main.home.HomeViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantCategory
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListViewModel
import hgh.project.baemin_clone.screen.main.my.MyViewModel
import hgh.project.baemin_clone.util.provider.DefaultResourcesProvider
import hgh.project.baemin_clone.util.provider.ResourceProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel( get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) ->
        RestaurantListViewModel(
            restaurantCategory,
            get()
        )
    }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
    single<MapRepository> {DefaultMapRepository(get(), get())}

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideMapRetrofit(get(), get()) }
    single { provideMapApiService(get())}

    single<ResourceProvider> { DefaultResourcesProvider(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }
}