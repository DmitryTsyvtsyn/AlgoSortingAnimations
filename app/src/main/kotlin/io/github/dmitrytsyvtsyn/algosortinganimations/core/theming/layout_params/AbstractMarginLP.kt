package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.view.ViewGroup

abstract class AbstractMarginLP<T : ViewGroup.MarginLayoutParams, R : AbstractMarginLP<T, R>>(
    params: T, match: Int, wrap: Int
) : AbstractLP<T, R>(params, match, wrap) {

    fun marginTop(dp: Int) = applyParams {
        params.topMargin = dp
    }

    fun marginStart(dp: Int) = applyParams {
        params.marginStart = dp
    }

    fun marginEnd(dp: Int) = applyParams {
        params.marginEnd = dp
    }

    fun marginBottom(dp: Int) = applyParams {
        params.bottomMargin = dp
    }

    fun marginVertical(dp: Int) = applyParams {
        params.topMargin = dp
        params.bottomMargin = dp
    }

    fun marginHorizontal(dp: Int) = applyParams {
        params.marginStart = dp
        params.marginEnd = dp
    }

    fun margins(dp: Int) = applyParams {
        params.marginStart = dp
        params.marginEnd = dp
        params.topMargin = dp
        params.bottomMargin = dp
    }

    fun margins(startDp: Int, topDp: Int, endDp: Int, bottomDp: Int) = applyParams {
        params.marginStart = startDp
        params.marginEnd = endDp
        params.topMargin = topDp
        params.bottomMargin = bottomDp
    }

}