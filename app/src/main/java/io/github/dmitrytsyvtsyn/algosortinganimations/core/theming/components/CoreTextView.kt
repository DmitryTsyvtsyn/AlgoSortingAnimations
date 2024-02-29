package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.fontSize
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceManager

open class CoreTextView @JvmOverloads constructor(
    ctx: Context,
    private val textColor: ColorAttributes = ColorAttributes.primaryTextColor,
    private val textStyle: TypefaceAttribute = TypefaceAttribute.Body1
): AppCompatTextView(ctx), ThemeManager.ThemeManagerListener {

    init {
        includeFontPadding = false
    }

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        val (fontFamily, textSize) = theme.textStyle[textStyle]
        typeface = TypefaceManager.typeface(fontFamily)
        fontSize(textSize)
        setTextColor(theme.colors[textColor])
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