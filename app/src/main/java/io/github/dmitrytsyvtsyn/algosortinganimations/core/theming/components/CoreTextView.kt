package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.fontSize
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager

open class CoreTextView @JvmOverloads constructor(
    ctx: Context,
    textColor: ColorAttributes = ColorAttributes.primaryTextColor,
    textStyle: TypefaceAttribute = TypefaceAttribute.Body1
): AppCompatTextView(ctx) {

    protected val theme = CoreTheme.LIGHT

    init {
        includeFontPadding = false

        val (fontFamily, textSize) = theme.textStyle[textStyle]
        typeface = TypefaceManager.typeface(fontFamily)
        fontSize(textSize)
        setTextColor(theme.colors[textColor])
    }

}