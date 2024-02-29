package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming

object ThemeManager {

    private var theme = CoreTheme.LIGHT
    private var insets: WindowInsets = WindowInsets(0, 0, 0, 0)
    private val themeListeners = mutableSetOf<ThemeManagerListener>()

    fun addThemeListener(listener: ThemeManagerListener) {
        themeListeners.add(listener)
        listener.notify()
    }

    fun removeThemeListener(listener: ThemeManagerListener) {
        themeListeners.remove(listener)
        listener.notify()
    }

    fun changeTheme(newTheme: CoreTheme) {
        if (theme == newTheme) return

        theme = newTheme
        themeListeners.notifyAll()
    }

    fun changeInsets(newInsets: WindowInsets) {
        insets = newInsets
        themeListeners.notifyAll()
    }

    private fun Set<ThemeManagerListener>.notifyAll() {
        forEach { listener -> listener.notify() }
    }

    private fun ThemeManagerListener.notify() {
        onThemeChanged(insets, theme)
    }

    fun interface ThemeManagerListener {
        fun onThemeChanged(insets: WindowInsets, theme: CoreTheme)
    }

    class WindowInsets(
        val start: Int,
        val top: Int,
        val end: Int,
        val bottom: Int
    )

}