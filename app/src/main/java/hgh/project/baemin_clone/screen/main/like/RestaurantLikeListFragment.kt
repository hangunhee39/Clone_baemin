package hgh.project.baemin_clone.screen.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import hgh.project.baemin_clone.databinding.FragmentRestaurantLikeListBinding
import hgh.project.baemin_clone.model.restaurant.RestaurantModel
import hgh.project.baemin_clone.screen.base.BaseFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.RestaurantDetailActivity
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.ModelRecyclerAdapter
import hgh.project.baemin_clone.widget.adapter.listener.restaurant.RestaurantLikeListListener
import hgh.project.baemin_clone.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment:BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override fun getViewBinding(): FragmentRestaurantLikeListBinding = FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantLikeListViewModel>()

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : RestaurantLikeListListener {
                override fun onDislikeItem(model: RestaurantModel) {
                    viewModel.dislikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(
                            requireContext(),
                            model.toEntity()
                        )
                    )
                }
            })
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner){
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>){
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()){
            adapter.submitList(restaurantList)
        }

    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = RestaurantLikeListFragment()

        const val TAG = "restaurantLikeListFragment"
    }
}