package hgh.project.baemin_clone.data.respository.user

import hgh.project.baemin_clone.data.db.dao.LocationDao
import hgh.project.baemin_clone.data.db.dao.RestaurantDao
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val restaurantDao: RestaurantDao,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUserLocation(): LocationLatLongEntity? = withContext(ioDispatcher) {
        locationDao.get(-1) //Default value
    }

    override suspend fun insertUserLocation(locationLatLongEntity: LocationLatLongEntity) =
        withContext(ioDispatcher) {
            locationDao.insert(locationLatLongEntity)
        }

    override suspend fun getUserLikedRestaurant(restaurantTitle: String): RestaurantEntity? =
        withContext(ioDispatcher) {
            restaurantDao.get(restaurantTitle)
        }

    override suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)= withContext(ioDispatcher) {
        restaurantDao.insert(restaurantEntity)
    }

    override suspend fun deleteUserLikedRestaurant(restaurantTitle: String)= withContext(ioDispatcher) {
        restaurantDao.delete(restaurantTitle)
    }

    override suspend fun deleteALlUserLikedRestaurant() = withContext(ioDispatcher){
        restaurantDao.deleteAll()
    }
}