package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.view.ViewGroup

private const val match = ViewGroup.LayoutParams.MATCH_PARENT
private const val wrap = ViewGroup.LayoutParams.WRAP_CONTENT

class ViewGroupLP(params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(wrap, wrap)) : AbstractLP<ViewGroup.LayoutParams, ViewGroupLP>(params, match, wrap) {

    override fun with(params: ViewGroup.LayoutParams): ViewGroupLP = ViewGroupLP(params)

}