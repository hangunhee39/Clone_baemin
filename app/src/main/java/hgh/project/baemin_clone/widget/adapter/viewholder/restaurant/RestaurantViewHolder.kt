package hgh.project.baemin_clone.widget.adapter.viewholder.restaurant

import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.databinding.ViewholderEmptyBinding
import hgh.project.baemin_clone.databinding.ViewholderRestaurantBinding
import hgh.project.baemin_clone.extiensions.clear
import hgh.project.baemin_clone.extiensions.load
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.model.restaurant.RestaurantModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.restaurant.RestaurantListListener
import hgh.project.baemin_clone.widget.adapter.viewholder.ModelVIewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
): ModelVIewHolder<RestaurantModel>(binding, viewModel, resourceProvider) {

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is RestaurantListListener){
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourceProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text = resourceProvider.getString(R.string.review_count, model.reviewCount)
            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text = resourceProvider.getString(R.string.delivery_time, minTime, maxTime)

            val (minTip, maxTip) = model.deliveryTipRange
            deliveryTipText.text = resourceProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }

    }

    override fun reset() = with(binding){
        restaurantImage.clear()
    }
}