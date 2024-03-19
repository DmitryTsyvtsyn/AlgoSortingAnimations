package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

class SortingItemState(
    private var text: String = "",
    private var bgColor: Int = 0
) {

    private var isValid = true

    private val animatedProperties: Array<AnimatedArray<*>> = arrayOf(
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
    )

    val title: String
        get() = text

    val isAnimationRunning: Boolean
        get() = animatedProperties.any { it.size > 1 }

    fun cancelAnimation(): SortingItemState {
        animatedProperties.forEach { it.reset() }
        return this
    }

    private var animationEndListener: (() -> Unit)? = null
    fun addOnAnimationEnd(listener: () -> Unit) {
        isValid = false
        animationEndListener = listener
    }

    fun <T> forceValue(key: AnimationKey<T>, value: T): SortingItemState {
        isValid = true
        animationEndListener = null
        (animatedProperties[key.key] as AnimatedArray<T>).forcePush(value)
        return this
    }

    fun <T> addValue(key: AnimationKey<T>, value: T): SortingItemState {
        (animatedProperties[key.key] as AnimatedArray<T>).push(value)
        return this
    }

    fun <T> addLastValue(key: AnimationKey<T>): SortingItemState {
        val animatedArray = animatedProperties[key.key] as AnimatedArray<T>
        val lastValue = animatedArray.peek()
        animatedArray.push(lastValue)
        return this
    }

    fun <T> value(key: AnimationKey<T>): T {
        return (animatedProperties[key.key] as AnimatedArray<T>).peek()
    }

    fun <T> animatedValue(key: AnimationKey<T>, fraction: Float): T {
        checkAnimationEndedListener()
        return (animatedProperties[key.key] as AnimatedArray<T>).pop(fraction)
    }

    fun changeTitle(title: String): SortingItemState {
        text = title
        return this
    }

    fun changeBgColor(color: Int): SortingItemState {
        bgColor = color
        return this
    }
    fun bgColor() = bgColor

    fun validate() {
        if (isValid) return

        finishAnimation()
        checkAnimationEndedListener()
        isValid = true
    }

    private fun finishAnimation() {
        if (isAnimationRunning) {
            animatedProperties.forEach { animatedArray -> animatedArray.pop(1f) }
        }
    }

    private fun checkAnimationEndedListener() {
        if (!isAnimationRunning) {
            animationEndListener?.invoke()
            animationEndListener = null
        }
    }

    sealed class AnimationKey<T>(val key: Int) {
        data object StartPosition : AnimationKey<Float>(0)
        data object TopPosition : AnimationKey<Float>(1)
        data object StrokeWidth : AnimationKey<Float>(2)
        data object SelectedSize : AnimationKey<Float>(3)
        data object TextColor : AnimationKey<Int>(4)
    }

}