package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.FrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy

open class CoreFrameLayout @JvmOverloads constructor(
    ctx: Context,
    private val backgroundColor: ColorAttributes = ColorAttributes.primaryBackgroundColor,
    private val shape: ShapeAttribute = ShapeAttribute.medium,
    private val shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.None()
): FrameLayout(ctx), ThemeManager.ThemeManagerListener {

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadii = shapeTreatmentStrategy.floatArrayOf(context.dp(theme.shapeStyle[shape]))
        gradientDrawable.setColor(theme.colors[backgroundColor])
        background = gradientDrawable
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