import android.content.Context;
import android.os.Environment;
import android.net.Uri;
import android.app.DownloadManager;
import android.webkit.DownloadListener;
package com.alexviewpro.app;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        searchEditText = findViewById(R.id.searchEditText);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // Define ação quando o usuário pressiona "Enter" na barra de pesquisa
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || 
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    
                    String query = searchEditText.getText().toString().trim();
                    if (!query.isEmpty()) {
                        // Pesquisa no DuckDuckGo
                        String url = "https://duckduckgo.com/?q=" + query.replace(" ", "+");
                        webView.loadUrl(url);
                    }
                    return true;
                }
                return false;
            }
        });

        // Carrega a página inicial DuckDuckGo no WebView
        webView.loadUrl("https://duckduckgo.com/");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
