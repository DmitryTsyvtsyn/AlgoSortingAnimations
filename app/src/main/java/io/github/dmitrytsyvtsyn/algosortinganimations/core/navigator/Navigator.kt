package io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator

import android.view.View
import android.view.ViewGroup
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.data.MemoryIDIdentityCache
import io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel.ViewModelProvider
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
        val view = event.view(BaseParams(parent.context, this, viewModelProvider))
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
        callbacks.removeLast().changeIsEnabled(false)

        return true
    }

    fun onRestoreBackStack(cache: MemoryIDIdentityCache) {
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

    fun onSaveBackStack(cache: MemoryIDIdentityCache) {
        callbacks.forEach { it.changeIsEnabled(false) }

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
        fun view(params: BaseParams): View
    }

}