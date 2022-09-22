package hgh.project.baemin_clone.screen.main.my

import hgh.project.baemin_clone.databinding.FragmentMyBinding
import hgh.project.baemin_clone.screen.base.BaseFragment
import hgh.project.baemin_clone.screen.main.home.HomeFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        fun newInstance() = MyFragment()
        const val  TAG ="MyFragment"
    }
}
