package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.widget.ImageView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes

open class CoreImageView @JvmOverloads constructor(
    ctx: Context,
    private val tintColor: ColorAttributes = ColorAttributes.primaryTextColor
): ImageView(ctx), ThemeManager.ThemeManagerListener {

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        setColorFilter(theme.colors[tintColor])
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeManager.addThemeListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeThemeListener(this)
    }

}