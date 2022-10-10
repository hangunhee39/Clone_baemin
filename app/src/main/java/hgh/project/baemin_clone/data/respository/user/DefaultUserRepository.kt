package hgh.project.baemin_clone.data.respository.user

import hgh.project.baemin_clone.data.db.dao.LocationDao
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
): UserRepository {
    override suspend fun getUserLocation(): LocationLatLongEntity? = withContext(ioDispatcher) {
        locationDao.get(-1) //Default value
    }

    override suspend fun insertUserLocation(locationLatLongEntity: LocationLatLongEntity) =
        withContext(ioDispatcher){
        locationDao.insert(locationLatLongEntity)
    }
}