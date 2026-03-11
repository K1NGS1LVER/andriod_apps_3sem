package com.danielpaul.financetracker.presentation.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object AnimationUtils {

    const val SHORT_DURATION = 200
    const val MEDIUM_DURATION = 350
    const val LONG_DURATION = 500

    val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val StandardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    fun <T> springSpec(
        dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        stiffness: Float = Spring.StiffnessMedium
    ): SpringSpec<T> = spring(dampingRatio = dampingRatio, stiffness = stiffness)

    fun <T> emphasizedTween(durationMillis: Int = MEDIUM_DURATION): TweenSpec<T> =
        tween(durationMillis = durationMillis, easing = EmphasizedEasing)

    fun <T> standardTween(durationMillis: Int = SHORT_DURATION): TweenSpec<T> =
        tween(durationMillis = durationMillis, easing = FastOutSlowInEasing)
}
