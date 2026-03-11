# Proguard rules for FinanceTracker production build
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.InstallIn class * { *; }
# Kotlin Serialization / data classes used in Room
-keep class com.danielpaul.financetracker.data.local.** { *; }
-keepclassmembers class com.danielpaul.financetracker.data.local.** { *; }
