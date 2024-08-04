package io.github.dmitrytsyvtsyn.algosortinganimations.main.data

class RandomArraysProducer(private val maxSize: Int) {

    fun randomArray(size: Int): IntArray {
        if (size > maxSize) throw IllegalArgumentException("The size: $size is more than maxSize: $maxSize")

        val newArray = IntArray(size) { it }

        newArray.shuffle()

        return newArray
    }

}