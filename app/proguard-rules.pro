# Mantém WebViewClient e WebChromeClient para evitar problemas com minificação
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(...);
}

-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void *(...);
}
