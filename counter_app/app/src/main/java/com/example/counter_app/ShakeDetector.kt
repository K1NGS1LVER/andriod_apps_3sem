package com.example.counter_app

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * Detects shake gestures using the device accelerometer.
 *
 * @param onShake Callback invoked once per shake gesture (debounced).
 */
class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {

    private var lastShakeTime = 0L

    /** Minimum G-force required to register as a shake. */
    private val shakeThresholdG = 2.7f

    /** Minimum milliseconds between two consecutive shake events (debounce). */
    private val shakeSlopTimeMs = 500L

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Calculate resultant G-force across all axes
        val gForce = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH

        if (gForce > shakeThresholdG) {
            val now = System.currentTimeMillis()
            if (now - lastShakeTime > shakeSlopTimeMs) {
                lastShakeTime = now
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
