package hgh.project.baemin_clone.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayoutMediator
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.MapSearchInfoEntity
import hgh.project.baemin_clone.databinding.FragmentHomeBinding
import hgh.project.baemin_clone.screen.base.BaseFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantCategory
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantOrder
import hgh.project.baemin_clone.screen.mylocation.MyLocationActivity
import hgh.project.baemin_clone.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    //위치설정 Activity 런처
    private val changeLocationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KET)?.let { myLocationInfo ->
                viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLong)
            }
        }
    }

    //permission 권한 관리 런처
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermission = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (responsePermission.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleTextView) {
                    setText(R.string.please_setup_location)
                    setOnClickListener {
                        getMyLocation()
                    }
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.can_not_assigned_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun initViews() = with(binding) {
        locationTitleTextView.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(requireContext(),mapInfo)
                )
            }
        }
        //정렬 기준 선택
        filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.chipDefault -> {
                    chipInitialize.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chipInitialize ->{
                    chipDefault.isChecked= true
                }
                R.id.chipLowDeliveryTip->{
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery ->{
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chipTopRate ->{
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_LATE)
                }
            }
        }
    }

    //배달 빠른순등으로 정렬
    private fun changeRestaurantOrder(order:RestaurantOrder){
        viewPagerAdapter.fragmentList.forEach{
            it.viewModel.setRestaurantOrder(order)
        }

    }

    //fragment 를 recyclerView 처럼 viewPager 에 세팅
    private fun initViewPager(locationLatLongEntity: LocationLatLongEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            filterChipGroup.isVisible =true
            //카테고리에 따라 나누기
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it , locationLatLongEntity)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLongEntity
            )
            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size
            //tab 레이아웃에 탭 뿌리기
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }
        //지도에서 위치를 바꿨을때(수동) RestaurantList 새로 고침
        if(locationLatLongEntity != viewPagerAdapter.locationLatLongEntity){
            viewPagerAdapter.locationLatLongEntity =locationLatLongEntity
            viewPagerAdapter.fragmentList.forEach{
                it.viewModel.setLocationLatLng(locationLatLongEntity)
            }
        }

    }

    override fun observeData() = viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is HomeState.Uninitialized -> {
                getMyLocation()
            }
            is HomeState.Loading -> {
                binding.locationLoading.isVisible = true
                binding.locationTitleTextView.text = getString(R.string.loading)
            }
            is HomeState.Success -> {
                binding.locationLoading.isGone = true
                binding.locationTitleTextView.text = it.mapSearchInfo.fullAddress
                binding.tabLayout.isVisible = true
                binding.filterScrollView.isVisible = true
                binding.viewPager.isVisible = true
                initViewPager(it.mapSearchInfo.locationLatLong)
                if (it.isLocationSame.not()) {
                    Toast.makeText(requireContext(), R.string.please_set_your_location, Toast.LENGTH_LONG).show()
                }
            }
            is HomeState.Error -> {
                binding.locationLoading.isGone = true
                binding.locationTitleTextView.setOnClickListener {
                    getMyLocation()
                }
                binding.locationTitleTextView.text = getString(R.string.location_not_found)
                Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsUnabled) {
            //권한 요청 런쳐 실행
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }


    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"
    }

    //위치가 바꿨을때
    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(p0: Location) {
            //binding.locationTitleTextView.text = "${p0.latitude},${p0.longitude}"
            //위도경도 -> 도로명 주소로 바꾸기
            viewModel.loadReverseGeoInformation(
                LocationLatLongEntity(
                    p0.latitude,
                    p0.longitude
                )
            )
            removeLocationListener()
        }
    }

    //위치가 한번 불러졌을때 계속 불러지는거 방지
    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }
}
