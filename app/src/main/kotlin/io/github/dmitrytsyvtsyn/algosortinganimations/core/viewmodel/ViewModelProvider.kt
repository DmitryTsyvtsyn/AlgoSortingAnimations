package io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel

class ViewModelProvider : CoreViewModel {

    private val cache = hashMapOf<String, CoreViewModel>()

    fun provideSubProvider(key: String, factory: () -> ViewModelProvider = ::ViewModelProvider): ViewModelProvider {
        val provider = cache[key]
        if (provider != null && provider is ViewModelProvider) return provider

        val newProvider = factory.invoke()
        val newProviderCache = newProvider.cache
        // child ViewModelProviders must have access to parent ViewModels
        cache.forEach { (key, value) ->
            if (value !is ViewModelProvider) {
                newProviderCache[key] = value
            }
        }

        cache[key] = newProvider

        return newProvider
    }

    fun removeSubProvider(key: String) {
        val viewModel = cache[key]
        if (viewModel is ViewModelProvider) {
            viewModel.cache.clear()
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