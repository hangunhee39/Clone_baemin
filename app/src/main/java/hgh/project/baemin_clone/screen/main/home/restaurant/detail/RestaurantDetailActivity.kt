package hgh.project.baemin_clone.screen.main.home.restaurant.detail

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.android.material.appbar.AppBarLayout
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.RestaurantEntity
import hgh.project.baemin_clone.databinding.ActivityRestaurantDetailBinding
import hgh.project.baemin_clone.extiensions.fromDpToPx
import hgh.project.baemin_clone.extiensions.load
import hgh.project.baemin_clone.screen.base.BaseActivity
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment
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

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            RestaurantDetailState.Loading -> {

            }
            is RestaurantDetailState.Success -> {
                handleSuccess(it)
            }
            RestaurantDetailState.Uninitalized -> {
            }
        }
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTalNumber == null

        restaurantTitleTextView.text =restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text =restaurantEntity.restaurantTitle
        ratingBar.rating =restaurantEntity.grade
        deliveryTimeText.text =
            getString(R.string.delivery_expected_time, restaurantEntity.deliveryTimeRange.first, restaurantEntity.deliveryTimeRange.second)
        deliveryTipText.text =
            getString(R.string.delivery_tip_range, restaurantEntity.deliveryTipRange.first ,restaurantEntity.deliveryTipRange.second)

        //drawable setUp 하기
        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this@RestaurantDetailActivity, if (state.isLiked == true) {
                R.drawable.ic_heart
            } else {
                R.drawable.ic_heart_border
            }),
            null, null, null
        )
    }

    override fun initViews() {
        initAppBar()
    }

    //스크롤 내리면 appbar 에 가게 이름 나오게하기
    private fun initAppBar() = with(binding) {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val topPadding = 300f.fromDpToPx().toFloat()
            //스크롤 내린 크기
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            //투명화 비율 (내린 비율)
            val realAlphaVerticalOffset: Float =
                if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }
            //??내린 비율
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            //투명화 가속도
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })

        toolbar.setNavigationOnClickListener { finish() }

        //전화하기 바로가기
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
                        "맛있는 음식점 : ${restaurantInfo.restaurantTitle}"+
                                "\n 평점 : ${restaurantInfo.grade}"+
                                "\n연락처 : ${restaurantInfo.restaurantTalNumber}"
                    )
                    Intent.createChooser(this,"친구에게 공유하기") //ActionChoose
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