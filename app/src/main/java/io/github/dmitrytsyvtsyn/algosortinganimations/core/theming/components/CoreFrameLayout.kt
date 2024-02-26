package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.FrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy

open class CoreFrameLayout @JvmOverloads constructor(
    ctx: Context,
    backgroundColor: ColorAttributes = ColorAttributes.primaryBackgroundColor,
    shape: ShapeAttribute = ShapeAttribute.medium,
    shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.None()
): FrameLayout(ctx) {

    private val theme = io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme.LIGHT

    init {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadii = shapeTreatmentStrategy.floatArrayOf(context.dp(theme.shapeStyle[shape]))
        gradientDrawable.setColor(theme.colors[backgroundColor])
        background = gradientDrawable
    }

}