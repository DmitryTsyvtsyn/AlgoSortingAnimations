package io.github.dmitrytsyvtsyn.algosortinganimations.main.dialogs

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreLinearLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreRadioButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreTextView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.frameLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.linearLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.main.viewmodel.SortingAlgorithmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class SortingNewArrayActionsDialog(
    private val parent: ViewGroup,
    parentViewModel: SortingAlgorithmViewModel,
    private val viewModel: SortingNewArrayActionsViewModel = SortingNewArrayActionsViewModel()
) : FrameLayout(parent.context) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val IntArray.string
        get() = joinToString(prefix = "[ ", postfix = " ]", separator = ", ") { "$it" }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navigateBack()
        }
    }

    init {
        setBackgroundColor(0x55_55_55_55)
        setOnClickListener {
            navigateBack()
        }

        val contentView = CoreLinearLayout(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        contentView.isClickable = true
        contentView.orientation = LinearLayout.VERTICAL
        contentView.elevation = context.dp(8f)
        contentView.padding(horizontal = context.dp(12), vertical = context.dp(16))
        contentView.layoutParams(frameLayoutParams().matchWidth().wrapHeight()
            .margins(context.dp(24))
            .gravity(Gravity.CENTER))
        addView(contentView)

        val arrayExampleView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Title2
        )
        arrayExampleView.gravity = Gravity.CENTER
        arrayExampleView.layoutParams(linearLayoutParams().matchWidth().wrapHeight())
        contentView.addView(arrayExampleView)

        val arraySizesView = RadioGroup(context)
        arraySizesView.orientation = LinearLayout.HORIZONTAL
        arraySizesView.layoutParams(linearLayoutParams().matchWidth().wrapHeight().marginTop(context.dp(24)))
        contentView.addView(arraySizesView)

        val generateRandomArrayButton = CoreButton(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        generateRandomArrayButton.setText(R.string.random_array)
        generateRandomArrayButton.layoutParams(linearLayoutParams().matchWidth().wrapHeight().marginTop(context.dp(24)))
        generateRandomArrayButton.setOnClickListener {
            viewModel.generateRandomArray()
        }
        contentView.addView(generateRandomArrayButton)

        val generateSortedArrayButton = CoreButton(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.AllRounded()
        )
        generateSortedArrayButton.setText(R.string.sorted_array)
        generateSortedArrayButton.layoutParams(linearLayoutParams().matchWidth().wrapHeight().marginTop(context.dp(12)))
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
            parentViewModel.changeSortingArray(viewModel.state.value.array)
            navigateBack()
        }
        okButton.layoutParams(linearLayoutParams().matchWidth().wrapHeight().marginTop(context.dp(12)))
        contentView.addView(okButton)

        (context as ComponentActivity).onBackPressedDispatcher.addCallback(callback)

        coroutineScope.launch {
            viewModel.state.collect { state ->
                arrayExampleView.text = state.array.string
                if (state.sizesChanged) {
                    arraySizesView.removeAllViews()
                    state.sizes.forEachIndexed { index, size ->
                        val button = CoreRadioButton(context)
                        button.text = size.toString()
                        button.isChecked = size == state.size
                        button.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                viewModel.changeSize(size)
                            }
                        }

                        val margin = if (index > 0) context.dp(8) else 0
                        button.layoutParams(linearLayoutParams().wrap().marginStart(margin))

                        arraySizesView.addView(button)
                    }
                } else {
                    state.sizes.forEachIndexed { index, size ->
                        val button = arraySizesView.getChildAt(index) as CoreRadioButton
                        button.isChecked = size == state.size
                    }
                }
            }
        }

    }

    private fun navigateBack() {
        callback.isEnabled = false
        parent.removeView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

}