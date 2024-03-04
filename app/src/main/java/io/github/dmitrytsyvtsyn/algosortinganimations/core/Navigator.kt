package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.removeLast

class Navigator(
    private val parent: ViewGroup,
    private val viewModelProvider: ViewModelProvider,
    private val onBackPressedDispatcher: OnBackPressedDispatcher
) {

    private val tag = "Navigator"

    private val stack = mutableListOf<NavigationScreen>()
    private val callbacks = mutableListOf<OnBackPressedCallback>()

    private val stackId = R.id.navigation_stack

    fun navigateForward(event: NavigationScreen, isAddToBackStack: Boolean = true) {
        val view = event.view(BaseFragmentParams(parent.context, this, viewModelProvider))
        parent.addView(view)

        if (isAddToBackStack) {
            stack.add(event)
            createOnBackPressedCallback()
        }
    }

    fun navigateBack(): Boolean {
        if (stack.isEmpty()) return false

        parent.removeLast()
        stack.removeLast()
        callbacks.removeLast().isEnabled = false

        return true
    }

    fun onRestoreBackStack(cache: MemoryCache) {
        val cachedStack = cache.read<MutableList<NavigationScreen>>(stackId) ?: return
        cache.remove(stackId)

        stack.clear()
        callbacks.clear()

        var index = 0
        while (index < cachedStack.size) {
            navigateForward(cachedStack[index])

            index++
        }
    }

    fun onSaveBackStack(cache: MemoryCache) {
        callbacks.forEach { it.isEnabled = false }

        cache.save(stackId, stack)
    }

    private fun createOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        }
        callbacks.add(callback)
        onBackPressedDispatcher.addCallback(callback)
    }

    fun interface NavigationScreen {
        fun view(params: BaseFragmentParams): View
    }

}