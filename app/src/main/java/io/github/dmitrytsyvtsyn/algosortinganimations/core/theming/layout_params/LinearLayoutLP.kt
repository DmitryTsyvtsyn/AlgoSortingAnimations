package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params

import android.widget.LinearLayout

private const val match = LinearLayout.LayoutParams.MATCH_PARENT
private const val wrap = LinearLayout.LayoutParams.WRAP_CONTENT

class LinearLayoutLP(private var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(match, wrap)) : AbstractMarginLP<LinearLayout.LayoutParams, LinearLayoutLP>(params, match, wrap) {

    fun weight(w: Float) = LinearLayoutLP(params.apply { weight = w })
    fun gravity(grav: Int) = LinearLayoutLP(params.apply { gravity = grav })

    override fun with(params: LinearLayout.LayoutParams) = LinearLayoutLP(params)

}