package io.github.dmitrytsyvtsyn.algosortinganimations.selection

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
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
class SortingAlgorithmSelectionFragment(
    private val parent: ViewGroup,
    viewModel: SortingAlgorithmViewModel
) : CoreLinearLayout(parent.context) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navigateBack()
        }
    }

    init {
        orientation = VERTICAL

        val toolbarView = ToolbarView(context)
        toolbarView.layoutParams(frameLayoutParams().matchWidth().height(context.dp(48)))
        toolbarView.changeTitle("Sorting algorithms")
        toolbarView.changeBackButtonListener { navigateBack() }
        addView(toolbarView)

        (context as ComponentActivity).onBackPressedDispatcher.addCallback(callback)

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

                    var params = linearLayoutParams().matchWidth().wrapHeight()
                    if (index == 0) {
                        params = params.marginTop(context.dp(16))
                    }
                    sortingListView.layoutParams(params)

                    sortingListView.setOnClickListener {
                        viewModel.changeSortingAlgorithm(algorithm)
                        navigateBack()
                    }
                    addView(sortingListView)
                }
            }
        }

    }

    private fun navigateBack() {
        callback.isEnabled = false
        parent.removeView(this)
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