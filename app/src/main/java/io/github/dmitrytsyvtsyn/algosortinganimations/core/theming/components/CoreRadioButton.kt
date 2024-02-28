package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatRadioButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.fontSize

@SuppressLint("ViewConstructor")
class CoreRadioButton(
    ctx: Context,
    textStyle: TypefaceAttribute = TypefaceAttribute.Body1
) : AppCompatRadioButton(ctx) {

    private val theme = CoreTheme.LIGHT

    init {
        includeFontPadding = false

        val (fontFamily, textSize) = theme.textStyle[textStyle]
        typeface = TypefaceManager.typeface(fontFamily)
        fontSize(textSize)

        setTextColor(theme.colors[ColorAttributes.primaryTextColor])
        buttonTintList = ColorStateList.valueOf(theme.colors[ColorAttributes.primaryColor])
    }

}