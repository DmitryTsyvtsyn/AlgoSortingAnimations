package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.widget.FrameLayout

fun frameLayoutParams(): AbstractFrameLayoutLP = FrameLayoutLP()

abstract class AbstractFrameLayoutLP(
    params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(match, wrap)
) : AbstractMarginLP<FrameLayout.LayoutParams, AbstractFrameLayoutLP>(params, match, wrap) {

    override fun applyParams(block: AbstractFrameLayoutLP.() -> Unit) = apply(block)

    fun gravity(gravity: Int) = applyParams {
        params.gravity = gravity
    }

    companion object {
        private const val match = FrameLayout.LayoutParams.MATCH_PARENT
        private const val wrap = FrameLayout.LayoutParams.WRAP_CONTENT
    }
}

private class FrameLayoutLP : AbstractFrameLayoutLP()