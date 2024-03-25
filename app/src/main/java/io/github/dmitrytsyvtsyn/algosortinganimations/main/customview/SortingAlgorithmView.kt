package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.SystemClock
import android.text.TextPaint
import android.view.View
import kotlin.math.roundToInt

class SortingAlgorithmView(context: Context) : View(context) {

    private val Float.dpf
        get() = this * resources.displayMetrics.density

    private val Int.dpf
        get() = this * resources.displayMetrics.density

    private val Int.sp
        get() = this * resources.displayMetrics.scaledDensity

    private val isAnimationEnabled: Boolean
        get() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ValueAnimator.areAnimatorsEnabled()
        } else {
            true
        }

    private val tag = "SortingAlgorithmView"

    private var defaultStrokeWidth = 2.5f.dpf
    private var selectedStrokeWidth = 5f.dpf
    private var defaultStrokeColor = 0xff019701.toInt()
    private var selectedStrokeColor = 0xff015501.toInt()
    private var selectionAnimationDuration = 2_500L
    private var movingAnimationDuration = 5_000L
    private var isRepeatableAnimation = true
    private var repeatableAnimationDuration = 2_500L
    private var itemSize = 48.dpf
    private var itemMargin = 8.dpf
    private var itemRadius = 8.dpf
    private var defaultTextColor = 0xff000000.toInt()
    private var selectedTextColor = 0xffffffff.toInt()

    private val itemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val textPaint = TextPaint().apply {
        color = defaultTextColor
        textSize = 19.sp
    }
    private val textBounds = Rect()

    private val sortingItemsStates = mutableListOf<SortingItemState>()
    private var sortingArrayCopy = intArrayOf()

    private var stepIndex = -1
    private var stepListener: (Int, SortingAlgorithmStep) -> Unit = { _, _ -> }
    private val steps = mutableListOf<SortingAlgorithmStep>(SortingAlgorithmStep.End(""))

    private var animationState = SortingAnimationState.ANIMATION_STOPPED

    private var currentAnimationTime = 0L
    private var pausedAnimationDifference = 0L

    private var startOfView = 0f
    private var topOfView = 0f

    private val restartAnimationDelayedRunnable = Runnable { stopAnimation(); startAnimation() }

    private val emptyStepFinishAction = { false }
    private var stepFinishAction: () -> Boolean = emptyStepFinishAction

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val statesSize = sortingItemsStates.size

        var measuredWidth = (paddingStart + itemSize * statesSize + itemMargin * statesSize + paddingEnd).roundToInt()
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> {}
            MeasureSpec.EXACTLY -> {
                val exactWidth = MeasureSpec.getSize(widthMeasureSpec)
                if (exactWidth < measuredWidth) {
                    throw IllegalArgumentException("$tag requires the following width size -> $measuredWidth")
                }
                measuredWidth = exactWidth
            }
            MeasureSpec.AT_MOST -> {
                val atMostWidth = MeasureSpec.getSize(widthMeasureSpec)
                if (atMostWidth < measuredWidth) {
                    throw IllegalArgumentException("$tag requires the following width size -> $measuredWidth")
                }
            }
        }

        var measuredHeight = (paddingTop + itemSize * 3 + itemMargin * 4 + paddingBottom).roundToInt()
        when (MeasureSpec.getMode(measuredHeight)) {
            MeasureSpec.UNSPECIFIED -> {}
            MeasureSpec.EXACTLY -> {
                val exactHeight = MeasureSpec.getSize(heightMeasureSpec)
                if (exactHeight < measuredHeight) {
                    throw IllegalArgumentException("$tag requires the following height size -> $measuredHeight")
                }
                measuredHeight = exactHeight
            }

            MeasureSpec.AT_MOST -> {
                val atMostHeight = MeasureSpec.getSize(heightMeasureSpec)
                if (atMostHeight < measuredHeight) {
                    throw IllegalArgumentException("$tag requires the following height size -> $measuredHeight")
                }
            }
        }

        startOfView = measuredWidth / 2 - itemSize * sortingItemsStates.size / 2
        topOfView = measuredHeight / 2 - itemSize / 2

        resetItems(); handleStep(animate = false)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val animationTimeDifference = SystemClock.uptimeMillis() - currentAnimationTime
        pausedAnimationDifference = animationTimeDifference

        val animationTimeDifferenceFloat = animationTimeDifference.toFloat()
        val selectionAnimationFraction = (animationTimeDifferenceFloat / selectionAnimationDuration).coerceAtMost(1f)
        val movingAnimationFraction = (animationTimeDifferenceFloat / movingAnimationDuration).coerceAtMost(1f)

        var haveItemsAnimation = false

        var index = 0
        while (index < sortingItemsStates.size) {
            val state = sortingItemsStates[index]

            val start = state.animatedValue(SortingItemState.AnimationKey.StartPosition, movingAnimationFraction)
            val top = state.animatedValue(SortingItemState.AnimationKey.TopPosition, movingAnimationFraction)
            val strokeWidth = state.animatedValue(SortingItemState.AnimationKey.StrokeWidth, selectionAnimationFraction)
            val strokeWidthHalf = strokeWidth / 2
            val strokeColor = state.bgColor()
            itemPaint.color = strokeColor
            itemPaint.strokeWidth = strokeWidth
            canvas.drawRoundRect(start + strokeWidthHalf, top + strokeWidthHalf, start + itemSize - strokeWidthHalf, top + itemSize - strokeWidthHalf, itemRadius, itemRadius, itemPaint)

            val selectedSize = state.animatedValue(SortingItemState.AnimationKey.SelectedSize, selectionAnimationFraction)
            if (selectedSize > 0f) {
                val selectedSizeHalf = selectedSize / 2f
                val startCenter = start + (itemSize / 2f)
                val topCenter = top + (itemSize / 2f)
                itemPaint.style = Paint.Style.FILL
                canvas.drawRoundRect(
                    startCenter - selectedSizeHalf,
                    topCenter - selectedSizeHalf,
                    startCenter + selectedSizeHalf,
                    topCenter + selectedSizeHalf,
                    itemRadius, itemRadius, itemPaint
                )
                itemPaint.style = Paint.Style.STROKE
            }

            val textColor = state.animatedValue(SortingItemState.AnimationKey.TextColor, selectionAnimationFraction)
            textPaint.color = textColor
            val text = state.title
            textPaint.getTextBounds(text, 0, text.length, textBounds)
            val textWidth = textBounds.width()
            val textHeight = textBounds.height()
            canvas.drawText(
                text, 0, text.length,
                start + itemSize / 2 - (textWidth / 2),
                top + itemSize / 2 + textHeight / 2,
                textPaint
            )

            if (state.isAnimationRunning) {
                haveItemsAnimation = true
            }

            index++
        }

        if (animationState == SortingAnimationState.ANIMATION_RUNNING) {
            when {
                haveItemsAnimation && isAnimationEnabled -> postInvalidateOnAnimation()
                haveItemsAnimation -> invalidate()
                else -> changeAnimationProgress(1)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearCallbacks()
    }

    fun changeParams(
        strokeWidth: Float = this.defaultStrokeWidth,
        selectedStrokeWidth: Float = this.selectedStrokeWidth,
        strokeColor: Int = this.defaultStrokeColor,
        selectedStrokeColor: Int = this.selectedStrokeColor,
        textColor: Int = this.defaultTextColor,
        selectedTextColor: Int = this.selectedTextColor,
        textSize: Float = this.textPaint.textSize,
        typeface: Typeface = this.textPaint.typeface,
        selectionAnimationDuration: Long = this.selectionAnimationDuration,
        movingAnimationDuration: Long = this.movingAnimationDuration,
        isRepeatableAnimation: Boolean = this.isRepeatableAnimation,
        repeatableAnimationDuration: Long = this.repeatableAnimationDuration,
        itemSize: Float = this.itemSize,
        itemMargin: Float = this.itemMargin,
        itemRadius: Float = this.itemRadius
    ) {
        clearCallbacks()

        val pausedAnimation = pauseAnimation()

        this.defaultStrokeWidth = strokeWidth
        this.selectedStrokeWidth = selectedStrokeWidth

        this.defaultStrokeColor = strokeColor
        this.selectedStrokeColor = selectedStrokeColor

        this.defaultTextColor = textColor
        this.selectedTextColor = selectedTextColor

        this.textPaint.textSize = textSize
        this.textPaint.typeface = typeface

        this.selectionAnimationDuration = selectionAnimationDuration
        this.movingAnimationDuration = movingAnimationDuration

        this.isRepeatableAnimation = isRepeatableAnimation
        this.repeatableAnimationDuration = repeatableAnimationDuration

        val needsRequestLayout = this.itemSize != itemSize || this.itemMargin != itemMargin

        this.itemSize = itemSize
        this.itemMargin = itemMargin
        this.itemRadius = itemRadius

        when {
            pausedAnimation -> startAnimation()
            needsRequestLayout -> requestLayout()
            else -> invalidate()
        }
    }

    // values: 0f..1f
    fun animationProgress(): Float {
        if (stepIndex == 0) return 0f
        if (steps.isEmpty()) return 0f

        return stepIndex / (steps.size - 1).toFloat()
    }

    fun changeAnimationSteps(newSteps: List<SortingAlgorithmStep>) {
        if (newSteps.isEmpty()) throw IllegalArgumentException("The steps is empty")

        steps.clear(); steps.addAll(newSteps)

        resetItems()
        handleStep(animate = false)
    }

    fun changeAnimationProgress(numberOfSteps: Int) {
        val newStepIndex = stepIndex + numberOfSteps

        if (newStepIndex < -1 || newStepIndex > steps.size - 1) return

        changeAnimationProgressInternal(newStepIndex)
    }

    // the progress range: 0f..1f
    fun changeAnimationProgress(progress: Float) {
        if (progress < 0f || progress > 1f) throw IllegalArgumentException("The progress: $progress must have the range: 0f..1f")
        if (steps.isEmpty()) throw IllegalArgumentException("The steps is empty")

        // the index range: -1..steps.size-1
        val newStepIndex = (steps.size * progress).roundToInt() - 1

        changeAnimationProgressInternal(newStepIndex)
    }

    private fun changeAnimationProgressInternal(newStepIndex: Int) {
        val isAnimationWasStarted = pauseAnimation()

        // finish the current step animations
        sortingItemsStates.forEach { state -> state.finishAnimation() }
        pausedAnimationDifference = 0L
        invokeStepFinishAction()

        if (newStepIndex < stepIndex) {
            resetItems() // inverted handleStep() is not implemented
        }

        stepIndex++
        while (stepIndex < newStepIndex) {
            handleStep(animate = false)

            stepIndex++
        }

        // to animate the last applied step or not
        if (isAnimationWasStarted) {
            startAnimation()
        } else {
            handleStep(animate = false)
        }
    }

    fun changeArray(intArray: IntArray) {
        checkArraySize(intArray); clearCallbacks()

        sortingArrayCopy = intArray.copyOf()

        sortingItemsStates.clear()
        sortingItemsStates.addAll(sortingArrayCopy.map { SortingItemState() })

        requestLayout()
    }

    fun startAnimation() {
        if (steps.isEmpty()) throw IllegalStateException("The steps list is empty!")

        if (animationState == SortingAnimationState.ANIMATION_RUNNING) return

        animationState = SortingAnimationState.ANIMATION_RUNNING
        handleStep()
    }

    fun pauseAnimation(): Boolean {
        clearCallbacks()

        if (animationState == SortingAnimationState.ANIMATION_RUNNING) {
            animationState = SortingAnimationState.ANIMATION_PAUSED
            return true
        }

        return false
    }

    fun stopAnimation() {
        if (animationState != SortingAnimationState.ANIMATION_STOPPED) {
            animationState = SortingAnimationState.ANIMATION_STOPPED

            resetItems()
        }
    }

    fun changeStepListener(listener: (Int, SortingAlgorithmStep) -> Unit) {
        stepListener = listener
    }

    private fun handleStep(animate: Boolean = true) {
        clearCallbacks()

        val step = steps.fetchCurrentOrEmptyStep(stepIndex)
        stepListener.invoke(stepIndex, step)

        val status = when (step) {
            is SortingAlgorithmStep.Swap -> handleStep(step, animate)
            is SortingAlgorithmStep.Select -> handleStep(step, animate)
            is SortingAlgorithmStep.Unselect -> handleStep(step, animate)
            is SortingAlgorithmStep.End -> handleStep(step, animate)
            is SortingAlgorithmStep.Empty -> handleStep(step, animate)
        }

        when (status) {
            HandleStepStatus.ANIMATION_INVALIDATE -> {
                currentAnimationTime = SystemClock.uptimeMillis() - pausedAnimationDifference
                postInvalidateOnAnimation()
            }
            HandleStepStatus.JUST_INVALIDATE -> {
                currentAnimationTime = 0
                invalidate()
            }
            HandleStepStatus.NOT_INVALIDATE -> {
                // nothing to do
            }
            HandleStepStatus.ERROR_INVALIDATE -> {
                throw IllegalStateException("The step ${steps[stepIndex]} has invalid field values")
            }
        }
    }

    private fun handleStep(step: SortingAlgorithmStep.Select, animate: Boolean = true): HandleStepStatus {
        val selectedIndices = step.indices
        val totalIndices = sortingItemsStates.indices

        var handleStepStatus = HandleStepStatus.ERROR_INVALIDATE

        selectedIndices.forEach { index ->
            if (index in totalIndices) {
                when {
                    animate -> {
                        sortingItemsStates[index]
                            .addValue(SortingItemState.AnimationKey.SelectedSize, itemSize)
                            .addValue(SortingItemState.AnimationKey.TextColor, selectedTextColor)

                        handleStepStatus = HandleStepStatus.ANIMATION_INVALIDATE
                    }
                    else -> {
                        sortingItemsStates[index]
                            .forceValue(SortingItemState.AnimationKey.SelectedSize, itemSize)
                            .forceValue(SortingItemState.AnimationKey.TextColor, selectedTextColor)

                        handleStepStatus = HandleStepStatus.JUST_INVALIDATE
                    }
                }
            }
        }

        return handleStepStatus
    }

    private fun handleStep(step: SortingAlgorithmStep.Unselect, animate: Boolean = true): HandleStepStatus {
        val selectedIndices = step.indices
        val totalIndices = sortingItemsStates.indices

        var handleStepStatus = HandleStepStatus.ERROR_INVALIDATE

        selectedIndices.forEach { index ->
            if (index in totalIndices) {
                when {
                    animate -> {
                        sortingItemsStates[index]
                            .addValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                            .addValue(SortingItemState.AnimationKey.TextColor, defaultTextColor)

                        handleStepStatus = HandleStepStatus.ANIMATION_INVALIDATE
                    }
                    else -> {
                        sortingItemsStates[index]
                            .forceValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                            .forceValue(SortingItemState.AnimationKey.TextColor, defaultTextColor)

                        handleStepStatus = HandleStepStatus.JUST_INVALIDATE
                    }
                }
            }
        }

        return handleStepStatus
    }

    private fun handleStep(step: SortingAlgorithmStep.Swap, animate: Boolean = true): HandleStepStatus {
        val index1 = step.index1
        val index2 = step.index2
        val totalIndices = sortingItemsStates.indices

        if (index1 !in totalIndices && index2 !in totalIndices) return HandleStepStatus.ERROR_INVALIDATE

        val sortingState1 = sortingItemsStates[index1]
        val sortingState2 = sortingItemsStates[index2]

        val startPosition1 = sortingState1.value(SortingItemState.AnimationKey.StartPosition)
        val startPosition2 = sortingState2.value(SortingItemState.AnimationKey.StartPosition)
        val topPosition1 = sortingState1.value(SortingItemState.AnimationKey.TopPosition)
        val topPosition2 = sortingState2.value(SortingItemState.AnimationKey.TopPosition)

        return when {
            animate -> {
                sortingState1
                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition1 - itemSize)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition1 - itemSize)
                    .addValue(SortingItemState.AnimationKey.StartPosition, startPosition2)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition1)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                sortingState2
                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition2 + itemSize)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition2 + itemSize)
                    .addValue(SortingItemState.AnimationKey.StartPosition, startPosition1)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition2)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                stepFinishAction = {
                    sortingItemsStates[index1] = sortingState2
                    sortingItemsStates[index2] = sortingState1
                    true
                }

                HandleStepStatus.ANIMATION_INVALIDATE
            }
            else -> {
                sortingState1.forceValue(SortingItemState.AnimationKey.StartPosition, startPosition2)
                sortingState2.forceValue(SortingItemState.AnimationKey.StartPosition, startPosition1)

                sortingItemsStates[index1] = sortingState2
                sortingItemsStates[index2] = sortingState1

                HandleStepStatus.JUST_INVALIDATE
            }
        }
    }

    private fun handleStep(step: SortingAlgorithmStep.End, animate: Boolean): HandleStepStatus {
        if (animate && isRepeatableAnimation) {
            postDelayed(restartAnimationDelayedRunnable, repeatableAnimationDuration)
        }
        return HandleStepStatus.NOT_INVALIDATE
    }

    private fun handleStep(step: SortingAlgorithmStep.Empty, animate: Boolean): HandleStepStatus {
        if (animate) return HandleStepStatus.ANIMATION_INVALIDATE
        return HandleStepStatus.JUST_INVALIDATE
    }

    private fun List<SortingAlgorithmStep>.fetchCurrentOrEmptyStep(index: Int): SortingAlgorithmStep {
        if (index !in indices) return SortingAlgorithmStep.Empty
        return get(index)
    }

    private fun invokeStepFinishAction() {
        stepFinishAction.invoke()
        stepFinishAction = emptyStepFinishAction
    }

    private fun resetItems() {
        clearCallbacks()

        stepFinishAction = emptyStepFinishAction
        pausedAnimationDifference = 0
        stepIndex = -1

        sortingArrayCopy.forEachIndexed { index, value ->
            sortingItemsStates[index]
                .changeTitle(value.toString())
                .changeBgColor(defaultStrokeColor)
                .forceValue(SortingItemState.AnimationKey.StartPosition, startOfView + itemSize * index)
                .forceValue(SortingItemState.AnimationKey.TopPosition, topOfView)
                .forceValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                .forceValue(SortingItemState.AnimationKey.StrokeWidth, defaultStrokeWidth)
                .forceValue(SortingItemState.AnimationKey.TextColor, defaultTextColor)
        }
    }

    private fun clearCallbacks() {
        removeCallbacks(restartAnimationDelayedRunnable)
    }

    private fun divideItems(dividerIndex: Int): Boolean {
        if (dividerIndex in sortingItemsStates.indices) {
            var index = 0

            var topPosition = sortingItemsStates[dividerIndex].value(SortingItemState.AnimationKey.TopPosition)
            if ((topPosition + 2 * itemSize) >= measuredHeight) {
                topPosition = paddingTop.toFloat()

                moveItemsToTopPosition(topPosition)
            }

            val newTopPosition = topPosition + itemSize + itemMargin * 2
            while (index < dividerIndex) {
                val state = sortingItemsStates[index]

                val lastStartPosition = state.value(SortingItemState.AnimationKey.StartPosition)
                state
                    .addValue(SortingItemState.AnimationKey.StartPosition, lastStartPosition - itemMargin / 2)
                    .addValue(SortingItemState.AnimationKey.TopPosition, newTopPosition)

                index++
            }

            while (index < sortingItemsStates.size) {
                val state = sortingItemsStates[index]
                val lastStartPosition = state.value(SortingItemState.AnimationKey.StartPosition)
                state
                    .addValue(SortingItemState.AnimationKey.StartPosition, lastStartPosition + itemMargin / 2)
                    .addValue(SortingItemState.AnimationKey.TopPosition, newTopPosition)

                index++
            }

            return true
        }

        return false
    }

    private fun moveItem(currentIndex: Int, newIndex: Int): Boolean {
        val indices = sortingItemsStates.indices
        if (currentIndex in indices && newIndex in indices) {
            val state = sortingItemsStates[currentIndex]
            var topPosition = state.value(SortingItemState.AnimationKey.TopPosition)
            if ((topPosition + 2 * itemSize) >= measuredHeight) {
                topPosition = paddingTop.toFloat()
                moveItemsToTopPosition(topPosition)
            }

            val newStartPosition = sortingItemsStates[newIndex].value(SortingItemState.AnimationKey.StartPosition)
            state
                .addValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)
                .addValue(SortingItemState.AnimationKey.TopPosition, topPosition + itemSize + itemMargin * 2)

            return true
        }

        return false
    }

    private fun moveItemsToTopPosition(position: Float) {
        sortingItemsStates.forEach { state ->
            state
                .addLastValue(SortingItemState.AnimationKey.StartPosition)
                .addValue(SortingItemState.AnimationKey.TopPosition, position)
        }
    }

    private fun checkArraySize(intArray: IntArray) {
        val arraySize = intArray.size
        val minItemSize = 2
        val maxItemSize = 8
        if (arraySize < minItemSize || arraySize > maxItemSize) {
            throw IllegalArgumentException("The array $intArray has an unsuitable size: ${intArray.size}, it must have a size between $minItemSize and $maxItemSize")
        }
    }

    private enum class SortingAnimationState {
        ANIMATION_STOPPED,
        ANIMATION_RUNNING,
        ANIMATION_PAUSED
    }

    private enum class HandleStepStatus {
        ANIMATION_INVALIDATE,
        JUST_INVALIDATE,
        NOT_INVALIDATE,
        ERROR_INVALIDATE
    }

}