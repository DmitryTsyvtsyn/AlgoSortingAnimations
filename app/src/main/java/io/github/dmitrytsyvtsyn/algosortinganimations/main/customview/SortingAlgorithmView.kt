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
import io.github.dmitrytsyvtsyn.algosortinganimations.core.logger.ProxyLogger
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

    private val itemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val textPaint = TextPaint().apply {
        textSize = 19.sp
    }
    private val textBounds = Rect()

    private val sortingItemsStates = mutableListOf<SortingItemState>()
    private var sortingArrayCopy = intArrayOf()

    private var stepIndex = 0
    private var stepListener: (Int, SortingAlgorithmStep) -> Unit = { _, _ -> }
    private val steps = mutableListOf<SortingAlgorithmStep>(SortingAlgorithmStep.End(""))

    private var animationState = SortingAnimationState.ANIMATION_STOPPED

    private var currentAnimationTime = 0L
    private var pausedAnimationDifference = 0L

    private var startOfView = 0f
    private var topOfView = 0f

    private val startAnimationDelayedRunnable = Runnable { startAnimation() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        ProxyLogger.debug(tag, "onMeasure() called")

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
            if (haveItemsAnimation) {
                if (isAnimationEnabled) postInvalidateOnAnimation() else invalidate()
                return
            }

            if (stepIndex < steps.size - 1) {
                pausedAnimationDifference = 0L
                stepIndex += 1
                handleStep()
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
        textColor: Int = this.textPaint.color,
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
        ProxyLogger.debug(tag, "changeParams() called")

        clearCallbacks()

        val pausedAnimation = pauseAnimation()

        this.defaultStrokeWidth = strokeWidth
        this.selectedStrokeWidth = selectedStrokeWidth

        this.defaultStrokeColor = strokeColor
        this.selectedStrokeColor = selectedStrokeColor

        this.textPaint.color = textColor
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
        ProxyLogger.debug(tag, "changeAnimationSteps($newSteps) called")

        if (newSteps.isEmpty()) throw IllegalArgumentException("The steps is empty")

        steps.clear(); steps.addAll(newSteps)

        resetItems()
        handleStep(animate = false)
    }

    fun changeAnimationProgress(numberOfSteps: Int) {
        val newStepIndex = stepIndex + numberOfSteps

        if (newStepIndex < 0 || newStepIndex > steps.size - 1) return

        changeAnimationProgress(newStepIndex / (steps.size - 1f))
    }

    // values: 0f..1f
    fun changeAnimationProgress(progress: Float) {
        ProxyLogger.debug(tag, "changeAnimationProgress($progress) called")

        if (progress < 0f || progress > 1f) throw IllegalArgumentException("The progress: $progress must have the range: 0f..1f")
        if (steps.isEmpty()) throw IllegalArgumentException("The steps is empty")

        val isAnimationWasStarted = pauseAnimation()

        val newStepIndex = ((steps.size - 1) * progress).roundToInt()
        ProxyLogger.debug(tag, "changeAnimationProgress() currentStepIndex: $newStepIndex")
        ProxyLogger.debug(tag, "changeAnimationProgress() stepIndex before comparing: $stepIndex")
        if (newStepIndex < stepIndex) {
            resetItems() // inverted handleStep() is not implemented
        }
        ProxyLogger.debug(tag, "changeAnimationProgress() stepIndex after comparing: $stepIndex")
        while (stepIndex < newStepIndex) {
            handleStep(animate = false)

            stepIndex++
        }
        handleStep(animate = false)
        ProxyLogger.debug(tag, "changeAnimationProgress() stepIndex after while cycle: $stepIndex")

        if (isAnimationWasStarted) {
            startAnimation()
        }
    }

    fun changeArray(intArray: IntArray) {
        ProxyLogger.debug(tag, "changeArray($intArray) called")

        checkArraySize(intArray); clearCallbacks()

        sortingArrayCopy = intArray.copyOf()

        sortingItemsStates.clear()
        sortingItemsStates.addAll(sortingArrayCopy.map { SortingItemState() })

        requestLayout()
    }

    fun startAnimation() {
        ProxyLogger.debug(tag, "startAnimation() called")

        if (steps.isEmpty()) throw IllegalStateException("The steps list is empty!")

        if (animationState == SortingAnimationState.ANIMATION_RUNNING) return

        animationState = SortingAnimationState.ANIMATION_RUNNING
        handleStep()
    }

    fun pauseAnimation(): Boolean {
        ProxyLogger.debug(tag, "pauseAnimation() called")

        clearCallbacks()

        if (animationState == SortingAnimationState.ANIMATION_RUNNING) {
            ProxyLogger.debug(tag, "pauseAnimation() returns true")
            animationState = SortingAnimationState.ANIMATION_PAUSED
            return true
        }
        ProxyLogger.debug(tag, "pauseAnimation() returns false")
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
        ProxyLogger.debug(tag, "handleStep($animate) called")
        ProxyLogger.debug(tag, "handleStep() stepIndex: $stepIndex")

        clearCallbacks()

        val step = steps[stepIndex]
        stepListener.invoke(stepIndex, step)

        when (step) {
            is SortingAlgorithmStep.Swap -> {
                val updated = swapItems(step.index1, step.index2, animate)
                updateView(updated && animate)
            }
            is SortingAlgorithmStep.Select -> {
                val updated = decorateItems(SortingItemDecorator.SELECTED, step.indices, animate)
                updateView(updated && animate)
            }
            is SortingAlgorithmStep.Unselect -> {
                val updated = decorateItems(SortingItemDecorator.EMPTY, step.indices, animate)
                updateView(updated && animate)
            }
            is SortingAlgorithmStep.End -> {
                val hasRunning = animationState == SortingAnimationState.ANIMATION_RUNNING
                stopAnimation()
                if (hasRunning && isRepeatableAnimation) {
                    postDelayed(startAnimationDelayedRunnable, repeatableAnimationDuration)
                }
            }
        }
    }

    private fun updateView(animate: Boolean) {
        if (animate) {
            currentAnimationTime = SystemClock.uptimeMillis() - pausedAnimationDifference
            postInvalidateOnAnimation()
        } else {
            currentAnimationTime = 0
            invalidate()
        }
    }

    private fun resetItems() {
        ProxyLogger.debug(tag, "resetItems() called")

        clearCallbacks()

        pausedAnimationDifference = 0
        stepIndex = 0

        sortingArrayCopy.forEachIndexed { index, value ->
            sortingItemsStates[index]
                .changeTitle(value.toString())
                .changeBgColor(defaultStrokeColor)
                .forceValue(SortingItemState.AnimationKey.StartPosition, startOfView + itemSize * index)
                .forceValue(SortingItemState.AnimationKey.TopPosition, topOfView)
                .forceValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                .forceValue(SortingItemState.AnimationKey.StrokeWidth, defaultStrokeWidth)
        }
    }

    private fun clearCallbacks() {
        ProxyLogger.debug(tag, "clearCallbacks() called")
        removeCallbacks(startAnimationDelayedRunnable)
    }

    private fun decorateItems(
        decorator: SortingItemDecorator,
        indices: IntArray,
        animate: Boolean = true
    ) : Boolean {
        ProxyLogger.debug(tag, "decorateItems($decorator, ${indices.toList()}, $animate) called")

        val statesIndices = sortingItemsStates.indices

        var isUpdated = false

        val indicesCount = indices.size
        var pointer = 0
        while (pointer < indicesCount) {
            val index = indices[pointer]

            if (index in statesIndices) {
                val itemSize = when (decorator) {
                    SortingItemDecorator.SELECTED -> itemSize
                    SortingItemDecorator.EMPTY -> 0f
                }

                when {
                    animate -> sortingItemsStates[index].addValue(SortingItemState.AnimationKey.SelectedSize, itemSize)
                    else -> sortingItemsStates[index].forceValue(SortingItemState.AnimationKey.SelectedSize, itemSize)
                }

                isUpdated = true
            }

            pointer++
        }

        return isUpdated
    }

    private fun swapItems(index1: Int, index2: Int, animate: Boolean = true): Boolean {
        val indices = sortingItemsStates.indices

        if (index1 in indices && index2 in indices) {
            val sortingState1 = sortingItemsStates[index1]
            val sortingState2 = sortingItemsStates[index2]

            sortingState1.cancelAnimation()
            sortingState2.cancelAnimation()

            val startPosition1 = sortingState1.value(SortingItemState.AnimationKey.StartPosition)
            val startPosition2 = sortingState2.value(SortingItemState.AnimationKey.StartPosition)
            val topPosition1 = sortingState1.value(SortingItemState.AnimationKey.TopPosition)
            val topPosition2 = sortingState2.value(SortingItemState.AnimationKey.TopPosition)

            when {
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

                    sortingState1.addOnAnimationEnd {
                        sortingItemsStates[index1] = sortingState2
                    }

                    sortingState2.addOnAnimationEnd {
                        sortingItemsStates[index2] = sortingState1
                    }
                }
                else -> {
                    sortingState1.forceValue(SortingItemState.AnimationKey.StartPosition, startPosition2)
                    sortingState2.forceValue(SortingItemState.AnimationKey.StartPosition, startPosition1)

                    sortingItemsStates[index1] = sortingState2
                    sortingItemsStates[index2] = sortingState1
                }
            }

            return true
        }

        return false
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

    private enum class SortingItemDecorator { SELECTED, EMPTY }

}