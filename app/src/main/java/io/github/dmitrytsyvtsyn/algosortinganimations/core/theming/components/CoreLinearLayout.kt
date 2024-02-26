package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.widget.LinearLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.CoreColors
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy

open class CoreLinearLayout @JvmOverloads constructor(
    ctx: Context,
    backgroundColor: ColorAttributes = ColorAttributes.primaryBackgroundColor,
    shape: ShapeAttribute = ShapeAttribute.medium,
    shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.None(),
    rippleColor: ColorAttributes? = null,
): LinearLayout(ctx) {

    private val theme = io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme.LIGHT

    init {
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

}