package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape

class ShapeStyle(
    private val small: Float,
    private val medium: Float,
    private val big: Float,
    private val max: Float = 100f
) {

    operator fun get(type: ShapeAttribute): Float {
        return when (type) {
            ShapeAttribute.small -> small
            ShapeAttribute.medium -> medium
            ShapeAttribute.big -> big
            ShapeAttribute.maximum -> max
        }
    }

}

