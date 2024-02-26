package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.Gravity
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute

open class CoreButton @JvmOverloads constructor(
    ctx: Context,
    textColor: ColorAttributes = ColorAttributes.colorOnPrimary,
    textStyle: TypefaceAttribute = TypefaceAttribute.Caption1,
    backgroundColor: ColorAttributes = ColorAttributes.primaryColor,
    rippleColor: ColorAttributes = ColorAttributes.primaryDarkColor,
    shape: ShapeAttribute = ShapeAttribute.medium,
    shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.None()
) : CoreTextView(ctx, textColor = textColor, textStyle = textStyle) {

    init {
        isClickable = true
        isFocusable = true
        gravity = Gravity.CENTER
        padding(horizontal = context.dp(16), vertical = context.dp(8))

        val stateListDrawable = StateListDrawable()

        val disabledBackgroundDrawable = GradientDrawable()
        disabledBackgroundDrawable.setColor(theme.colors[ColorAttributes.disabledBackgroundColor])
        disabledBackgroundDrawable.cornerRadii = shapeTreatmentStrategy.floatArrayOf(context.dp(theme.shapeStyle[shape]))
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disabledBackgroundDrawable)

        val normalBackgroundDrawable = GradientDrawable()
        normalBackgroundDrawable.setColor(theme.colors[backgroundColor])
        normalBackgroundDrawable.cornerRadii = shapeTreatmentStrategy.floatArrayOf(context.dp(theme.shapeStyle[shape]))
        stateListDrawable.addState(StateSet.WILD_CARD, normalBackgroundDrawable)

        background = RippleDrawable(
            ColorStateList.valueOf(theme.colors[rippleColor]),
            stateListDrawable,
            null
        )
    }

}