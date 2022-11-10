package hgh.project.baemin_clone.widget.adapter.listener.order

import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener

interface OrderListListener: AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)
}