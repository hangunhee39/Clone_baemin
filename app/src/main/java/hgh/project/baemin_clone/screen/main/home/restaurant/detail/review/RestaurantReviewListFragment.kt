package hgh.project.baemin_clone.screen.main.home.restaurant.detail.review

import android.os.Bundle
import androidx.core.os.bundleOf
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.databinding.FragmentListBinding
import hgh.project.baemin_clone.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantReviewListViewModel>()

    override fun observeData() {
    }

    companion object{

        const val RESTAURANT_ID_KEY= "restaurantId"

        fun newInstance(restaurantId: Long): RestaurantReviewListFragment {
            val bundle= bundleOf (
            RESTAURANT_ID_KEY to restaurantId
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}