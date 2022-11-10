package hgh.project.baemin_clone.widget.adapter.listener.order

import hgh.project.baemin_clone.model.food.FoodModel
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener

interface OrderMenuListListener: AdapterListener {

    fun onRemoveItem(model: FoodModel)

}