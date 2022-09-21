package hgh.project.baemin_clone.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    protected var stateBundle: Bundle? =null    //activity(lifecycle 을 가진 컴포넌트)의 정보(상태) 저장

    open fun fetchData(): Job = viewModelScope.launch {  }

    open fun storeState(stateBundle: Bundle){   //View 상태 저장
        this.stateBundle =stateBundle
    }
}