package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.widget.LinearLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.CoreColors
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy

open class CoreLinearLayout @JvmOverloads constructor(
    ctx: Context,
    private val backgroundColor: ColorAttributes = ColorAttributes.primaryBackgroundColor,
    private val shape: ShapeAttribute = ShapeAttribute.medium,
    private val shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.None(),
    private val rippleColor: ColorAttributes? = null,
): LinearLayout(ctx), ThemeManager.ThemeManagerListener {

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        val gradientDrawable = GradientDrawable()
        val radius = shapeTreatmentStrategy.floatArrayOf(context.dp(theme.shapeStyle[shape]))
        gradientDrawable.cornerRadii = radius
        gradientDrawable.setColor(theme.colors[backgroundColor])

        background = if (rippleColor != null) {
            val maskDrawable = if (backgroundColor == ColorAttributes.transparent) {
                GradientDrawable().apply {
                    cornerRadii = radius
                    setColor(CoreColors.white)
                }
            } else {
                null
            }
            RippleDrawable(
                ColorStateList.valueOf(theme.colors[rippleColor]),
                gradientDrawable,
                maskDrawable
            )
        } else {
            gradientDrawable
        }
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