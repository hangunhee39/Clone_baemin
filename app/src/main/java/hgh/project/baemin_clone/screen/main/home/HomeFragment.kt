package hgh.project.baemin_clone.screen.main.home

import com.google.android.material.tabs.TabLayoutMediator
import hgh.project.baemin_clone.databinding.FragmentHomeBinding
import hgh.project.baemin_clone.screen.base.BaseFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantCategory
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment
import hgh.project.baemin_clone.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel,FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var  viewPagerAdapter: RestaurantListFragmentPagerAdapter

    override fun initViews() {
        super.initViews()
        initViewPager()
    }

    //fragment 를 recyclerView 처럼 viewPager 에 세팅
    private fun initViewPager() = with(binding){
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()){
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList
            )
            viewPager.adapter = viewPagerAdapter
        }
        viewPager.offscreenPageLimit = restaurantCategories.size
        //tab 레이아웃에 탭 뿌리기
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()

    }

    override fun observeData() {
    }

    companion object {
        fun newInstance() = HomeFragment()
        const val  TAG ="HomeFragment"
    }
}
