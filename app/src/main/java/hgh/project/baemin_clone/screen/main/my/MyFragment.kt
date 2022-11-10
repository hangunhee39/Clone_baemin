package hgh.project.baemin_clone.screen.main.my

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import hgh.project.baemin_clone.R
import hgh.project.baemin_clone.databinding.FragmentMyBinding
import hgh.project.baemin_clone.extiensions.load
import hgh.project.baemin_clone.model.order.OrderModel
import hgh.project.baemin_clone.screen.base.BaseFragment
import hgh.project.baemin_clone.screen.main.home.HomeFragment
import hgh.project.baemin_clone.screen.review.AddRestaurantReviewActivity
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.ModelRecyclerAdapter
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener
import hgh.project.baemin_clone.widget.adapter.listener.order.OrderListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel, MyViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            object : OrderListListener {
                override fun writeRestaurantReview(orderId: String, restaurantTitle: String) {
                    startActivity(
                        AddRestaurantReviewActivity.newIntent(
                            requireContext(),
                            orderId = orderId,
                            restaurantTitle = restaurantTitle
                        )
                    )
                }
            })
    }

    //구글 로그인 서비스
    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    //구글 로그인 런쳐
    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    //에러 처리
                    task.getResult(ApiException::class.java)?.let { accout ->
                        viewModel.saveToken(accout.idToken ?: throw Exception())
                    }
                } catch (e: Exception) {

                }
            }
        }

    private var isFirstShown = false

    override fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
        recyclerView.adapter = adapter
    }

    //로그인 함수
    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is MyState.Loading -> handleLoadingState()
            is MyState.Success -> handleSuccessState(it)
            is MyState.Login -> handleLoginState(it)
            is MyState.Error -> handleErrorState(it)
            else -> Unit
        }
    }

    private fun handleLoadingState() {
        binding.progressBar.isVisible = true
        binding.loginRequiredGroup.isGone = true
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.isGone = true
        when (state) {
            is MyState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageVIew.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

        //Log.d("text",state.orderList.toString())
        adapter.submitList(state.orderList)
    }

    private fun handleLoginState(state: MyState.Login) {
        binding.progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }

    private fun handleErrorState(state: MyState.Error) {
        binding.progressBar.isGone = true
    }

    companion object {
        fun newInstance() = MyFragment()
        const val TAG = "MyFragment"
    }
}
