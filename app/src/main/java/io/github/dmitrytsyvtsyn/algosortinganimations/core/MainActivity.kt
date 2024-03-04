package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreFrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.viewGroupLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager
import io.github.dmitrytsyvtsyn.algosortinganimations.main.SortingAlgorithmMainFragment
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var navigator by Delegates.notNull<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TypefaceManager.setAssets(assets)

        val fragmentContainerView = CoreFrameLayout(this)
        fragmentContainerView.layoutParams(viewGroupLayoutParams().match())
        setContentView(fragmentContainerView)



        val navigator = Navigator(fragmentContainerView, ViewModelProvider(), onBackPressedDispatcher)
        this.navigator = navigator

        navigator.navigateForward(::SortingAlgorithmMainFragment, isAddToBackStack = false)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainerView) { _, windowInsets ->
            val systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            ThemeManager.changeInsets(systemBarsInsets.intoThemeInsets())

            WindowInsetsCompat.CONSUMED
        }

        checkDarkTheme()
        navigator.onRestoreBackStack((application as App).cache)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        checkDarkTheme(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigator.onSaveBackStack((application as App).cache)
    }

    private fun Insets.intoThemeInsets() = ThemeManager.WindowInsets(
        start = left,
        top = top,
        end = right,
        bottom = bottom
    )

    private fun checkDarkTheme(configuration: Configuration = resources.configuration) {
        val isDarkTheme = (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        ThemeManager.changeTheme(if (isDarkTheme) CoreTheme.DARK else CoreTheme.LIGHT)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = !isDarkTheme
        insetsController.isAppearanceLightNavigationBars = !isDarkTheme
    }

}