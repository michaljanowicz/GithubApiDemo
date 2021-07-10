package pl.janowicz.githubapidemo.features.userdetails.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.janowicz.githubapidemo.databinding.ActivityUserDetailsBinding
import pl.janowicz.githubapidemo.utils.error.ErrorSnackbar
import pl.janowicz.githubapidemo.utils.extensions.setTextAndVisibility

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private val viewModel: UserDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityUserDetailsBinding.inflate(layoutInflater).apply {
            setContentView(root)
            backImageView.setOnClickListener {
                viewModel.onBackClicked()
            }
            swipeRefreshLayout.setOnRefreshListener(viewModel::onSwipedRefresh)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.events.collect { event ->
                        handleUiEvent(event, binding)
                    }
                }
                launch {
                    viewModel.uiState.collect { uiState ->
                        handleUiState(uiState, binding)
                    }
                }
            }
        }
        viewModel.onCreated()
    }

    private fun handleUiEvent(event: UserDetailsScreenEvent, binding: ActivityUserDetailsBinding) {
        when (event) {
            is UserDetailsScreenEvent.ShowError -> ErrorSnackbar.showCallError(
                binding.root,
                event.error
            )
            is UserDetailsScreenEvent.GoBack -> onBackPressed()
        }
    }

    private fun handleUiState(
        state: UserDetailsScreenState,
        binding: ActivityUserDetailsBinding
    ) {
        binding.swipeRefreshLayout.isRefreshing = state is UserDetailsScreenState.Loading
        if (state is UserDetailsScreenState.Success) {
            val userDetails = state.userDetails
            with(binding) {
                avatarImageView.load(userDetails.avatarUrl) {
                    transformations(CircleCropTransformation())
                }
                nameTextView.setTextAndVisibility(userDetails.name)
                locationTextView.setTextAndVisibility(userDetails.location)
                websiteTextView.setTextAndVisibility(userDetails.website)
            }
        }
    }

    companion object {

        fun startActivity(context: Context, userLogin: String) {
            context.startActivity(
                Intent(context, UserDetailsActivity::class.java).apply {
                    putExtra(KEY_USER_LOGIN, userLogin)
                }
            )
        }
    }
}