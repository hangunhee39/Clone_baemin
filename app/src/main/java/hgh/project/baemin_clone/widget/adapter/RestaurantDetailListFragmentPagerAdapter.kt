package hgh.project.baemin_clone.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import hgh.project.baemin_clone.data.entity.LocationLatLongEntity
import hgh.project.baemin_clone.screen.main.home.restaurant.RestaurantListFragment

class RestaurantDetailListFragmentPagerAdapter(
    val activity: FragmentActivity,
    var fragmentList: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}