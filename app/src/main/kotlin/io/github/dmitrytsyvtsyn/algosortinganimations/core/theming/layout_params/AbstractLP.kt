package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.view.ViewGroup

abstract class AbstractLP<T : ViewGroup.LayoutParams, R : AbstractLP<T, R>>(
    protected val params: T,
    private val match: Int,
    private val wrap: Int
) {

    protected abstract fun applyParams(block: R.() -> Unit): R

    fun matchWidth() = applyParams {
        params.width = match
    }

    fun matchHeight() = applyParams {
        params.height = match
    }

    fun match() = applyParams {
        params.width = match
        params.height = match
    }

    fun wrapWidth() = applyParams {
        params.width = wrap
    }

    fun wrapHeight() = applyParams {
        params.height = wrap
    }

    fun wrap() = applyParams {
        params.width = wrap
        params.height = wrap
    }

    fun width(dp: Int) = applyParams {
        params.width = dp
    }

    fun height(dp: Int) = applyParams {
        params.height = dp
    }

    fun build(): T = params

}