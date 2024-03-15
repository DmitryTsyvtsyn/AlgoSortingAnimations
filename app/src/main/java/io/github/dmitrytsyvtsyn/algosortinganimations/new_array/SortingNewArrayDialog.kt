package io.github.dmitrytsyvtsyn.algosortinganimations.new_array

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.core.view.forEach
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.navigator.BaseParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.CoreColors
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreFrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreImageButtonView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreLinearLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreRadioButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreTextView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params.frameLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.layout_params.linearLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.main.viewmodel.SortingAlgorithmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class SortingNewArrayDialog(params: BaseParams) : FrameLayout(params.context) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val navigator = params.navigator
    private val parentViewModel = params.viewModelProvider.provide(SortingAlgorithmViewModel::class.java)
    private val viewModel = params.viewModelProvider.provide(SortingNewArrayViewModel::class.java) {
        SortingNewArrayViewModel(array = parentViewModel.sortingArray)
    }

    private val IntArray.string
        get() = joinToString(prefix = "[  ", postfix = "  ]", separator = "   ") { "$it" }

    init {
        setBackgroundColor(CoreColors.grayTranslucent)
        setOnClickListener { viewModel.navigateBack() }

        val marginMedium = context.dp(16)

        val contentView = CoreLinearLayout(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        contentView.isClickable = true
        contentView.orientation = LinearLayout.VERTICAL
        contentView.elevation = context.dp(8f)
        contentView.padding(bottom = marginMedium)
        contentView.layoutParams(frameLayoutParams().matchWidth().wrapHeight()
            .margins(context.dp(24))
            .gravity(Gravity.CENTER))
        addView(contentView)

        val titleContentView = CoreFrameLayout(
            ctx = context,
            backgroundColor = ColorAttributes.transparent
        )
        titleContentView.layoutParams(linearLayoutParams().matchWidth().wrapHeight())
        contentView.addView(titleContentView)

        val titleView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Title1
        )
        titleView.setText(R.string.new_array)
        titleView.layoutParams(frameLayoutParams().wrap().gravity(Gravity.START)
            .marginStart(marginMedium).marginTop(marginMedium))
        titleContentView.addView(titleView)

        val size = context.dp(48)
        val closeView = CoreImageButtonView(
            ctx = context,
            shape = ShapeAttribute.medium,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.StartBottomTopEndRounded()
        )
        closeView.setOnClickListener { viewModel.navigateBack() }
        closeView.padding(context.dp(12))
        closeView.setImageResource(R.drawable.ic_close)
        closeView.layoutParams(frameLayoutParams().width(size).height(size).gravity(Gravity.END))
        titleContentView.addView(closeView)

        val arrayExampleView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Title2
        )
        arrayExampleView.gravity = Gravity.CENTER
        arrayExampleView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(24))
            .marginStart(marginMedium)
            .marginEnd(marginMedium))
        contentView.addView(arrayExampleView)

        val arraySizesTitleView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Body1
        )
        arraySizesTitleView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(24))
            .marginStart(marginMedium)
            .marginEnd(marginMedium))
        arraySizesTitleView.setText(R.string.array_size)
        contentView.addView(arraySizesTitleView)

        val arraySizesContentView = RadioGroup(context)
        arraySizesContentView.orientation = LinearLayout.HORIZONTAL
        arraySizesContentView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(16))
            .marginStart(marginMedium)
            .marginEnd(marginMedium))
        contentView.addView(arraySizesContentView)

        val generateRandomArrayButton = CoreButton(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        generateRandomArrayButton.setText(R.string.random)
        generateRandomArrayButton.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(24))
            .marginStart(marginMedium)
            .marginEnd(marginMedium))
        generateRandomArrayButton.setOnClickListener {
            viewModel.generateRandomArray()
        }
        contentView.addView(generateRandomArrayButton)

        val generateSortedArrayButton = CoreButton(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        generateSortedArrayButton.setText(R.string.sorted)
        generateSortedArrayButton.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(12))
            .marginStart(marginMedium)
            .marginEnd(marginMedium))
        generateSortedArrayButton.setOnClickListener {
            viewModel.generateSortedArray()
        }
        contentView.addView(generateSortedArrayButton)

        val okButton = CoreButton(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        okButton.setText(R.string.ok)
        okButton.setOnClickListener {
            parentViewModel.changeSortingArray(viewModel.array)
            viewModel.navigateBack()
        }
        okButton.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(12))
            .marginStart(marginMedium)
            .marginEnd(marginMedium))
        contentView.addView(okButton)

        var cachedState = SortingNewArrayState()
        coroutineScope.launch {
            viewModel.state.collect {

                val state = it.difference(cachedState); cachedState = it

                if (state.hasChanged(SortingNewArrayState.arrayChanged)) {
                    arrayExampleView.text = state.array.string
                }

                if (state.hasChanged(SortingNewArrayState.sizesChanged)) {
                    arraySizesContentView.removeAllViews()
                    state.sizes.forEachIndexed { index, size ->
                        val button = CoreRadioButton(context)
                        button.text = size.toString()
                        button.tag = size
                        button.isChecked = size == state.size
                        button.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                viewModel.changeSize(size)
                            }
                        }

                        val margin = if (index > 0) context.dp(8) else 0
                        button.layoutParams(linearLayoutParams().wrap().marginStart(margin))

                        arraySizesContentView.addView(button)
                    }
                }

                if (state.hasChanged(SortingNewArrayState.sizeChanged)) {
                    arraySizesContentView.forEach { view ->
                        val checkedView = view as CoreRadioButton
                        checkedView.isChecked = checkedView.tag == state.size
                    }
                }

                if (state.hasChanged(SortingNewArrayState.backNavigatedChanged)) {
                    if (state.backNavigated) {
                        navigator.navigateBack()
                    }
                }

            }
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

}