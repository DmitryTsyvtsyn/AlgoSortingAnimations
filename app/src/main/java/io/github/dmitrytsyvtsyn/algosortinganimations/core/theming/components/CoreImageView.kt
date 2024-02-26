package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes

open class CoreImageView @JvmOverloads constructor(
    ctx: Context,
    tintColor: ColorAttributes = ColorAttributes.primaryTextColor
): AppCompatImageView(ctx) {

    protected val theme = io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme.LIGHT

    init {
        setColorFilter(theme.colors[tintColor])
    }

}