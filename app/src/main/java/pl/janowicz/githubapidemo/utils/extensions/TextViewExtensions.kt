package pl.janowicz.githubapidemo.utils.extensions

import android.widget.TextView
import androidx.core.view.isVisible

fun TextView.setTextAndVisibility(text: String) {
    isVisible = text.isNotEmpty()
    this.text = text
}