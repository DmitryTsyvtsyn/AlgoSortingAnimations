package io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator

import android.view.View

fun interface NavigationScreen {
    fun view(params: BaseParams): View
}