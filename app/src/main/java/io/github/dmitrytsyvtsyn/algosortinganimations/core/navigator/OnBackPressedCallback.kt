package io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator

abstract class OnBackPressedCallback(isEnabled: Boolean) {

    private var enabled: Boolean = isEnabled
    private var enabledCallback: (Boolean) -> Unit = {}

    val isEnabled: Boolean
        get() = enabled

    abstract fun handleOnBackPressed()

    fun changeIsEnabled(isEnabled: Boolean) {
        enabled = isEnabled
        enabledCallback.invoke(enabled)
    }

    fun changeIsEnabledCallback(callback: (Boolean) -> Unit) {
        enabledCallback = callback
    }

}