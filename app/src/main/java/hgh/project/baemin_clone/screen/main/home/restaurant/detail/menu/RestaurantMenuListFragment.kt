package hgh.project.baemin_clone.screen.main.home.restaurant.detail.menu

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.databinding.FragmentListBinding
import hgh.project.baemin_clone.model.food.FoodModel
import hgh.project.baemin_clone.model.restaurant.RestaurantModel
import hgh.project.baemin_clone.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListViewModel
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.RestaurantDetailActivity
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.ModelRecyclerAdapter
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.food.FoodMenuListListener
import hgh.project.baemin_clone.widget.adapter.listener.restaurant.RestaurantListListener
import okhttp3.internal.notify
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment :
    BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val restaurantId by lazy { arguments?.getLong(RESTAURANT_ID_KEY, -1) }
    private val restaurantFoodList by lazy { arguments?.getParcelableArrayList<RestaurantFoodEntity>(
        FOOD_LIST_KEY)!! }

    override val viewModel by viewModel<RestaurantMenuListViewModel>{
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : FoodMenuListListener{
                override fun onClickItem(model: FoodModel) {
                    viewModel.insertMenuInBasket(model)
                }
            })
    }

    //상위 viewModel 에 접근
    private val restaurantDetailViewModel by sharedViewModel<RestaurantDetailViewModel>()

    override fun initViews() {
        binding.recyclerView.adapter =adapter
    }

    override fun observeData(){
        viewModel.restaurantFoodLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        //현재 선택한 아이템
        viewModel.menuBasketLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "장바구니에 담겼습니다. 메뉴: ${it.title}", Toast.LENGTH_SHORT).show()
            //DetailViewModel basket 에 넣기
            restaurantDetailViewModel.notifyFoodMenuListInBasket(it)
        }

        viewModel.isClearNeedInBasketLiveData.observe(viewLifecycleOwner){ (isClearNeed, afterAction )->
            if (isClearNeed){
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed,afterAction)
            }

        }
    }

    companion object{

        const val RESTAURANT_ID_KEY= "restaurantId"
        const val FOOD_LIST_KEY = "foodList"

        fun newInstance(restaurantId: Long, foodList: ArrayList<RestaurantFoodEntity>): RestaurantMenuListFragment {
            val bundle= bundleOf (
            RESTAURANT_ID_KEY to restaurantId,
            FOOD_LIST_KEY to foodList

            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }
}