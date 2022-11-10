package hgh.project.baemin_clone.model.food

import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description:String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String,
    val restaurantTitle: String
): Model(id, type){

    //basket 용 entity
    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}",title, description, price, imageUrl, restaurantId,restaurantTitle
    )
}
