package hgh.project.baemin_clone.screen.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hgh.project.baemin_clone.data.preference.AppPreferenceManager
import hgh.project.baemin_clone.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val preferenceManager: AppPreferenceManager
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
            myStateLiveData.value =MyState.Success.Registered(
                userName = user.displayName ?:"익명",
                profileImageUri = user.photoUrl
            )
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    //idToken preference 에 삭제
    fun signOut() =viewModelScope.launch {
        withContext(Dispatchers.IO){
            preferenceManager.removeIdToken()
        }
        fetchData()
    }
}

