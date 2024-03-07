package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params.frameLayoutParams

class ToolbarView(ctx: Context) : CoreFrameLayout(ctx) {

    private val menuButtonSize = 48
    private val menuButtonMarginStart = 4

    private val titleView = CoreTextView(context, textStyle = TypefaceAttribute.Title1)
    private val backButtonView = CoreImageButtonView(context)
    private val menuButtonView = CoreImageButtonView(context)

    init {
        elevation = context.dp(2f)

        titleView.maxLines = 2
        titleView.ellipsize = TextUtils.TruncateAt.END
        val titleMargins = context.dp(menuButtonSize + menuButtonMarginStart + 8)
        titleView.layoutParams(frameLayoutParams().wrap().gravity(Gravity.CENTER).marginStart(titleMargins).marginEnd(titleMargins))
        addView(titleView)

        backButtonView.isVisible = false
        backButtonView.layoutParams(
            frameLayoutParams()
            .width(context.dp(menuButtonSize)).height(context.dp(menuButtonSize))
            .gravity(Gravity.START or Gravity.CENTER_VERTICAL)
            .marginStart(context.dp(menuButtonMarginStart)))
        backButtonView.scaleType = ImageView.ScaleType.CENTER
        backButtonView.setImageResource(R.drawable.ic_back)
        backButtonView.isClickable = true
        backButtonView.isFocusable = true
        addView(backButtonView)

        menuButtonView.layoutParams(
            frameLayoutParams()
            .width(context.dp(menuButtonSize)).height(context.dp(menuButtonSize))
            .gravity(Gravity.END or Gravity.CENTER_VERTICAL)
            .marginEnd(context.dp(menuButtonMarginStart)))
        menuButtonView.isVisible = false
        menuButtonView.scaleType = ImageView.ScaleType.CENTER
        menuButtonView.setImageResource(R.drawable.ic_settings)
        menuButtonView.isClickable = true
        menuButtonView.isFocusable = true
        addView(menuButtonView)
    }

    fun changeTitle(title: String) {
        titleView.text = title
    }

    fun changeBackButtonListener(listener: OnClickListener) {
        backButtonView.setOnClickListener(listener)
        backButtonView.isVisible = true
    }

    fun changeMenuButtonDrawable(@DrawableRes resource: Int) {
        menuButtonView.setImageResource(resource)
        menuButtonView.isVisible = true
    }

    fun changeMenuClickListener(listener: OnClickListener) {
        menuButtonView.setOnClickListener(listener)
        menuButtonView.isVisible = true
    }

}