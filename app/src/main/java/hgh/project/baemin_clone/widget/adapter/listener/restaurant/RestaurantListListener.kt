package hgh.project.baemin_clone.widget.adapter.listener.restaurant

import hgh.project.baemin_clone.model.restaurant.RestaurantModel
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener

interface RestaurantListListener :AdapterListener {

    fun onClickItem(model: RestaurantModel)
}