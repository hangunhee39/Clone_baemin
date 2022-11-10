package hgh.project.baemin_clone.model.order

import hgh.project.baemin_clone.data.entity.OrderEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>,
    val restaurantTitle: String
): Model( id, type) {

    fun toEntity() =OrderEntity(
        id =orderId,
        userId=userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList,
        restaurantTitle =restaurantTitle
    )
}
