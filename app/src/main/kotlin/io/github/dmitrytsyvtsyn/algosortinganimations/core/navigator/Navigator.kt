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
    private val providers = mutableMapOf<String, ViewModelProvider>()

    private val stackId = R.id.navigation_stack

    fun navigateForward(event: NavigationScreen, isAddToBackStack: Boolean = true) {
        when {
            isAddToBackStack -> {
                val key = key(event::class.java)
                val provider = parentViewModelProvider().provideSubProvider(key)
                providers[key] = provider
                parent.addView(event.view(BaseParams(parent.context, this, provider)))
                stack.add(event)

                val callback = createOnBackPressedCallback()
                callbacks.add(callback)
                onBackPressedDispatcher.addCallback(callback)
            }
            else -> {
                parent.addView(event.view(BaseParams(parent.context, this, viewModelProvider)))
            }
        }
    }

    fun navigateBack(): Boolean {
        if (stack.isEmpty()) return false

        val event = stack.removeLast()
        val key = key(event::class.java)
        viewModelProvider.removeSubProvider(key)
        providers.remove(key)
        parent.removeLast()
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

    fun interface NavigationScreen {
        fun view(params: BaseParams): View
    }

    private fun createOnBackPressedCallback() =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        }

    private fun key(navigationScreenClass: Class<out NavigationScreen>) =
        "ViewModelProvider.SubProvider.Key.${navigationScreenClass.canonicalName}"

    private fun parentViewModelProvider(): ViewModelProvider {
        if (stack.isEmpty()) return viewModelProvider

        val key = key(stack.last()::class.java)
        return providers[key] ?: throw IllegalStateException("Not found such a ViewModelProvider with the key: $key")
    }

}