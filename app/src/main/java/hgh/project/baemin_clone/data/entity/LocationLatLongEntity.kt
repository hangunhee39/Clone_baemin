package hgh.project.baemin_clone.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationLatLongEntity(
    val latitude: Double,
    val longitude: Double,
    override val id: Long = -1,
) : Entity, Parcelable
