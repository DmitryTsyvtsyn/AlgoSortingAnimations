package io.github.dmitrytsyvtsyn.algosortinganimations.core.theming.shape

interface ShapeTreatmentStrategy {
    fun floatArrayOf(radius: Float): FloatArray

    class None : ShapeTreatmentStrategy {
        override fun floatArrayOf(radius: Float) = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    }

    class AllRounded : ShapeTreatmentStrategy {
        override fun floatArrayOf(radius: Float) = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    }

    class AllElliptical : ShapeTreatmentStrategy {
        override fun floatArrayOf(radius: Float) = floatArrayOf(radius / 2f, radius, radius / 2f, radius, radius / 2f, radius, radius / 2f, radius)
    }

    class EndElliptical : ShapeTreatmentStrategy {
        override fun floatArrayOf(radius: Float) = floatArrayOf(0f, 0f, radius / 2f, radius, radius / 2f, radius, 0f, 0f)
    }

    class StartElliptical : ShapeTreatmentStrategy {
        override fun floatArrayOf(radius: Float) = floatArrayOf(radius / 2f, radius, 0f, 0f, 0f, 0f, radius / 2f, radius)
    }

    class StartBottomTopEndRounded : ShapeTreatmentStrategy {
        override fun floatArrayOf(radius: Float) = floatArrayOf(0f, 0f, radius, radius, 0f, 0f, radius, radius)
    }

}