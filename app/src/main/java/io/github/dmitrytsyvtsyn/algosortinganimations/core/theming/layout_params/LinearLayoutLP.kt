package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.widget.LinearLayout

fun linearLayoutParams(): AbstractLinearLayoutLP = LinearLayoutLP()

abstract class AbstractLinearLayoutLP(
    params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(match, wrap)
) : AbstractMarginLP<LinearLayout.LayoutParams, AbstractLinearLayoutLP>(params, match, wrap) {

    override fun applyParams(block: AbstractLinearLayoutLP.() -> Unit) = apply(block)

    fun weight(weight: Float) = apply {
        params.weight = weight
    }

    fun gravity(gravity: Int) = apply {
        params.gravity = gravity
    }

    companion object {
        private const val match = LinearLayout.LayoutParams.MATCH_PARENT
        private const val wrap = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}

private class LinearLayoutLP : AbstractLinearLayoutLP()