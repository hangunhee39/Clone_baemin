package hgh.project.baemin_clone.screen.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import hgh.project.baemin_clone.databinding.ActivityOrderMenuListBinding
import hgh.project.baemin_clone.model.food.FoodModel
import hgh.project.baemin_clone.screen.base.BaseActivity
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.ModelRecyclerAdapter
import hgh.project.baemin_clone.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override fun getViewBinding(): ActivityOrderMenuListBinding =
        ActivityOrderMenuListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<OrderMenuListViewModel>()

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object :
                OrderMenuListListener {
                override fun onRemoveItem(model: FoodModel) {
                    viewModel.removeOrderMenu(model)
                }
            })
    }

    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this) {
        when (it) {
            is OrderMenuState.Loading -> {
                handleLoading()
            }
            is OrderMenuState.Success -> {
                handleSuccess(it)
            }
            is OrderMenuState.Order -> {
                handleOrder()
            }
            is OrderMenuState.Error -> {
                handleError(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone = true
        adapter.submitList(state.restaurantFOodMenuList)
        val menuOrderIsEmpty = state.restaurantFOodMenuList.isNullOrEmpty()
        confirmButton.isEnabled = menuOrderIsEmpty.not()
        if (menuOrderIsEmpty){
            Toast.makeText(this@OrderMenuListActivity, "주문 메뉴가 없습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handleOrder() {
        Toast.makeText(this,"성공적으로 주문을 완료했습니다",Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleError(state: OrderMenuState.Error){
        Toast.makeText(this, getString(state.messageId, state.e), Toast.LENGTH_SHORT).show()
    }

    override fun initViews() = with(binding) {
        recyclerVIew.adapter =adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, OrderMenuListActivity::class.java)
    }
}