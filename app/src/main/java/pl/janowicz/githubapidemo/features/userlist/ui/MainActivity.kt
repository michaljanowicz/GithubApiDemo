package pl.janowicz.githubapidemo.features.userlist.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.janowicz.githubapidemo.databinding.ActivityMainBinding
import pl.janowicz.githubapidemo.features.userdetails.ui.UserDetailsActivity
import pl.janowicz.githubapidemo.utils.error.ErrorSnackbar

private const val NUMBER_OF_COLUMNS = 2

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userAdapter = UsersAdapter(viewModel::onUserClicked)
        val binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            swipeRefreshLayout.setOnRefreshListener(viewModel::onSwipedRefresh)
            recyclerView.layoutManager = StaggeredGridLayoutManager(
                NUMBER_OF_COLUMNS,
                StaggeredGridLayoutManager.VERTICAL
            )
            recyclerView.adapter = userAdapter
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
                        handleUiState(uiState, binding, userAdapter)
                    }
                }
            }
        }
    }

    private fun handleUiEvent(event: UsersScreenEvent, binding: ActivityMainBinding) {
        when (event) {
            is UsersScreenEvent.ShowError -> ErrorSnackbar.showCallError(binding.root, event.error)
            is UsersScreenEvent.OpenUserDetails -> UserDetailsActivity.startActivity(
                this@MainActivity,
                event.userLogin
            )
        }
    }

    private fun handleUiState(
        state: UsersScreenState,
        binding: ActivityMainBinding,
        userAdapter: UsersAdapter
    ) {
        binding.swipeRefreshLayout.isRefreshing = state is UsersScreenState.Loading
        if (state is UsersScreenState.Success) {
            userAdapter.submitList(state.users)
        }
    }
}