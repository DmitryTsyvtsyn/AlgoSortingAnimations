package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.view.ViewGroup

abstract class AbstractMarginLP<T : ViewGroup.MarginLayoutParams, R>(private val params: T, match: Int, wrap: Int) : AbstractLP<T, R>(params, match, wrap) {

    fun marginTop(dp: Int) = with(params.apply { topMargin = dp })
    fun marginStart(dp: Int) = with(params.apply { marginStart = dp })
    fun marginEnd(dp: Int) = with(params.apply { marginEnd = dp })
    fun marginBottom(dp: Int) = with(params.apply { bottomMargin = dp })
    fun marginVertical(dp: Int) = with(params.apply {
        topMargin = dp
        bottomMargin = dp
    })
    fun marginHorizontal(dp: Int) = with(params.apply {
        marginStart = dp
        marginEnd = dp
    })
    fun margins(dp: Int) = with(params.apply {
        marginStart = dp
        marginEnd = dp
        topMargin = dp
        bottomMargin = dp
    })
    fun margins(startDp: Int, topDp: Int, endDp: Int, bottomDp: Int) = with(params.apply {
        marginStart = startDp
        marginEnd = endDp
        topMargin = topDp
        bottomMargin = bottomDp
    })
}