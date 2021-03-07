# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\program\androidStudio\androidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
#############################################
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
#-optimizationpasses 5
#
## 混合时不使用大小写混合，混合后的类名为小写
#-dontusemixedcaseclassnames
#
## 指定不去忽略非公共库的类
#-dontskipnonpubliclibraryclasses
#
## 这句话能够使我们的项目混淆后产生映射文件
## 包含有类名->混淆后类名的映射关系
#-verbose
#
## 指定不去忽略非公共库的类成员
#-dontskipnonpubliclibraryclassmembers
#
## 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
#-dontpreverify
#
## 保留Annotation不混淆
#-keepattributes *Annotation*,InnerClasses
#
## 避免混淆泛型
#-keepattributes Signature
#
## 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable
#
## 指定混淆是采用的算法，后面的参数是一个过滤器
## 这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#
##############################################
##
## Android开发中一些需要保留的公共部分
##
##############################################
#
## 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
## 因为这些子类都有可能被外部调用
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Appliction
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService
#
## 保留support下的所有类及其内部类
#-keep class android.support.** {*;}
#
## 保留继承的
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.annotation.**
#
## 保留R下面的资源
#-keep class **.R$* {*;}
#
## 保留本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
## 保留在Activity中的方法参数是view的方法，
## 这样以来我们在layout中写的onClick就不会被影响
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}
#
## 保留枚举类不被混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
## 保留我们自定义控件（继承自View）不被混淆
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
## 保留Parcelable序列化类不被混淆
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
#
## 保留Serializable序列化的类不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
## 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}
#
## webView处理，项目中没有使用到webView忽略即可
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, jav.lang.String);
#}
#
#
##-------------------okhttp-----------------
#-dontwarn com.squareup.okhttp3.**
#-keep class com.squareup.okhttp3.** { *;}
#-dontwarn okio.**
#
##-------------------裁剪UCrop----------------
#-dontwarn com.yalantis.ucrop**
#-keep class com.yalantis.ucrop** { *; }
#-keep interface com.yalantis.ucrop** { *; }
#
##------------------Glide---------------------
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#
##------------------gson---------------------
##如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#-keepattributes Signature
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
## Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.** { *; }
#-keep class com.google.gson.stream.** { *; }
## 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
#-keep class yimeng.common.** { *; }
#-keep class yimeng.model.** { *; }

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-ignorewarnings # 抑制警告

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends com.imtanbao.modle.BaseHttpRequest      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.app.Dialog       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

# keep 泛型
-keepattributes Signature

-dontwarn org.springframework.**
-keep class org.springframework.** { *;}

-dontwarn cz.msebera.**
-keep class cz.msebera.** { *;}

-dontwarn java.awt.event.**
-keep class java.awt.event.** { *;}
-dontwarn com.sun.jna.**
-keep class com.sun.jna.** { *; }

-dontwarn javax.swing.event.**
-keep class javax.swing.event.** { *;}

-dontwarn java.nio.file.**
-keep class java.nio.file.** { *;}

-dontwarn org.apache.log4j.**
-keep class org.apache.log4j.** { *;}

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}

-dontwarn com.sun.**
-keep class com.sun.** { *;}

-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}

-dontwarn com.google.gson.jpush.**
-keep class com.google.gson.jpush.** { *;}

-dontwarn org.apache.http.conn.ssl.**
-keep class org.apache.http.conn.ssl.** { *;}

-dontwarn cz.msebera.android.**
-keep class cz.msebera.android.** { *;}

-dontwarn android.net.**
-keep class android.net.** { *;}

-dontwarn com.loopj.android.http.**
-keep public class com.loopj.android.http.** { *;}

-dontwarn com.google.**
-keep public class com.google.** { *;}

-dontwarn org.codehaus.mojo.**
-keep public class org.codehaus.mojo.** { *;}

-dontwarn com.baidu.**
-keep class com.baidu.** { *;}

-dontwarn android.app.**
-keep class android.app.** { *;}

-dontwarn com.alipay.**
-keep class com.alipay.** { *;}

-dontwarn de.mindpipe.android.logging.log4j.**
-keep class de.mindpipe.android.logging.log4j.** { *;}

-dontwarn com.qiniu.**
-keep class com.qiniu.** { *;}

-dontwarn com.jpush.**
-keep class com.jpush.** { *;}

-dontwarn com.jpush.**
-keep class com.jpush.** { *;}

-dontwarn com.tencent.**
-keep class com.tencent.** { *;}

-dontwarn com.mob.**
-keep class com.mob.** { *;}

-dontwarn okhttp3.**
-keep class okhttp3.** { *;}

-dontwarn okio.**
-keep class okio.** { *;}

-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.** { *;}

-dontwarn com.sun.**
-keep class com.sun.** { *;}

-dontwarn com.nostra13.**
-keep class com.nostra13.** { *;}

-dontwarn android.support.**
-keep class android.support.** { *;}

-dontwarn org.xutils.**
-keep class org.xutils.** { *;}

-dontwarn yimeng.common.**
-keep class yimeng.common.** { *;}

-dontwarn yimeng.model.**
-keep class yimeng.model.** { *;}
#腾讯buly的集成日志
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# 保留Annotation不混淆x
-keepattributes *Annotation*,InnerClasses
#-keep class *extends java.lang.annotation.Annotation {*;}
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }

#------------------gson---------------------
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
#这是你定义的实体类
-keep class yimeng.entity.**{*;}
-keep class yimeng.eventEntity.**{*;}

#-------------------okhttp-----------------
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#-------------------裁剪UCrop----------------
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#------------------Glide---------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#------------------webview---------------------
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**

#------------------faceAuth--------------------
-keep class com.face.** {*;}
-keep class cn.com.bsfit.** {*;}
-keep class com.android.snetjob.** {*;}
-keep class com.udcredit.** { *; }
-keep class com.authreal.** { *; }
-keep class com.hotvision.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.lianlian.face.** {*;}

#------------------rxjava-----------------------
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
#-keepattributes Signature-keepattributes
#-keepattributes Exceptions
-dontnote retrofit2.Platform
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor
#-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#-ButterKnife 7.0
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
  @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
 @butterknife.* <methods>;
 }

# 友盟
-keep class com.umeng.** {*;}
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class * extends com.umeng.socialize.net.base.SocializeReseponse {
   *;
}

# 信鸽
-keep class com.tencent.android.tpush.** {* ;}
-keep class com.tencent.mid.** {* ;}
-keep class com.qq.taf.jce.** {*;}
# 高德
## 定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
## 搜索
-keep   class com.amap.api.services.**{*;}
## 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
# litepal
-keep class org.litepal.** { *;}
-keep class * extends org.litepal.crud.DataSupport { *;}
-keep class * extends org.litepal.crud.LitePalSupport { *;}
# xpopup
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}

# 融合广告
-keep class com.touchxd.fusionsdk.** {*;}
-keep class com.baidu.** { *; }
-keep class com.qq.e.** {
    public protected *;
}
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.ss.sys.ces.* {*;}
-keep class com.dydroid.ads.**{*;}

-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {
    public <methods>;
}
-keep class org.chromium.** {*;}
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**