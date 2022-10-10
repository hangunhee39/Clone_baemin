package hgh.project.baemin_clone.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.data.entity.MapSearchInfoEntity
import hgh.project.baemin_clone.databinding.ActivityMyLocationBinding
import hgh.project.baemin_clone.screen.base.BaseActivity
import hgh.project.baemin_clone.screen.main.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel,ActivityMyLocationBinding>(), OnMapReadyCallback {

    override val viewModel by viewModel<MyLocationViewModel>{
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KET)
        )
    }

    override fun getViewBinding(): ActivityMyLocationBinding = ActivityMyLocationBinding.inflate(layoutInflater)

    private lateinit var map :GoogleMap

    private var isMapInitialized:Boolean = false
    private var isChangeLocation:Boolean =false

    //onMapReadyCallback
    override fun onMapReady(map: GoogleMap) {
        this.map = map ?:return
        viewModel.fetchData()
    }

    override fun initViews()= with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {
            viewModel.confirmSelectLocation()
        }
        setupGoogleMap()
    }

    private fun setupGoogleMap(){
        val mapFragment =supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun observeData() =viewModel.myLocationStateLiveData.observe(this){
        when(it){
            is MyLocationState.Loading -> {
                handleLoadingState()
            }
            is MyLocationState.Success -> {
                if (::map.isInitialized){
                    handleSuccessState(it)
                }
            }
            is MyLocationState.Confirm -> {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(HomeViewModel.MY_LOCATION_KET, it.mapSearchInfoEntity)
                })
                finish()
            }
            is MyLocationState.Error ->{
                Toast.makeText(this, it.messageId, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    private fun handleLoadingState() = with(binding){
        locationLoading.isVisible=true
        locationTitleTextView.text =getString(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding){
        val mapSearchInfo = state.mapSearchInfoEntity
        locationLoading.isGone=true
        locationTitleTextView.text =mapSearchInfo.fullAddress
        if (isMapInitialized.not()){
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInfo.locationLatLong.latitude,
                        mapSearchInfo.locationLatLong.longitude
                    ), CAMERA_ZOOM_LEVEL
                )
            )
            //지도가 멈췄을때 리스너(계속 api 쓰는거 방지)
            map.setOnCameraIdleListener {
                if (isChangeLocation.not()){
                    isChangeLocation =true
                    //1초 안움직이면 현재 주소 얻기
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng =map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLongEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    },1000)
                }
            }
            isMapInitialized =true
        }
    }

    private fun handleConfirmState() = with(binding){
    }

    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KET, mapSearchInfoEntity)
            }

        const val CAMERA_ZOOM_LEVEL=17f
    }

}