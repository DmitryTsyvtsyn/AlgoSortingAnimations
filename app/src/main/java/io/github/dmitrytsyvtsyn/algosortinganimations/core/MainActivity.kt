package io.github.dmitrytsyvtsyn.algosortinganimations.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import io.github.dmitrytsyvtsyn.algosortinganimations.main.SortingAlgorithmViewModel
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreFrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.frameLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.viewGroupLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager
import io.github.dmitrytsyvtsyn.algosortinganimations.main.SortingAlgorithmMainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TypefaceManager.setAssets(assets)

        val fragmentContainerView = CoreFrameLayout(this)
        fragmentContainerView.layoutParams(viewGroupLayoutParams().match())
        setContentView(fragmentContainerView)

        val fragment = SortingAlgorithmMainFragment(fragmentContainerView, SortingAlgorithmViewModel())
        fragment.layoutParams(frameLayoutParams().match())
        fragmentContainerView.addView(fragment)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainerView) { _, windowInsets ->
            val systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            fragmentContainerView.padding(top = systemBarsInsets.top, bottom = systemBarsInsets.bottom)

            WindowInsetsCompat.CONSUMED
        }

    }

}