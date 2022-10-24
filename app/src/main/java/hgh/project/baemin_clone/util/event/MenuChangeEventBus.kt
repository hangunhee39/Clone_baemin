package hgh.project.baemin_clone.util.event

import hgh.project.baemin_clone.screen.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

//mainActivity 에 메뉴탭을 바꿀수있게하는 eventBus
class MenuChangeEventBus {

    val mainTabMenuFlow =MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu: MainTabMenu){
        mainTabMenuFlow.emit(menu)
    }
}