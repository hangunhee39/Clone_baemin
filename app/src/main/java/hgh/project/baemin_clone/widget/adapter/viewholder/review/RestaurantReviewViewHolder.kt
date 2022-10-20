package hgh.project.baemin_clone.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.databinding.ViewholderEmptyBinding
import hgh.project.baemin_clone.databinding.ViewholderRestaurantBinding
import hgh.project.baemin_clone.databinding.ViewholderRestaurantReviewBinding
import hgh.project.baemin_clone.extiensions.clear
import hgh.project.baemin_clone.extiensions.load
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.model.review.RestaurantReviewModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.restaurant.RestaurantListListener
import hgh.project.baemin_clone.widget.adapter.viewholder.ModelVIewHolder

class RestaurantReviewViewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
): ModelVIewHolder<RestaurantReviewModel>(binding, viewModel, resourceProvider) {

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = with(binding) {
        Unit
    }

    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if (model.thumbnailImageUri != null){
                reviewThumbnailImage.isVisible =true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString(), 24f)
            }else{
                reviewThumbnailImage.isGone=true
            }
            reviewTitleText.text=model.title
            reviewText.text= model.description
            ratingBar.rating =model.grade.toFloat()
        }

    }

    override fun reset() = with(binding){
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone= true
    }
}