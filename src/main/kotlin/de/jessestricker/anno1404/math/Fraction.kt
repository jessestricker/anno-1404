package de.jessestricker.anno1404.math

import org.apache.commons.lang3.math.Fraction as ACLMFraction

@JvmInline
value class Fraction private constructor(private val value: ACLMFraction) {
    constructor(value: Int) : this(ACLMFraction.getFraction(value, 1))

    operator fun unaryMinus() = Fraction(value.negate())

    operator fun plus(other: Fraction) = Fraction(value.add(other.value))
    operator fun plus(other: Int) = this.plus(Fraction(other))

    operator fun minus(other: Fraction) = Fraction(value.subtract(other.value))
    operator fun minus(other: Int) = this.minus(Fraction(other))

    operator fun times(other: Fraction) = Fraction(value.multiplyBy(other.value))
    operator fun times(other: Int) = this.times(Fraction(other))

    operator fun div(other: Fraction) = Fraction(value.divideBy(other.value))
    operator fun div(other: Int) = this.div(Fraction(other))

    fun toDouble() = value.toDouble()
    fun toInt() = value.toInt()

    override fun toString() = value.toString()
}

fun Int.toFraction() = Fraction(this)
