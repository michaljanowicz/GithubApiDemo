package pl.janowicz.githubapidemo.utils.extensions

import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.getLayoutInflater() = LayoutInflater.from(context)