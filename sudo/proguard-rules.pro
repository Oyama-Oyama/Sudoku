# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-dontwarn
-dontnote
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
-allowaccessmodification

-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontwarn android.webkit.**
-keepattributes JavascriptInterface

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keep public class * extends com.badlogic.gdx.backends.android.AndroidApplication
-keep public class * extends android.view.View
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.appwidget.AppWidgetProvider
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.os.Parcelable
-keep public class * extends android.os.IInterface
-keep public class * extends android.os.Binder
-keep public class com.android.vending.licensing.ILicensingService

-keep class * extends java.util.ListResourceBundle {
	protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
	public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
	@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
	public static final ** CREATOR;
}

# AppCompat
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

#-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions,InnerClasses
-keepclassmembers class * {
	public <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object,com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}
-keep class * extends java.lang.Enum{
*;
}
-keep class org.dom4j.**{
*;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-dontwarn android.**
-keep class android.** { *;}


#Fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}

# Keep native methods
-keepclassmembers class com.facebook.** {
    native <methods>;
}

# Do not strip SoLoader class and init method
-keep public class com.facebook.soloader.SoLoader {
    public static void init(android.content.Context, int);
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

#Glide混淆文件
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Vungle
-dontwarn com.vungle.warren.downloader.DownloadRequestMediator$Status
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Google
-dontwarn com.google.android.gms.common.GoogleApiAvailabilityLight
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient$Info

# Moat SDK
-keep class com.moat.** { *; }
-dontwarn com.moat.**

# GSON
-keepattributes *Annotation*
-keepattributes Signature
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# OkHttp + Okio
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# pangle
-keep class com.bytedance.sdk.** { *; }
-keep class com.pgl.sys.ces.* {*;}

#IronSource
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# For removing warnings due to lack of Multi-Window support
-dontwarn android.app.Activity