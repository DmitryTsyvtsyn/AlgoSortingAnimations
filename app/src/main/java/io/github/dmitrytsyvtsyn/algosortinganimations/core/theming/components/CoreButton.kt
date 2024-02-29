package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.Gravity
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
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
    private val backgroundColor: ColorAttributes = ColorAttributes.primaryColor,
    private val rippleColor: ColorAttributes = ColorAttributes.primaryDarkColor,
    private val shape: ShapeAttribute = ShapeAttribute.medium,
    private val shapeTreatmentStrategy: ShapeTreatmentStrategy = ShapeTreatmentStrategy.None()
) : CoreTextView(ctx, textColor = textColor, textStyle = textStyle), ThemeManager.ThemeManagerListener {

    init {
        isClickable = true
        isFocusable = true
        gravity = Gravity.CENTER
        padding(horizontal = context.dp(16), vertical = context.dp(8))
    }

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        super.onThemeChanged(insets, theme)

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