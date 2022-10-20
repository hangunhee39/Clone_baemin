package hgh.project.baemin_clone.data.response.restaurant

import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description:String,
    val price: String,
    val imageUrl: String
){

    fun toEntity(restaurantId: Long) = RestaurantFoodEntity(
        id,
        title,
        description,
        price.toDouble().toInt(),
        imageUrl,
        restaurantId
    )
}