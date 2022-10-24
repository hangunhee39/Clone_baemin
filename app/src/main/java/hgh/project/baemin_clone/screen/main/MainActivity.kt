package hgh.project.baemin_clone.screen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.databinding.ActivityMainBinding
import hgh.project.baemin_clone.screen.main.home.HomeFragment
import hgh.project.baemin_clone.screen.main.like.RestaurantLikeListFragment
import hgh.project.baemin_clone.screen.main.my.MyFragment
import hgh.project.baemin_clone.util.event.MenuChangeEventBus
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()

        initViews()
    }

    private fun observeData(){
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect{
                goToTab(it)
            }
        }
    }

    private fun initViews() = with(binding){
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_home ->{
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }
            R.id.menu_like->{
                showFragment(RestaurantLikeListFragment.newInstance(), RestaurantLikeListFragment.TAG)
                true
            }
            R.id.menu_my ->{
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu){
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    private fun showFragment(fragment: Fragment, tag: String){
        val findFragment =supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach{
            supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss() //모든 프레그먼트 숨기기
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
            //이미 등록한 상태는 commit 해도 상관없음
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()  //onSaveInstanceState 무관하게 동작 시키기 위해서 (처음할때)
        }
    }
}

enum class MainTabMenu(@IdRes val menuId: Int){

    HOME(R.id.menu_home),LIKE(R.id.menu_like), MY(R.id.menu_my)
}