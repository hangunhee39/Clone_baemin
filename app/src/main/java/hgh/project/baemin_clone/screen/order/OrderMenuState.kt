package hgh.project.baemin_clone.screen.order

import androidx.annotation.StringRes
import hgh.project.baemin_clone.model.food.FoodModel

sealed class OrderMenuState{
    object Uninitialized : OrderMenuState()

    object Loading: OrderMenuState()

    data class Success(
        val restaurantFOodMenuList: List<FoodModel>? =null
    ):OrderMenuState()

    object Order: OrderMenuState()

    data class Error(
        @StringRes val messageId: Int,
        val e : Throwable
    ):OrderMenuState()
}
