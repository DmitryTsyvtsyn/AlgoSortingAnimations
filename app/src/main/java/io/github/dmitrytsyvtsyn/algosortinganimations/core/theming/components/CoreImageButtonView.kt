package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.CoreColors
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy

class CoreImageButtonView @JvmOverloads constructor(
    ctx: Context,
    shape: ShapeAttribute = ShapeAttribute.maximum,
    shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded(),
    rippleColor: ColorAttributes = ColorAttributes.primaryColor,
    backgroundColor: ColorAttributes? = null,
    tintColor: ColorAttributes = ColorAttributes.primaryTextColor
): CoreImageView(ctx, tintColor = tintColor) {

    init {
        isClickable = true
        isFocusable = true

        val radius = context.dp(theme.shapeStyle[shape])
        val maskBackground = GradientDrawable().apply {
            setColor(CoreColors.white)
            cornerRadii = shapeTreatmentStrategy.floatArrayOf(radius)
        }

        val contentGradient = if (backgroundColor != null) {
            val drawable = GradientDrawable()
            drawable.cornerRadii = shapeTreatmentStrategy.floatArrayOf(radius)
            drawable.setColor(theme.colors[backgroundColor])
            drawable
        } else {
            null
        }

        background = RippleDrawable(
            ColorStateList.valueOf(theme.colors[rippleColor]),
            contentGradient,
            maskBackground
        )
    }

}