package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.view.ViewGroup

fun viewGroupLayoutParams(): AbstractViewGroupLP = ViewGroupLP()

abstract class AbstractViewGroupLP(
    params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(wrap, wrap)
) : AbstractLP<ViewGroup.LayoutParams, AbstractViewGroupLP>(params, match, wrap) {

    override fun applyParams(block: AbstractViewGroupLP.() -> Unit) = apply(block)

    companion object {
        private const val match = ViewGroup.LayoutParams.MATCH_PARENT
        private const val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}

private class ViewGroupLP : AbstractViewGroupLP()