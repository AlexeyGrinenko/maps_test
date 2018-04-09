package com.alexg.maps.utils

import android.animation.TypeEvaluator

class DoubleArrayEvaluator : TypeEvaluator<DoubleArray> {

    private var mArray: DoubleArray? = null

    /**
     * Create a DoubleArrayEvaluator that does not reuse the animated value. Care must be taken
     * when using this option because on every evaluation a new `double[]` will be
     * allocated.
     *
     * @see .DoubleArrayEvaluator
     */
    constructor() {}

    /**
     * Create a DoubleArrayEvaluator that reuses `reuseArray` for every evaluate() call.
     * Caution must be taken to ensure that the value returned from
     * [android.animation.ValueAnimator.getAnimatedValue] is not cached, modified, or
     * used across threads. The value will be modified on each `evaluate()` call.
     *
     * @param reuseArray The array to modify and return from `evaluate`.
     */
    constructor(reuseArray: DoubleArray) {
        mArray = reuseArray
    }

    /**
     * Interpolates the value at each index by the fraction. If
     * [.DoubleArrayEvaluator] was used to construct this object,
     * `reuseArray` will be returned, otherwise a new `double[]`
     * will be returned.
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start value.
     * @param endValue   The end value.
     * @return A `double[]` where each element is an interpolation between
     * the same index in startValue and endValue.
     */
    override fun evaluate(fraction: Float, startValue: DoubleArray, endValue: DoubleArray): DoubleArray {
        var array: DoubleArray? = mArray
        if (array == null) {
            array = DoubleArray(startValue.size)
        }

        for (i in 0..array.size-1) {
            val start = startValue[i]
            val end = endValue[i]
            array[i] = start + fraction * (end - start)
        }
        return array
    }
}
