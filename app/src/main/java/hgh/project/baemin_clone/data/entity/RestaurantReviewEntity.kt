package hgh.project.baemin_clone.data.entity

import android.net.Uri

data class RestaurantReviewEntity(
    override val id: Long,
    val title: String,
    val description: String,
    val grade: Int,
    val image: List<Uri>?=null
) : Entity
