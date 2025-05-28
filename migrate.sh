#!/bin/bash

echo "🚀 Migrando AlexViewPro para usar GeckoView..."

APP_PATH="app"
SRC_PATH="$APP_PATH/src/main/java"
LAYOUT_PATH="$APP_PATH/src/main/res/layout"
PKG_PATH=$(find "$SRC_PATH" -type d -name "*alexviewpro*" | head -n 1)

if [[ ! -d "$PKG_PATH" ]]; then
  echo "❌ Não foi possível localizar o pacote da MainActivity. Verifique o nome do pacote."
  exit 1
fi

# 1. Adiciona GeckoView ao build.gradle
echo "📦 Adicionando GeckoView ao build.gradle..."
sed -i '/dependencies {/a \    implementation "org.mozilla.geckoview:geckoview:124.0.20240506093022"' $APP_PATH/build.gradle

# 2. Atualiza o layout
LAYOUT_FILE="$LAYOUT_PATH/activity_main.xml"
if [[ -f "$LAYOUT_FILE" ]]; then
  echo "🖼️ Substituindo WebView por GeckoView no layout..."
  sed -i 's/<WebView/<org.mozilla.geckoview.GeckoView/' "$LAYOUT_FILE"
  sed -i 's/<\/WebView>/<\/org.mozilla.geckoview.GeckoView>/' "$LAYOUT_FILE"
else
  echo "⚠️ Layout não encontrado. Criando novo layout com GeckoView..."
  mkdir -p "$LAYOUT_PATH"
  cat > "$LAYOUT_FILE" <<EOF
<?xml version="1.0" encoding="utf-8"?>
<org.mozilla.geckoview.GeckoView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/geckoView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
EOF
fi

# 3. Cria nova MainActivity.kt
MAIN_ACTIVITY="$PKG_PATH/MainActivity.kt"
echo "⚙️ Substituindo MainActivity com GeckoView..."
cat > "$MAIN_ACTIVITY" <<EOF
package $(basename "$PKG_PATH")

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoView

class MainActivity : AppCompatActivity() {
    private lateinit var session: GeckoSession
    private lateinit var runtime: GeckoRuntime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val geckoView = findViewById<GeckoView>(R.id.geckoView)
        runtime = GeckoRuntime.create(this)
        session = GeckoSession()
        session.open(runtime)
        geckoView.setSession(session)
        session.loadUri("https://duckduckgo.com")
    }
}
EOF

echo "✅ Migração concluída com sucesso!"
