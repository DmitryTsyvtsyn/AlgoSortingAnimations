package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.widget.SeekBar
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import kotlin.math.roundToInt

class CoreSeekBar(ctx: Context) : SeekBar(ctx), ThemeManager.ThemeManagerListener {

    init {
        padding(0)
    }

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
        thumb = null
        background = null

        val layerDrawables = LayerDrawable(arrayOf(
            GradientDrawable().apply {
                setColor(theme.colors[ColorAttributes.selectableBackgroundColor])
            },
            ClipDrawable(
                GradientDrawable().apply {
                    setColor(theme.colors[ColorAttributes.primaryColor],)
                    setSize(-1, measuredHeight)
                },
                Gravity.START,
                ClipDrawable.HORIZONTAL
            )
        ))

        layerDrawables.setId(0, android.R.id.background)
        layerDrawables.setId(1, android.R.id.progress)

        progressDrawable = layerDrawables
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