package hgh.project.baemin_clone.screen.mylocation

import androidx.annotation.StringRes
import hgh.project.baemin_clone.data.entity.MapSearchInfoEntity
import hgh.project.baemin_clone.screen.main.home.HomeState

sealed class MyLocationState {
    object Uninitialized : MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Error(
        @StringRes val messageId: Int
    ) : MyLocationState()

}