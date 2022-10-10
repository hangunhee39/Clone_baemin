package hgh.project.baemin_clone.data.respository.user

import hgh.project.baemin_clone.data.entity.LocationLatLongEntity

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLongEntity?

    suspend fun insertUserLocation(locationLatLongEntity: LocationLatLongEntity)
}