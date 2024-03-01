package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import kotlin.math.roundToInt

class CoreSeekBar(ctx: Context) : AppCompatSeekBar(ctx), ThemeManager.ThemeManagerListener {

    // values: 0f..1f
    fun changeProgress(progress: Float) {
        this.progress = (100 * progress).roundToInt()
    }

    fun changeProgressListener(listener: (Float) -> Unit) {
        setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    listener.invoke(progress / 100f)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        val color = theme.colors[ColorAttributes.primaryColor]

        thumb = GradientDrawable().apply {
            setColor(color)
            cornerRadius = context.dp(4f)
            setSize(context.dp(6), context.dp(16))
        }
        progressTintList = ColorStateList.valueOf(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            progressBackgroundTintBlendMode = BlendMode.COLOR
        }
        progressBackgroundTintList = ColorStateList.valueOf(theme.colors[ColorAttributes.selectableBackgroundColor])
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