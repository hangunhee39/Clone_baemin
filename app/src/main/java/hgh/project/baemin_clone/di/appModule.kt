package hgh.project.baemin_clone.di

import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.MapSearchInfoEntity
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.data.preference.AppPreferenceManager
import hgh.project.baemin_clone.data.respository.food.DefaultRestaurantFoodRepository
import hgh.project.baemin_clone.data.respository.food.RestaurantFoodRepository
import hgh.project.baemin_clone.data.respository.map.DefaultMapRepository
import hgh.project.baemin_clone.data.respository.map.MapRepository
import hgh.project.baemin_clone.data.respository.restaurant.DefaultRestaurantRepository
import hgh.project.baemin_clone.data.respository.restaurant.RestaurantRepository
import hgh.project.baemin_clone.data.respository.review.DefaultRestaurantReviewRepository
import hgh.project.baemin_clone.data.respository.review.RestaurantReviewRepository
import hgh.project.baemin_clone.data.respository.user.DefaultUserRepository
import hgh.project.baemin_clone.data.respository.user.UserRepository
import hgh.project.baemin_clone.screen.main.home.HomeViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantCategory
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import hgh.project.baemin_clone.screen.main.like.RestaurantLikeListViewModel
import hgh.project.baemin_clone.screen.main.my.MyViewModel
import hgh.project.baemin_clone.screen.mylocation.MyLocationViewModel
import hgh.project.baemin_clone.util.provider.DefaultResourcesProvider
import hgh.project.baemin_clone.util.provider.ResourceProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get()) }
    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { (restaurantCategory: RestaurantCategory,
                    locationLatLng: LocationLatLongEntity) ->
        RestaurantListViewModel(
            restaurantCategory,
            locationLatLng,
            get()
        )
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(
            mapSearchInfoEntity,
            get(),
            get()
        )
    }
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(
            restaurantEntity,
            get(),
            get()
        )
    }
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(
            restaurantId,
            restaurantFoodList,
            get()
        )
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get()) }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single(named("map")) { provideMapRetrofit(get(), get()) }   //api 이름 붙이기(구별하기 위해서)
    single(named("food")) { provideFoodRetrofit(get(), get()) }
    single { provideMapApiService(get(qualifier = named("map"))) } //적용용
    single { provideFoodApiService(get(qualifier = named("food"))) }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuDao(get()) }

    single<ResourceProvider> { DefaultResourcesProvider(androidApplication()) }

    single { AppPreferenceManager(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }
}