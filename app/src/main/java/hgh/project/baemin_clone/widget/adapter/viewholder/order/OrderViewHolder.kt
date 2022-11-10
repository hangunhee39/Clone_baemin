package hgh.project.baemin_clone.widget.adapter.viewholder.order

import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.databinding.ViewholderOrderBinding
import hgh.project.baemin_clone.model.order.OrderModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.order.OrderListListener
import hgh.project.baemin_clone.widget.adapter.viewholder.ModelVIewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelVIewHolder<OrderModel>(binding, viewModel, resourceProvider) {
    override fun reset() = Unit

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderListListener){
            binding.root.setOnClickListener {
                adapterListener.writeRestaurantReview(model.orderId, model.restaurantTitle)
            }
        }
    }

    override fun bindData(model: OrderModel) = with(binding) {
        val foodMenuList = model.foodMenuList

        orderTitleText.text =
            resourceProvider.getString(R.string.order_history_title, model.orderId)
        orderTotalPriceText.text = resourceProvider.getString(
            R.string.price,
            foodMenuList.map { it.price }.reduce { total, price -> total + price })
        foodMenuList
            .groupBy { it.title }
            .entries.forEach { (title, menuList,)->
                val orderDataStr =
                    orderContentText.text.toString() + "메뉴 : $title | 가격 : ${menuList.first().price} X ${menuList.size}\n"
                orderContentText.text = orderDataStr
            }
    }
}