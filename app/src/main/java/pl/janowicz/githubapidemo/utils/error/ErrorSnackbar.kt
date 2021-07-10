package pl.janowicz.githubapidemo.utils.error

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import pl.janowicz.githubapidemo.R

object ErrorSnackbar {

    fun showCallError(parent: View, callError: CallError) {
        when (callError) {
            is CallError.ApiError -> showError(parent, callError.message)
            is CallError.NetworkError -> showSnackbar(parent, R.string.network_error_message)
            is CallError.UnknownError -> showSnackbar(parent, R.string.unknown_error_message)
        }
    }


    private fun showError(parent: View, message: String): Snackbar =
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).also {
            it.show()
        }

    private fun showSnackbar(parent: View, @StringRes resId: Int): Snackbar =
        Snackbar.make(parent, resId, Snackbar.LENGTH_LONG).also {
            it.show()
        }
}