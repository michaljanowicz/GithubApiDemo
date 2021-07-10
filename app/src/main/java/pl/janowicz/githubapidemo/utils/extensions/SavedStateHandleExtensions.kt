package pl.janowicz.githubapidemo.utils.extensions

import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.requireValue(key: String) = requireNotNull(get<T>(key))