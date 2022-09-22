package hgh.project.baemin_clone.screen.main.home

import hgh.project.baemin_clone.databinding.FragmentHomeBinding
import hgh.project.baemin_clone.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<HomeViewModel,FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        fun newInstance() = HomeFragment()
        const val  TAG ="HomeFragment"
    }
}
