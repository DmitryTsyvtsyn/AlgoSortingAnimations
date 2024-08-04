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
        if (insets == newInsets) return
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
    ) {
        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (other !is WindowInsets) return false

            return start == other.start && top == other.top &&
                    end == other.end && bottom == other.bottom
        }
        override fun hashCode(): Int {
            var result = start
            result = 31 * result + top
            result = 31 * result + end
            result = 31 * result + bottom
            return result
        }
    }

}