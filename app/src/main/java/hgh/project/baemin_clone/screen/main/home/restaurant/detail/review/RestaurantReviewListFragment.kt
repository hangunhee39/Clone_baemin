package hgh.project.baemin_clone.screen.main.home.restaurant.detail.review

import android.content.res.loader.ResourcesProvider
import android.os.Bundle
import android.widget.Adapter
import android.widget.Toast
import androidx.core.os.bundleOf
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.databinding.FragmentListBinding
import hgh.project.baemin_clone.model.food.FoodModel
import hgh.project.baemin_clone.model.restaurant.RestaurantModel
import hgh.project.baemin_clone.model.review.RestaurantReviewModel
import hgh.project.baemin_clone.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.RestaurantDetailActivity
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.ModelRecyclerAdapter
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.food.FoodMenuListListener
import hgh.project.baemin_clone.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantReviewListViewModel> {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }
    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : AdapterListener{}
        )
    }

    override fun initViews() {
        binding.recyclerView.adapter =adapter
    }

    override fun observeData() =viewModel.reviewStateLiveData.observe(viewLifecycleOwner){
        when(it){
            is RestaurantReviewState.Success ->{
                handleSuccess(it)
            }
            else ->Unit
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success){
        adapter.submitList(state.reviewList)
    }

    companion object{

        const val RESTAURANT_TITLE_KEY= "restaurantTitle"

        fun newInstance(restaurantTitle: String): RestaurantReviewListFragment {
            val bundle= bundleOf (
            RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}