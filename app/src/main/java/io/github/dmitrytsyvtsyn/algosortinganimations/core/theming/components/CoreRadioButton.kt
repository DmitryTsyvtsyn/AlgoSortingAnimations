package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.widget.RadioButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.fontSize

@SuppressLint("ViewConstructor")
class CoreRadioButton(
    ctx: Context,
    private val textStyle: TypefaceAttribute = TypefaceAttribute.Body1
) : RadioButton(ctx), ThemeManager.ThemeManagerListener {

    init {
        includeFontPadding = false
    }

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        val (fontFamily, textSize) = theme.textStyle[textStyle]
        typeface = TypefaceManager.typeface(fontFamily)
        fontSize(textSize)

        setTextColor(theme.colors[ColorAttributes.primaryTextColor])
        buttonTintList = ColorStateList.valueOf(theme.colors[ColorAttributes.primaryColor])
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