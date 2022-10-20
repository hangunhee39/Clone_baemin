package hgh.project.baemin_clone.widget.adapter.listener.food

import hgh.project.baemin_clone.model.food.FoodModel
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener

interface FoodMenuListListener:AdapterListener {

    fun onClickItem(model: FoodModel)
}