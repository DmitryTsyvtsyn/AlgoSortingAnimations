package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview


class SortingItemState(
    private var text: String = "",
    private var bgColor: Int = 0
) {

    private val animatedProperties: Array<AnimatedArray<*>> = arrayOf(
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
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
        animationEndListener = listener
    }

    fun <T> forceValue(key: AnimationKey<T>, value: T): SortingItemState {
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

    private fun checkAnimationEndedListener() {
        if (!isAnimationRunning) {
            animationEndListener?.invoke()
            animationEndListener = null
        }
    }

    sealed class AnimationKey<T>(val key: Int) {
        object StartPosition : AnimationKey<Float>(0)
        object TopPosition : AnimationKey<Float>(1)
        object StrokeWidth : AnimationKey<Float>(2)
        object SelectedSize : AnimationKey<Float>(3)
    }

}