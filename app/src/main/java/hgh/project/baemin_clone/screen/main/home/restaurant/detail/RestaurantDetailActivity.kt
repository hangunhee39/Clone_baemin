package hgh.project.baemin_clone.screen.main.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.data.entity.RestaurantFoodEntity
import hgh.project.baemin_clone.databinding.ActivityRestaurantDetailBinding
import hgh.project.baemin_clone.extiensions.fromDpToPx
import hgh.project.baemin_clone.extiensions.load
import hgh.project.baemin_clone.screen.base.BaseActivity
import hgh.project.baemin_clone.screen.main.MainActivity
import hgh.project.baemin_clone.screen.main.MainTabMenu
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.detail.review.RestaurantReviewListFragment
import hgh.project.baemin_clone.screen.order.OrderMenuListActivity
import hgh.project.baemin_clone.util.event.MenuChangeEventBus
import hgh.project.baemin_clone.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    override fun getViewBinding(): ActivityRestaurantDetailBinding =
        ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    private lateinit var viewPagerAdapter: RestaurantDetailListFragmentPagerAdapter

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            is RestaurantDetailState.Loading ->{
                handleLoading()
            }
            is RestaurantDetailState.Success -> {
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading()= with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.isGone= true

        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTalNumber == null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        deliveryTimeText.text =
            getString(
                R.string.delivery_expected_time,
                restaurantEntity.deliveryTimeRange.first,
                restaurantEntity.deliveryTimeRange.second
            )
        deliveryTipText.text =
            getString(
                R.string.delivery_tip_range,
                restaurantEntity.deliveryTipRange.first,
                restaurantEntity.deliveryTipRange.second
            )

        //drawable setUp ??????
        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart
                } else {
                    R.drawable.ic_heart_border
                }
            ),
            null, null, null
        )

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(state.restaurantEntity.restaurantTitle,state.restaurantEntity.restaurantInfoId, state.restaurantFoodList)
        }

        notifyBasketCount(state.foodMenuListInBasket)

        //?????? ???????????? ????????? ????????? food ??????
        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction
        if (isClearNeed){
            alertClearNeedInBasket(afterAction)
        }
    }

    private fun initViewPager(
        restaurantTitle: String,
        restaurantInfoId: Long,
        restaurantFoodList: List<RestaurantFoodEntity>?
    ) {
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(restaurantTitle)
            )
        )
        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        //?????? & ?????? tab
        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position ->
            tab.setText(RestaurantCategoryDetail.values()[position].categoryNameId)
        }.attach()
    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) = with(binding){
        basketCountTextView.text = if (foodMenuListInBasket.isNullOrEmpty()){
            "0"
        }else{
            getString(R.string.basket_count, foodMenuListInBasket.size)
        }
        basketButton.setOnClickListener{
            //????????? ??????
            if (firebaseAuth.currentUser ==null){
                alertLoginNeed {
                    lifecycleScope.launch {
                        //sharedFlow ??? ??????????????? ????????????.?
                        menuChangeEventBus.changeMenu(MainTabMenu.MY)
                        finish()
                    }
                }
            }else{
                startActivity(
                    OrderMenuListActivity.newIntent(this@RestaurantDetailActivity)
                )
            }
        }
    }

    //????????? ????????? dialog
    private fun alertLoginNeed(afterAction: () ->Unit){
        AlertDialog.Builder(this)
            .setTitle("???????????? ???????????????.")
            .setMessage("??????????????? ???????????? ???????????????. My????????? ?????????????????????????")
            .setPositiveButton("??????") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("??????") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    //???????????? ????????? ???????????? ????????? dialog
    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("?????????????????? ?????? ????????? ????????? ?????? ??? ????????????.")
            .setMessage("???????????? ????????? ??????????????? ?????? ?????? ????????? ?????? ????????? ???????????????.")
            .setPositiveButton("??????") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("??????") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    override fun initViews() {
        initAppBar()
    }

    //????????? ????????? appbar ??? ?????? ?????? ???????????????
    private fun initAppBar() = with(binding) {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val topPadding = 300f.fromDpToPx().toFloat()
            //????????? ?????? ??????
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            //????????? ?????? (?????? ??????)
            val realAlphaVerticalOffset: Float =
                if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }
            //???????? ??????
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            //????????? ?????????
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })

        toolbar.setNavigationOnClickListener { finish() }

        //???????????? ????????????
        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let { telNumber ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                startActivity(intent)
            }
        }
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "????????? ????????? : ${restaurantInfo.restaurantTitle}" +
                                "\n ?????? : ${restaurantInfo.grade}" +
                                "\n????????? : ${restaurantInfo.restaurantTalNumber}"
                    )
                    Intent.createChooser(this, "???????????? ????????????") //ActionChoose
                }
                startActivity(intent)
            }
        }

    }

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
            }
    }
}