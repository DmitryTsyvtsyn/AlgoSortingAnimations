package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

class SortingItemState(
    private var text: String = "",
    private var strokeColor: Int = 0
) {

    private val startPositions = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
    private val topPositions = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
    private val strokeWidthParams = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f))

    val title: String
        get() = text

    val isAnimationRunning: Boolean
        get() = startPositions.size > 1 || topPositions.size > 1 || strokeWidthParams.size > 1

    fun cancelAnimation(): SortingItemState {
        startPositions.reset()
        topPositions.reset()
        strokeWidthParams.reset()
        return this
    }

    private var animationEndListener: (() -> Unit)? = null
    fun addOnAnimationEnd(listener: () -> Unit) {
        animationEndListener = listener
    }

    fun forcePosition(startPosition: Float = startPositions.peek(), topPosition: Float = topPositions.peek()): SortingItemState {
        animationEndListener = null
        startPositions.forcePush(startPosition)
        topPositions.forcePush(topPosition)
        return this
    }
    fun addPosition(startPosition: Float = startPositions.peek(), topPosition: Float = topPositions.peek()): SortingItemState {
        startPositions.push(startPosition)
        topPositions.push(topPosition)
        return this
    }
    fun startPosition() = startPositions.peek()
    fun animatedStartPosition(fraction: Float = 1f): Float {
        checkAnimationEndedListener()
        return startPositions.pop(fraction)
    }
    fun topPosition() = topPositions.peek()
    fun animatedTopPosition(fraction: Float = 1f): Float {
        checkAnimationEndedListener()
        return topPositions.pop(fraction)
    }

    fun forceStrokeWidth(strokeWidth: Float): SortingItemState {
        animationEndListener = null
        strokeWidthParams.forcePush(strokeWidth)
        return this
    }
    fun addStrokeWidth(strokeWidth: Float): SortingItemState {
        strokeWidthParams.push(strokeWidth)
        return this
    }
    fun animatedStrokeWidth(fraction: Float = 1f): Float {
        checkAnimationEndedListener()
        return strokeWidthParams.pop(fraction)
    }

    fun changeTitle(title: String): SortingItemState {
        text = title
        return this
    }

    fun changeStrokeColor(color: Int) {
        strokeColor = color
    }
    fun strokeColor() = strokeColor

    private fun checkAnimationEndedListener() {
        if (!isAnimationRunning) {
            animationEndListener?.invoke()
            animationEndListener = null
        }
    }

}