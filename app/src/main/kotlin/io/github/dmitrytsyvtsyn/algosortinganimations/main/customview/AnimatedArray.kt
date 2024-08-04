package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

interface AnimatedArray<T> {
    val size: Int
    fun forcePush(value: T)
    fun push(value: T)
    fun reset()
    fun peek(): T
    fun pop(fraction: Float): T
}