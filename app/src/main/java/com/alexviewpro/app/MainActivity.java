package com.alexviewpro.app;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_bar);
        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        // Página inicial
        webView.loadUrl("https://www.google.com");

        // Listener para a barra de pesquisa (quando o usuário pressiona "search" no teclado)
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String input = v.getText().toString().trim();
                    String url;
                    if (input.startsWith("http://") || input.startsWith("https://")) {
                        url = input;
                    } else if (input.contains(".") && !input.contains(" ")) {
                        url = "https://" + input;
                    } else {
                        url = "https://www.google.com/search?q=" + input.replace(" ", "+");
                    }

                    webView.loadUrl(url);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}