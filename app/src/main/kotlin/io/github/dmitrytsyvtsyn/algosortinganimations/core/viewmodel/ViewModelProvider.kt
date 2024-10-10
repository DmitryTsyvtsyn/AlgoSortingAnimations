package io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel

@Suppress("UNCHECKED_CAST")
class ViewModelProvider : CoreViewModel {

    private val cache = hashMapOf<String, CoreViewModel>()

    fun createChildProvider(key: String): ViewModelProvider {
        val provider = cache[key]
        if (provider != null && provider is ViewModelProvider) return provider

        val childProvider = ViewModelProvider()
        val childProviderCache = childProvider.cache
        // child ViewModelProviders must have access to parent ViewModels
        cache.forEach { (key, value) ->
            if (value !is ViewModelProvider) {
                childProviderCache[key] = value
            }
        }

        cache[key] = childProvider

        return childProvider
    }

    fun removeChildProvider(key: String) {
        val provider = cache[key]
        if (provider is ViewModelProvider) {
            provider.cache.clear()
            cache.remove(key)
        }
    }

    fun <T : CoreViewModel> provide(viewModelClass: Class<T>): T {
        val key = key(viewModelClass)
        return cache[key] as? T ?: throw IllegalStateException("Not found such a ViewModel with the key: $key")
    }

    fun <T : CoreViewModel> provide(viewModelClass: Class<T>, factory: () -> T): T {
        val key = key(viewModelClass)
        val cachedViewModel = cache[key]
        if (cachedViewModel != null) return cachedViewModel as T

        val viewModel = factory.invoke()
        cache[key] = viewModel
        return viewModel
    }

    private fun <T> key(viewModelClass: Class<T>): String =
        "ViewModelProvider.Key.${viewModelClass.canonicalName}"

}