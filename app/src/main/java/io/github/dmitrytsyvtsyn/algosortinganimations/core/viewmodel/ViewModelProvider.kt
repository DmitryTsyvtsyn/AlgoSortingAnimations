package io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel

class ViewModelProvider {

    private val cache = hashMapOf<String, CoreViewModel>()

    fun <T : CoreViewModel> provide(viewModelClass: Class<T>): T {
        val key = key(viewModelClass)
        return cache[key] as? T ?: throw IllegalStateException("Not found such a ViewModel with key: $key")
    }

    fun <T : CoreViewModel> provide(viewModelClass: Class<T>, factory: () -> T): T {
        val key = key(viewModelClass)
        val cachedViewModel = cache[key]
        if (cachedViewModel != null) return cachedViewModel as T

        val viewModel = factory.invoke()
        cache[key] = viewModel
        return viewModel
    }

    fun <T : CoreViewModel> clear(viewModelClass: Class<T>) {
        cache.remove(key(viewModelClass))
    }

    private fun <T> key(viewModelClass: Class<T>): String =
        "ViewModelProvider.Key.${viewModelClass.canonicalName}"

}