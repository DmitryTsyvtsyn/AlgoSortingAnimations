package io.github.dmitrytsyvtsyn.algosortinganimations.selection

import android.annotation.SuppressLint
import android.view.Gravity
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.BaseFragmentParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.CoreTheme
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.ThemeManager
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreLinearLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.ToolbarView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.frameLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.linearLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.main.viewmodel.SortingAlgorithmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class SortingAlgorithmSelectionFragment(params: BaseFragmentParams) : CoreLinearLayout(params.context) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val navigator = params.navigator
    private val viewModel = params.viewModelProvider.provide(SortingAlgorithmViewModel::class.java)

    init {
        orientation = VERTICAL

        val toolbarView = ToolbarView(context)
        toolbarView.layoutParams(frameLayoutParams().matchWidth().height(context.dp(48)))
        toolbarView.changeTitle(context.getString(R.string.sorting_algorithms))
        toolbarView.changeBackButtonListener { navigator.navigateBack() }
        addView(toolbarView)

        coroutineScope.launch {
            viewModel.algorithmListState.collect { algorithms ->
                algorithms.forEachIndexed { index, algorithm ->
                    val sortingListView = CoreButton(
                        ctx = context,
                        textColor = ColorAttributes.primaryTextColor,
                        textStyle = TypefaceAttribute.Body1,
                        backgroundColor = ColorAttributes.primaryBackgroundColor,
                        rippleColor = ColorAttributes.selectableBackgroundColor
                    )
                    sortingListView.gravity = Gravity.START
                    sortingListView.padding(context.dp(16))
                    sortingListView.setText(algorithm.title)

                    var layoutParams = linearLayoutParams().matchWidth().wrapHeight()
                    if (index == 0) {
                        layoutParams = layoutParams.marginTop(context.dp(16))
                    }
                    sortingListView.layoutParams(layoutParams)

                    sortingListView.setOnClickListener {
                        viewModel.changeSortingAlgorithm(algorithm)
                        navigator.navigateBack()
                    }
                    addView(sortingListView)
                }
            }
        }

    }

    override fun onThemeChanged(insets: ThemeManager.WindowInsets, theme: CoreTheme) {
        super.onThemeChanged(insets, theme)

        padding(top = insets.top, bottom = insets.bottom)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

}