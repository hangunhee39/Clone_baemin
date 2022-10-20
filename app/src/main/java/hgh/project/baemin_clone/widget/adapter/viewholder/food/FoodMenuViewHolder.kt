package hgh.project.baemin_clone.widget.adapter.viewholder.food

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.databinding.ViewholderFoodMenuBinding
import hgh.project.baemin_clone.extiensions.clear
import hgh.project.baemin_clone.extiensions.load
import hgh.project.baemin_clone.model.food.FoodModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.food.FoodMenuListListener
import hgh.project.baemin_clone.widget.adapter.viewholder.ModelVIewHolder

class FoodMenuViewHolder(
    private val binding: ViewholderFoodMenuBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelVIewHolder<FoodModel>(binding, viewModel, resourceProvider){
    override fun reset() =with(binding){
        foodImage.clear()
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if (adapterListener is FoodMenuListListener){
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

    override fun bindData(model: FoodModel)= with(binding) {
        foodImage.load(model.imageUrl, 24f, CenterCrop())
        foodTitleText.text= model.title
        foodDescriptionText.text = model.description
        priceText.text = resourceProvider.getString(R.string.price, model.price)
    }
}