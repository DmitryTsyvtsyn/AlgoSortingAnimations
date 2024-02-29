package io.github.dmitrytsyvtsyn.algosortinganimations.main.views

import android.content.Context
import android.view.Gravity
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreFrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreTextView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.frameLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute

class ComplexityView(ctx: Context) : CoreFrameLayout(ctx) {

    private val complexityTitleView = CoreTextView(
        ctx = context,
        textStyle = TypefaceAttribute.Body1
    )

    private val complexityDescriptionView = CoreTextView(
        ctx = context,
        textStyle = TypefaceAttribute.Title2
    )

    init {
        padding(start = context.dp(16), end = context.dp(16))

        complexityTitleView.layoutParams(
            frameLayoutParams().wrap()
            .gravity(Gravity.START or Gravity.CENTER_VERTICAL))
        addView(complexityTitleView)

        complexityDescriptionView.gravity = Gravity.CENTER_HORIZONTAL
        complexityDescriptionView.layoutParams(
            frameLayoutParams()
            .wrap()
            .gravity(Gravity.END or Gravity.CENTER_VERTICAL))
        addView(complexityDescriptionView)
    }

    fun changeTitle(title: String) {
        complexityTitleView.text = title
    }

    fun changeDescription(description: String) {
        complexityDescriptionView.text = description
    }

}