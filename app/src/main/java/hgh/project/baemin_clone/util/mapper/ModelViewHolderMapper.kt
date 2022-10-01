package hgh.project.baemin_clone.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import hgh.project.baemin_clone.databinding.ViewholderEmptyBinding
import hgh.project.baemin_clone.databinding.ViewholderRestaurantBinding
import hgh.project.baemin_clone.model.CellType
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.viewholder.EmptyViewHolder
import hgh.project.baemin_clone.widget.adapter.viewholder.ModelVIewHolder
import hgh.project.baemin_clone.widget.adapter.viewholder.restaurant.RestaurantViewHolder

object ModelViewHolderMapper{

    @Suppress("UNCHECKED_CAST")
    fun<M: Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourceProvider: ResourceProvider
    ):ModelVIewHolder<M>{
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when(type){
            CellType.EMPTY_CELl -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourceProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourceProvider
            )
        }
        return viewHolder as ModelVIewHolder<M>
    }

}