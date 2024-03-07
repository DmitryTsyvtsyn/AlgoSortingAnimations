package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator.Navigator
import io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator.OnBackPressedDispatcher
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreFrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params.viewGroupLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.viewmodel.ViewModelProvider
import io.github.dmitrytsyvtsyn.algosortinganimations.main.SortingAlgorithmMainFragment
import kotlin.properties.Delegates

class MainActivity : Activity() {

    private var navigator by Delegates.notNull<Navigator>()
    private var viewModelProvider by Delegates.notNull<ViewModelProvider>()

    private val backPressedDispatcher = OnBackPressedDispatcher {
        try {
            super.onBackPressed()
        } catch (e: IllegalStateException) {
            // Calling onBackPressed() on an Activity with its state saved can cause an
            // error on devices on API levels before 26
            if (e.message != "Can not perform this action after onSaveInstanceState") {
                throw e
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TypefaceManager.setAssets(assets)

        val fragmentContainerView = CoreFrameLayout(this)
        fragmentContainerView.layoutParams(viewGroupLayoutParams().match())
        setContentView(fragmentContainerView)

        if (BuildCompat.isAtLeastT()) {
            backPressedDispatcher.changeOnBackInvokedDispatcher(onBackInvokedDispatcher)
        }

        viewModelProvider = lastNonConfigurationInstance as? ViewModelProvider ?: ViewModelProvider()
        navigator = Navigator(fragmentContainerView, viewModelProvider, backPressedDispatcher)
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

    override fun onBackPressed() { backPressedDispatcher.onBackPressed() }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        checkDarkTheme(newConfig)
    }

    override fun onRetainNonConfigurationInstance() = viewModelProvider

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