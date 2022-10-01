package hgh.project.baemin_clone.model.restaurant

import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantCategory

data class RestaurantModel(
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>

) : Model(id, type) {
    fun toEntity() = RestaurantEntity(
        id,
        restaurantInfoId,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange
    )

}
