package hgh.project.baemin_clone.widget.adapter.listener.restaurant

import hgh.project.baemin_clone.model.restaurant.RestaurantModel

interface RestaurantLikeListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)

}