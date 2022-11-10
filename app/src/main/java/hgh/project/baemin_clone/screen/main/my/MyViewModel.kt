package hgh.project.baemin_clone.screen.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.data.entity.OrderEntity
import hgh.project.baemin_clone.data.preference.AppPreferenceManager
import hgh.project.baemin_clone.data.respository.order.DefaultOrderRepository
import hgh.project.baemin_clone.data.respository.order.OrderRepository
import hgh.project.baemin_clone.data.respository.user.UserRepository
import hgh.project.baemin_clone.model.order.OrderModel
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val preferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
):BaseViewModel() {

    val myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)

    override fun fetchData(): Job =viewModelScope.launch{
        myStateLiveData.value= MyState.Loading
        preferenceManager.getIdToken()?.let {
            myStateLiveData.value = MyState.Login(it)
        }?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }

    }

    //idtoken preference 에 저장
    fun saveToken(idToken: String) =viewModelScope.launch{
        withContext(Dispatchers.IO){
            preferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) =viewModelScope.launch{
        firebaseUser?.let {user->
            when (val orderMenusResult =orderRepository.getAllOrderMenus(user.uid)){
                is DefaultOrderRepository.Result.Success<*> ->{
                    val orderList = orderMenusResult.data as List<OrderEntity>
                    myStateLiveData.value =MyState.Success.Registered(
                        userName = user.displayName ?:"익명",
                        profileImageUri = user.photoUrl,
                        orderList =orderList.map {
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                    )
                }
                is DefaultOrderRepository.Result.Error ->{
                    myStateLiveData.value =MyState.Error(
                        R.string.request_error,
                        orderMenusResult.e
                    )
                }
            }
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    //idToken preference 에 삭제
    fun signOut() =viewModelScope.launch {
        withContext(Dispatchers.IO){
            preferenceManager.removeIdToken()
        }
        userRepository.deleteALlUserLikedRestaurant()
        fetchData()
    }
}

