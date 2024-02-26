package io.github.dmitrytsyvtsyn.algosortinganimations.main

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import io.github.dmitrytsyvtsyn.algosortinganimations.R
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.colors.ColorAttributes
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreButton
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreFrameLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreImageButtonView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreLinearLayout
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreSeekBar
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.CoreTextView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.components.ToolbarView
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.dp
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.frameLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.layoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.linearLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.padding
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.extensions.viewGroupLayoutParams
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape.ShapeTreatmentStrategy
import io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.typeface.TypefaceAttribute
import io.github.dmitrytsyvtsyn.algosortinganimations.main.customview.SortingAlgorithmView
import io.github.dmitrytsyvtsyn.algosortinganimations.selection.SortingAlgorithmSelectionFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class SortingAlgorithmMainFragment(
    parent: ViewGroup,
    viewModel: SortingAlgorithmViewModel
) : CoreLinearLayout(parent.context) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main.immediate)

    init {
        orientation = VERTICAL

        val toolbarView = ToolbarView(context)
        toolbarView.layoutParams(frameLayoutParams().matchWidth().height(context.dp(48)))
        toolbarView.changeMenuButtonDrawable(R.drawable.ic_settings)
        toolbarView.changeMenuClickListener {
            parent.addView(SortingAlgorithmSelectionFragment(parent, viewModel))
        }
        addView(toolbarView)

        val backgroundColor = 0xffe7e7e7.toInt()
        val sortingContentView = LinearLayout(context)
        sortingContentView.orientation = VERTICAL
        sortingContentView.setBackgroundColor(backgroundColor)
        sortingContentView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(16)))
        addView(sortingContentView)

        val sortingAlgorithmView = SortingAlgorithmView(context)
        sortingAlgorithmView.layoutParams(linearLayoutParams().matchWidth().wrapHeight())
        sortingContentView.addView(sortingAlgorithmView)

        val stepStatusView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Caption1
        )
        val lineNumber = 3
        stepStatusView.setLines(lineNumber)
        stepStatusView.minLines = lineNumber
        stepStatusView.maxLines = lineNumber
        stepStatusView.ellipsize = TextUtils.TruncateAt.END
        stepStatusView.padding(context.dp(16))
        stepStatusView.layoutParams(linearLayoutParams().matchWidth().wrapHeight())
        sortingContentView.addView(stepStatusView)

        val progressView = CoreSeekBar(context)
        progressView.changeProgressListener(sortingAlgorithmView::changeAnimationProgress)
        progressView.layoutParams(linearLayoutParams().matchWidth().height(context.dp(40)))
        sortingContentView.addView(progressView)

        val controlButtonsView = CoreLinearLayout(context, backgroundColor = ColorAttributes.transparent)
        controlButtonsView.orientation = HORIZONTAL
        controlButtonsView.padding(bottom = context.dp(8))
        sortingContentView.addView(controlButtonsView)

        val iconSize = context.dp(40)
        val playPauseButtonView = CoreImageButtonView(context, tintColor = ColorAttributes.primaryColor)
        playPauseButtonView.padding(context.dp(4))
        playPauseButtonView.layoutParams(linearLayoutParams()
            .width(iconSize).height(iconSize)
            .marginStart(context.dp(4)))
        playPauseButtonView.setImageResource(R.drawable.ic_play)
        controlButtonsView.addView(playPauseButtonView)

        val previousButtonView = CoreImageButtonView(context, tintColor = ColorAttributes.primaryColor)
        previousButtonView.padding(context.dp(4))
        previousButtonView.layoutParams(linearLayoutParams()
            .width(iconSize).height(iconSize)
            .marginStart(context.dp(4)))
        previousButtonView.setImageResource(R.drawable.ic_previous)
        previousButtonView.setOnClickListener {
            sortingAlgorithmView.changeAnimationProgress(-1)
        }
        controlButtonsView.addView(previousButtonView)

        val nextButtonView = CoreImageButtonView(context, tintColor = ColorAttributes.primaryColor)
        nextButtonView.padding(context.dp(4))
        nextButtonView.layoutParams(linearLayoutParams()
            .width(iconSize).height(iconSize)
            .marginStart(context.dp(4)))
        nextButtonView.setImageResource(R.drawable.ic_next)
        nextButtonView.setOnClickListener {
            sortingAlgorithmView.changeAnimationProgress(1)
        }
        controlButtonsView.addView(nextButtonView)

        controlButtonsView.addView(View(context).apply {
            layoutParams(linearLayoutParams().wrapWidth().height(context.dp(1)).weight(1f))
        })

        val randomArrayButton = CoreButton(
            ctx = context,
            shapeTreatmentStrategy = ShapeTreatmentStrategy.None()
        )
        randomArrayButton.setText(R.string.random_array)
        randomArrayButton.setOnClickListener { viewModel.generateRandomArray() }
        randomArrayButton.layoutParams(linearLayoutParams().wrap()
            .marginStart(context.dp(12))
            .marginEnd(context.dp(16)))
        controlButtonsView.addView(randomArrayButton)

        sortingAlgorithmView.changeStepListener { index, step ->
            val title = step.titleResource
            if (title.isNotBlank()) {
                stepStatusView.text = context.getString(R.string.number_step, index + 1, title)
            }
            progressView.changeProgress(sortingAlgorithmView.animationProgress())
        }

        val buttonsView = CoreLinearLayout(context)
        buttonsView.orientation = HORIZONTAL
        buttonsView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(24)).marginStart(context.dp(16))
            .marginEnd(context.dp(16)))
        addView(buttonsView)

        val someDetailInformationView = CoreLinearLayout(context)
        someDetailInformationView.orientation = VERTICAL
        someDetailInformationView.padding(top = context.dp(16))
        someDetailInformationView.layoutParams(viewGroupLayoutParams().matchWidth().wrapHeight())
        addView(ScrollView(context).apply {
            addView(someDetailInformationView)
            layoutParams(linearLayoutParams().matchWidth().wrapHeight())
        })

        val worstTimeComplexityView = ComplexityView(context)
        worstTimeComplexityView.changeTitle(context.getString(R.string.worst_case_time))
        worstTimeComplexityView.layoutParams(linearLayoutParams().matchWidth().wrapHeight())
        someDetailInformationView.addView(worstTimeComplexityView)

        val bestTimeComplexityView = ComplexityView(context)
        bestTimeComplexityView.changeTitle(context.getString(R.string.best_case_time))
        bestTimeComplexityView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(12)))
        someDetailInformationView.addView(bestTimeComplexityView)

        val averageTimeComplexityView = ComplexityView(context)
        averageTimeComplexityView.changeTitle(context.getString(R.string.average_time))
        averageTimeComplexityView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(12)))
        someDetailInformationView.addView(averageTimeComplexityView)

        val worstSpaceComplexityView = ComplexityView(context)
        worstSpaceComplexityView.changeTitle(context.getString(R.string.worst_case_space))
        worstSpaceComplexityView.layoutParams(linearLayoutParams().matchWidth().wrapHeight()
            .marginTop(context.dp(12)))
        someDetailInformationView.addView(worstSpaceComplexityView)

        coroutineScope.launch {
            viewModel.algorithmDetailState.collect { state ->

                with(state.selectedAlgorithm) {
                    toolbarView.changeTitle(context.getString(title))

                    worstTimeComplexityView.changeDescription(worstTimeComplexity)
                    bestTimeComplexityView.changeDescription(bestTimeComplexity)
                    averageTimeComplexityView.changeDescription(averageTimeComplexity)
                    worstSpaceComplexityView.changeDescription(worstSpaceComplexity)
                }

                sortingAlgorithmView.changeArray(state.elements)
                sortingAlgorithmView.changeAnimationSteps(state.steps(resources))
            }
        }

        coroutineScope.launch {
            viewModel.buttonState.collect { state ->

                val (imageResource, clickListener) = when (state) {
                    SortingAnimationButtonState.PAUSED -> R.drawable.ic_play to OnClickListener {
                        sortingAlgorithmView.startAnimation()
                        viewModel.toggleAnimation()
                    }
                    SortingAnimationButtonState.RUNNING -> R.drawable.ic_pause to OnClickListener {
                        sortingAlgorithmView.pauseAnimation()
                        viewModel.toggleAnimation()
                    }
                }

                playPauseButtonView.setImageResource(imageResource)
                playPauseButtonView.setOnClickListener(clickListener)
            }
        }

    }

    class ComplexityView(ctx: Context) : CoreFrameLayout(ctx) {

        private val complexityTitleView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Body1
        )

        private val complexityDescriptionView = CoreTextView(
            ctx = context,
            textStyle = TypefaceAttribute.Title2
        )

        init {
            padding(start = context.dp(16), end = context.dp(16))

            complexityTitleView.layoutParams(frameLayoutParams().wrap()
                .gravity(Gravity.START or Gravity.CENTER_VERTICAL))
            addView(complexityTitleView)

            complexityDescriptionView.gravity = Gravity.CENTER_HORIZONTAL
            complexityDescriptionView.layoutParams(frameLayoutParams()
                .wrap()
                .gravity(Gravity.END or Gravity.CENTER_VERTICAL))
            addView(complexityDescriptionView)
        }

        fun changeTitle(title: String) {
            complexityTitleView.text = title
        }

        fun changeDescription(description: String) {
            complexityDescriptionView.text = description
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

}