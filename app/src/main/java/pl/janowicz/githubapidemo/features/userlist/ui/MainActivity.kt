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
                viewModel.uiState.collect { uiState ->
                    binding.swipeRefreshLayout.isRefreshing = uiState is UsersScreenState.Loading

                    when (uiState) {
                        is UsersScreenState.Loading -> {
                            //binding.swipeRefreshLayout.isRefreshing = true
                        }
                        is UsersScreenState.Success -> {
                            userAdapter.submitList(uiState.users)
                        }
                        is UsersScreenState.Error -> {

                        }
                    }
                }
            }
        }
    }
}