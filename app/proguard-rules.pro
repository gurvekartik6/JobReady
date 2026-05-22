# JobRead ProGuard Rules

# Keep application class
-keep class com.jobread.app.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson
-keep class com.google.gson.** { *; }
-keepattributes *Annotation*

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-dontwarn androidx.room.paging.**

# Dagger
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter

# RxJava
-keep class io.reactivex.rxjava3.** { *; }
-dontwarn io.reactivex.rxjava3.**

# WorkManager
-keep class androidx.work.** { *; }

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Lottie
-keep class com.airbnb.lottie.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Google Drive API
-keep class com.google.api.** { *; }
-keep class com.google.apis.** { *; }
-dontwarn com.google.api.**

# Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Data models - prevent stripping
-keepclassmembers class com.jobread.app.models.** { *; }
-keepclassmembers class com.jobread.app.database.entities.** { *; }

# Suppress warnings
-dontwarn javax.annotation.**
-dontwarn sun.misc.**
