package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.SystemClock
import android.text.TextPaint
import android.util.SparseArray
import android.view.View
import androidx.core.util.forEach
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

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
    private var selectionAnimationDuration = 2_500L
    private var movingAnimationDuration = 5_000L
    private var isRepeatableAnimation = true
    private var repeatableAnimationDuration = 2_500L
    private var itemSize = 48.dpf
    private var itemMargin = 8.dpf
    private var itemRadius = 8.dpf
    private var itemColor = 0xff019701.toInt()
    private var defaultTextColor = 0xff000000.toInt()
    private var selectedTextColor = 0xffffffff.toInt()
    private var selectedRangeColor = 0xff0b74de.toInt()

    private val itemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val textPaint = TextPaint().apply {
        color = defaultTextColor
        textSize = 19.sp
    }
    private val textBounds = Rect()

    private val sortingItemsStates = mutableListOf<SortingItemState>()
    private val pendingSortingItemStates = SparseArray<SortingItemState>()
    private var sortingArrayCopy = intArrayOf()

    private var stepIndex = -1
    private var stepListener: (Int, SortingAlgorithmStep) -> Unit = { _, _ -> }
    private val steps = mutableListOf<SortingAlgorithmStep>(SortingAlgorithmStep.End(""))

    private var animationState = SortingAnimationState.ANIMATION_STOPPED

    private var currentAnimationTime = 0L
    private var pausedAnimationTime = 0L

    private var startOfView = 0f
    private var topOfView = 0f

    private val restartAnimationDelayedRunnable = Runnable { stopAnimation(); startAnimation() }

    private val stepFinishActions = mutableListOf<() -> Boolean>()

    private val stepQueue = mutableListOf<SortingAlgorithmStep>()

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

        startOfView = measuredWidth / 2 - itemSize * sortingArrayCopy.size / 2
        topOfView = measuredHeight / 2 - itemSize / 2

        resetItems(); handleStep(animate = false)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val animationTime = SystemClock.uptimeMillis() - currentAnimationTime
        pausedAnimationTime = animationTime

        var haveItemsAnimation = false

        var index = 0
        while (index < sortingItemsStates.size) {
            val state = sortingItemsStates[index]

            canvas.drawState(state, animationTime)
            haveItemsAnimation = haveItemsAnimation || state.isAnimationRunning

            val pendingState = pendingSortingItemStates.get(index)
            if (pendingState != null) {
                canvas.drawState(pendingState, animationTime)
                haveItemsAnimation = haveItemsAnimation || pendingState.isAnimationRunning
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

    private fun Canvas.drawState(state: SortingItemState, animationTime: Long) {
        val start = state.animatedValue(SortingItemState.AnimationKey.StartPosition, animationTime)
        val top = state.animatedValue(SortingItemState.AnimationKey.TopPosition, animationTime)
        val strokeWidth = state.animatedValue(SortingItemState.AnimationKey.StrokeWidth, animationTime)
        val strokeWidthHalf = strokeWidth / 2
        val strokeColor = state.animatedValue(SortingItemState.AnimationKey.StrokeColor, animationTime)
        itemPaint.color = strokeColor
        itemPaint.strokeWidth = strokeWidth
        drawRoundRect(start + strokeWidthHalf, top + strokeWidthHalf, start + itemSize - strokeWidthHalf, top + itemSize - strokeWidthHalf, itemRadius, itemRadius, itemPaint)

        val selectedSize = state.animatedValue(SortingItemState.AnimationKey.SelectedSize, animationTime)
        if (selectedSize > 0f) {
            val selectedSizeHalf = selectedSize / 2f
            val startCenter = start + (itemSize / 2f)
            val topCenter = top + (itemSize / 2f)
            itemPaint.color = state.animatedValue(SortingItemState.AnimationKey.BackgroundColor, animationTime)
            itemPaint.style = Paint.Style.FILL
            drawRoundRect(
                startCenter - selectedSizeHalf,
                topCenter - selectedSizeHalf,
                startCenter + selectedSizeHalf,
                topCenter + selectedSizeHalf,
                itemRadius, itemRadius, itemPaint
            )
            itemPaint.style = Paint.Style.STROKE
        }

        val textColor = state.animatedValue(SortingItemState.AnimationKey.TextColor, animationTime)
        textPaint.color = textColor
        val text = state.title
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textWidth = textBounds.width()
        val textHeight = textBounds.height()
        drawText(
            text, 0, text.length,
            start + itemSize / 2 - (textWidth / 2),
            top + itemSize / 2 + textHeight / 2,
            textPaint
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearCallbacks()
    }

    fun changeParams(
        strokeWidth: Float = this.defaultStrokeWidth,
        selectedStrokeWidth: Float = this.selectedStrokeWidth,
        textColor: Int = this.defaultTextColor,
        selectedTextColor: Int = this.selectedTextColor,
        selectedRangeColor: Int = this.selectedRangeColor,
        textSize: Float = this.textPaint.textSize,
        typeface: Typeface = this.textPaint.typeface,
        selectionAnimationDuration: Long = this.selectionAnimationDuration,
        movingAnimationDuration: Long = this.movingAnimationDuration,
        isRepeatableAnimation: Boolean = this.isRepeatableAnimation,
        repeatableAnimationDuration: Long = this.repeatableAnimationDuration,
        itemSize: Float = this.itemSize,
        itemMargin: Float = this.itemMargin,
        itemRadius: Float = this.itemRadius,
        itemColor: Int = this.itemColor
    ) {
        clearCallbacks()

        val pausedAnimation = pauseAnimation()

        this.defaultStrokeWidth = strokeWidth
        this.selectedStrokeWidth = selectedStrokeWidth

        this.defaultTextColor = textColor
        this.selectedTextColor = selectedTextColor
        this.selectedRangeColor = selectedRangeColor

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
        this.itemColor = itemColor

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
        pendingSortingItemStates.forEach { _, state -> state.finishAnimation() }
        pausedAnimationTime = 0L
        invokeStepFinishActions()

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
        checkArraySize(intArray)

        sortingArrayCopy = intArray.copyOf()

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

        var status = HandleStepStatus.ANIMATION_INVALIDATE
        // case: startAnimation() call after pauseAnimation() call
        // the paused animation needs to be finished
        if (sortingItemsStates.none { it.isAnimationRunning }) {
            val step = steps.fetchCurrentOrEmptyStep(stepIndex)
            stepListener.invoke(stepIndex, step)

            stepQueue.clear()
            stepQueue.add(step)

            status = HandleStepStatus.NOT_INVALIDATE
        }

        while (stepQueue.size > 0) {
            val queuedStep = stepQueue.removeFirst()
            if (queuedStep is SortingAlgorithmStep.List) {
                stepQueue.addAll(queuedStep.steps)
                continue
            }

            val realAnimate = animate && !queuedStep.force
            val stepStatus = when (queuedStep) {
                is SortingAlgorithmStep.Swap -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Select -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Unselect -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.SelectRange -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.UnselectRange -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Insert -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Shift -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Up -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Divide -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.End -> handleStep(queuedStep, realAnimate)
                is SortingAlgorithmStep.Empty -> handleStep(queuedStep, realAnimate)
                else -> throw IllegalStateException("Unhandled step: $queuedStep")
            }

            if (stepStatus.priority > status.priority) {
                status = stepStatus
            }
        }

        when (status) {
            HandleStepStatus.ANIMATION_INVALIDATE -> {
                currentAnimationTime = SystemClock.uptimeMillis() - pausedAnimationTime
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

        if (selectedIndices.isEmpty()) return HandleStepStatus.ERROR_INVALIDATE
        if (selectedIndices.any { index -> index !in totalIndices }) return HandleStepStatus.ERROR_INVALIDATE

        selectedIndices.forEach { index ->
            if (index in totalIndices) {
                when {
                    animate -> {
                        sortingItemsStates[index]
                            .addValue(SortingItemState.AnimationKey.SelectedSize, itemSize)
                            .addValue(SortingItemState.AnimationKey.TextColor, selectedTextColor)
                    }
                    else -> {
                        sortingItemsStates[index]
                            .forceValue(SortingItemState.AnimationKey.SelectedSize, itemSize)
                            .forceValue(SortingItemState.AnimationKey.TextColor, selectedTextColor)
                    }
                }
            }
        }

        if (animate) {
            return HandleStepStatus.ANIMATION_INVALIDATE
        }
        return HandleStepStatus.JUST_INVALIDATE
    }

    private fun handleStep(step: SortingAlgorithmStep.Unselect, animate: Boolean = true): HandleStepStatus {
        val selectedIndices = step.indices
        val totalIndices = sortingItemsStates.indices

        if (selectedIndices.isEmpty()) return HandleStepStatus.ERROR_INVALIDATE
        if (selectedIndices.any { index -> index !in totalIndices }) return HandleStepStatus.ERROR_INVALIDATE

        selectedIndices.forEach { index ->
            when {
                animate -> {
                    sortingItemsStates[index]
                        .addValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                        .addValue(SortingItemState.AnimationKey.TextColor, defaultTextColor)
                }
                else -> {
                    sortingItemsStates[index]
                        .forceValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                        .forceValue(SortingItemState.AnimationKey.TextColor, defaultTextColor)
                }
            }
        }

        if (animate) {
            return HandleStepStatus.ANIMATION_INVALIDATE
        }
        return HandleStepStatus.JUST_INVALIDATE
    }

    private fun handleStep(step: SortingAlgorithmStep.SelectRange, animate: Boolean): HandleStepStatus {
        val startIndex = step.startIndex
        val endIndex = step.endIndex
        val existingIndices = sortingItemsStates.indices
        if (startIndex !in existingIndices && endIndex !in existingIndices) return HandleStepStatus.ERROR_INVALIDATE
        if (startIndex > endIndex) return HandleStepStatus.ERROR_INVALIDATE

        (startIndex..endIndex).forEach { index ->
            when {
                animate -> {
                    sortingItemsStates[index].addValue(SortingItemState.AnimationKey.StrokeColor, selectedRangeColor)
                }
                else -> {
                    sortingItemsStates[index].forceValue(SortingItemState.AnimationKey.StrokeColor, selectedRangeColor)
                }
            }
        }

        if (animate) {
            return HandleStepStatus.ANIMATION_INVALIDATE
        }
        return HandleStepStatus.JUST_INVALIDATE
    }

    private fun handleStep(step: SortingAlgorithmStep.UnselectRange, animate: Boolean): HandleStepStatus {
        val startIndex = step.startIndex
        val endIndex = step.endIndex
        val existingIndices = sortingItemsStates.indices
        if (startIndex !in existingIndices && endIndex !in existingIndices) return HandleStepStatus.ERROR_INVALIDATE
        if (startIndex > endIndex) return HandleStepStatus.ERROR_INVALIDATE

        (startIndex..endIndex).forEach { index ->
            when {
                animate -> {
                    sortingItemsStates[index].addValue(SortingItemState.AnimationKey.StrokeColor, itemColor)
                }
                else -> {
                    sortingItemsStates[index].forceValue(SortingItemState.AnimationKey.StrokeColor, itemColor)
                }
            }
        }

        if (animate) {
            return HandleStepStatus.ANIMATION_INVALIDATE
        }
        return HandleStepStatus.JUST_INVALIDATE
    }

    private fun handleStep(step: SortingAlgorithmStep.Swap, animate: Boolean = true): HandleStepStatus {
        val index1 = step.index1
        val index2 = step.index2
        val totalIndices = sortingItemsStates.indices

        if (index1 !in totalIndices && index2 !in totalIndices) return HandleStepStatus.ERROR_INVALIDATE

        val state1 = sortingItemsStates[index1]
        val state2 = sortingItemsStates[index2]

        val startPosition1 = state1.value(SortingItemState.AnimationKey.StartPosition)
        val startPosition2 = state2.value(SortingItemState.AnimationKey.StartPosition)
        val topPosition1 = state1.value(SortingItemState.AnimationKey.TopPosition)
        val topPosition2 = state2.value(SortingItemState.AnimationKey.TopPosition)

        return when {
            animate -> {
                state1
                    .changeDuration(SortingItemState.AnimationKey.StartPosition, movingAnimationDuration)
                    .changeDuration(SortingItemState.AnimationKey.TopPosition, movingAnimationDuration)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition1 - itemSize)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition1 - itemSize)
                    .addValue(SortingItemState.AnimationKey.StartPosition, startPosition2)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition1)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                state2
                    .changeDuration(SortingItemState.AnimationKey.StartPosition, movingAnimationDuration)
                    .changeDuration(SortingItemState.AnimationKey.TopPosition, movingAnimationDuration)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition2 + itemSize)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition2 + itemSize)
                    .addValue(SortingItemState.AnimationKey.StartPosition, startPosition1)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition2)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                stepFinishActions.add {
                    sortingItemsStates[index1] = state2
                    sortingItemsStates[index2] = state1
                    true
                }

                HandleStepStatus.ANIMATION_INVALIDATE
            }
            else -> {
                state1.forceValue(SortingItemState.AnimationKey.StartPosition, startPosition2)
                state2.forceValue(SortingItemState.AnimationKey.StartPosition, startPosition1)

                sortingItemsStates[index1] = state2
                sortingItemsStates[index2] = state1

                HandleStepStatus.JUST_INVALIDATE
            }
        }
    }

    private fun handleStep(step: SortingAlgorithmStep.Insert, animate: Boolean): HandleStepStatus {
        val indices = sortingItemsStates.indices
        val currentIndex = step.currentIndex
        val newIndex = step.newIndex

        if (currentIndex !in indices && newIndex !in indices) return HandleStepStatus.ERROR_INVALIDATE

        var state = sortingItemsStates[currentIndex]
        val pendingState = pendingSortingItemStates.get(currentIndex)
        if (pendingState != null) {
            state = pendingState
        }

        val startPosition = startOfView + itemSize * newIndex
        val topPosition = state.value(SortingItemState.AnimationKey.TopPosition) + itemSize + itemMargin

        return when {
            animate -> {
                state
                    .changeDuration(SortingItemState.AnimationKey.StartPosition, movingAnimationDuration)
                    .changeDuration(SortingItemState.AnimationKey.TopPosition, movingAnimationDuration)

                    .addValue(SortingItemState.AnimationKey.StartPosition, startPosition)
                    .addLastValue(SortingItemState.AnimationKey.TopPosition)

                    .addValue(SortingItemState.AnimationKey.TopPosition, topPosition)
                    .addLastValue(SortingItemState.AnimationKey.StartPosition)

                stepFinishActions.add {
                    sortingItemsStates[newIndex] = state
                    pendingSortingItemStates.remove(currentIndex)

                    true
                }

                HandleStepStatus.ANIMATION_INVALIDATE
            }
            else -> {
                state
                    .forceValue(SortingItemState.AnimationKey.StartPosition, startPosition)
                    .forceValue(SortingItemState.AnimationKey.TopPosition, topPosition)

                sortingItemsStates[newIndex] = state
                pendingSortingItemStates.remove(currentIndex)

                HandleStepStatus.JUST_INVALIDATE
            }
        }
    }

    private fun handleStep(step: SortingAlgorithmStep.Shift, animate: Boolean): HandleStepStatus {
        val indices = sortingItemsStates.indices
        val currentIndex = step.currentIndex
        val newIndex = step.newIndex

        if (currentIndex !in indices && newIndex !in indices) return HandleStepStatus.ERROR_INVALIDATE

        val state = sortingItemsStates[currentIndex]

        val shiftCount = newIndex - currentIndex
        val newStartPosition = state.value(SortingItemState.AnimationKey.StartPosition) + itemSize * shiftCount
        return when {
            animate -> {
                var startPositionDuration = movingAnimationDuration
                if (shiftCount.absoluteValue == 1) {
                    startPositionDuration = movingAnimationDuration / 2
                }

                state
                    .changeDuration(SortingItemState.AnimationKey.StartPosition, startPositionDuration)
                    .addValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)

                stepFinishActions.add {
                    sortingItemsStates[newIndex] = state

                    true
                }

                HandleStepStatus.ANIMATION_INVALIDATE
            }
            else -> {
                state.forceValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)

                sortingItemsStates[newIndex] = state

                HandleStepStatus.JUST_INVALIDATE
            }
        }
    }

    private fun handleStep(step: SortingAlgorithmStep.Up, animate: Boolean): HandleStepStatus {
        val uppedIndices = step.indices
        val totalIndices = sortingItemsStates.indices

        if (uppedIndices.isEmpty()) return HandleStepStatus.ERROR_INVALIDATE
        if (uppedIndices.any { index -> index !in totalIndices }) return HandleStepStatus.ERROR_INVALIDATE

        uppedIndices.forEach { index ->
            val state = sortingItemsStates[index]

            pendingSortingItemStates.put(index, state)

            val newTopPosition = state.value(SortingItemState.AnimationKey.TopPosition) - itemSize - itemMargin
            when {
                animate -> {
                    state
                        .changeDuration(SortingItemState.AnimationKey.TopPosition, movingAnimationDuration / 2)
                        .addValue(SortingItemState.AnimationKey.TopPosition, newTopPosition)
                }
                else -> {
                    state.forceValue(SortingItemState.AnimationKey.TopPosition, newTopPosition)
                }
            }
        }

        if (animate) {
            return HandleStepStatus.ANIMATION_INVALIDATE
        }
        return HandleStepStatus.JUST_INVALIDATE
    }

    private fun handleStep(step: SortingAlgorithmStep.Divide, animate: Boolean): HandleStepStatus {
        val pivotIndex = step.pivotIndex

        if (pivotIndex !in sortingItemsStates.indices) return HandleStepStatus.ERROR_INVALIDATE

        var index = 0
        while (index < pivotIndex) {
            val state = sortingItemsStates[index]

            val newStartPosition = state.value(SortingItemState.AnimationKey.StartPosition) - itemMargin / 2
            when {
                animate -> {
                    state.addValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)
                }
                else -> {
                    state.forceValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)
                }
            }

            pendingSortingItemStates.put(index, state)
            index++
        }

        while (index < sortingItemsStates.size) {
            val state = sortingItemsStates[index]

            val newStartPosition = state.value(SortingItemState.AnimationKey.StartPosition) + itemMargin / 2
            when {
                animate -> {
                    state.addValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)
                }
                else -> {
                    state.forceValue(SortingItemState.AnimationKey.StartPosition, newStartPosition)
                }
            }

            pendingSortingItemStates.put(index, state)
            index++
        }

        if (animate) {
            return HandleStepStatus.ANIMATION_INVALIDATE
        }
        return HandleStepStatus.JUST_INVALIDATE
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

    private fun invokeStepFinishActions() {
        stepFinishActions.forEach { it.invoke() }
        stepFinishActions.clear()
    }

    private fun resetItems() {
        clearCallbacks()

        stepFinishActions.clear()
        pausedAnimationTime = 0
        stepIndex = -1

        pendingSortingItemStates.clear()

        sortingItemsStates.matchWithSize(sortingArrayCopy.size) { SortingItemState() }

        sortingArrayCopy.forEachIndexed { index, value ->
            sortingItemsStates[index]
                .changeTitle(value.toString())

                .changeDuration(SortingItemState.AnimationKey.StartPosition, movingAnimationDuration)
                .changeDuration(SortingItemState.AnimationKey.TopPosition, movingAnimationDuration)
                .changeDuration(SortingItemState.AnimationKey.SelectedSize, selectionAnimationDuration)
                .changeDuration(SortingItemState.AnimationKey.StrokeWidth, selectionAnimationDuration)
                .changeDuration(SortingItemState.AnimationKey.StrokeColor, selectionAnimationDuration)
                .changeDuration(SortingItemState.AnimationKey.TextColor, selectionAnimationDuration)
                .changeDuration(SortingItemState.AnimationKey.BackgroundColor, selectionAnimationDuration)

                .forceValue(SortingItemState.AnimationKey.StartPosition, startOfView + itemSize * index)
                .forceValue(SortingItemState.AnimationKey.TopPosition, topOfView)
                .forceValue(SortingItemState.AnimationKey.SelectedSize, 0f)
                .forceValue(SortingItemState.AnimationKey.StrokeWidth, defaultStrokeWidth)
                .forceValue(SortingItemState.AnimationKey.StrokeColor, itemColor)
                .forceValue(SortingItemState.AnimationKey.TextColor, defaultTextColor)
                .forceValue(SortingItemState.AnimationKey.BackgroundColor, itemColor)

        }
    }

    private fun clearCallbacks() {
        removeCallbacks(restartAnimationDelayedRunnable)
    }

    private fun checkArraySize(intArray: IntArray) {
        val arraySize = intArray.size
        val minItemSize = 2
        val maxItemSize = 8
        if (arraySize < minItemSize || arraySize > maxItemSize) {
            throw IllegalArgumentException("The array $intArray has an unsuitable size: ${intArray.size}, it must have a size between $minItemSize and $maxItemSize")
        }
    }

    private inline fun <T> MutableList<T>.matchWithSize(newSize: Int, init: (Int) -> T) {
        val step = (newSize - size).sign
        if (step == 0) return

        var index = size - 1
        val lastNewIndex = newSize - 1
        while (true) {
            if (index == lastNewIndex) break

            if (step > 0) {
                val nextIndex = index + 1
                val nextItem = init.invoke(nextIndex)
                add(nextItem)
            } else {
                removeAt(index)
            }

            index += step
        }
    }

    private enum class SortingAnimationState {
        ANIMATION_STOPPED,
        ANIMATION_RUNNING,
        ANIMATION_PAUSED
    }

    private enum class HandleStepStatus(val priority: Int) {
        ANIMATION_INVALIDATE(9),
        JUST_INVALIDATE(8),
        NOT_INVALIDATE(7),
        ERROR_INVALIDATE(10)
    }

}