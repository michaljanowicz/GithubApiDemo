package pl.janowicz.githubapidemo.utils.error

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import pl.janowicz.githubapidemo.R

object ErrorSnackbar {

    fun showNetworkError(parent: View): Snackbar =
        showSnackbar(parent, R.string.network_error_message)

    fun showServerError(parent: View): Snackbar =
        showSnackbar(parent, R.string.server_error_message)

    fun showError(parent: View, message: String): Snackbar =
        Snackbar.make(parent, message, Snackbar.LENGTH_INDEFINITE).also {
            it.show()
        }

    private fun showSnackbar(parent: View, @StringRes resId: Int): Snackbar =
        Snackbar.make(parent, resId, Snackbar.LENGTH_INDEFINITE).also {
            it.show()
        }
}