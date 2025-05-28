package com.alexviewpro.app;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText searchBar;
    private LinearLayout containerLayout;
    private WebView webView;
    private boolean hasMovedUp = false;
    private boolean isDesktop = false;
    private WebSettings settings;
    private SharedPreferences prefs;
    private Button btnBack, btnFav, btnDesktop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_bar);
        containerLayout = findViewById(R.id.container_layout);
        webView = findViewById(R.id.webview);
        btnBack = findViewById(R.id.btn_back);
        btnFav = findViewById(R.id.btn_fav);
        btnDesktop = findViewById(R.id.btn_desktop);
        prefs = getSharedPreferences("alexview_favs", MODE_PRIVATE);

        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !hasMovedUp) {
                animateSearchBarUp();
            }
        });

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchBar.getText().toString().trim();
                if (!query.startsWith("http")) {
                    query = "https://www.google.com/search?q=" + query.replace(" ", "+");
                }
                webView.loadUrl(query);
                hideKeyboard();
                if (!hasMovedUp) animateSearchBarUp();
                return true;
            }
            return false;
        });

        btnBack.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                Toast.makeText(this, "Nada para voltar", Toast.LENGTH_SHORT).show();
            }
        });

        btnFav.setOnClickListener(v -> {
            String url = webView.getUrl();
            prefs.edit().putString("last_fav", url).apply();
            Toast.makeText(this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
        });

        btnDesktop.setOnClickListener(v -> {
            isDesktop = !isDesktop;
            settings.setUseWideViewPort(isDesktop);
            settings.setLoadWithOverviewMode(isDesktop);
            settings.setUserAgentString(isDesktop
                    ? "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 Chrome/113.0.0.0 Safari/537.36"
                    : null);
            webView.reload();
            Toast.makeText(this, isDesktop ? "Versão Desktop" : "Versão Mobile", Toast.LENGTH_SHORT).show();
        });
    }

    private void animateSearchBarUp() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(containerLayout, "translationY", 0, -250);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        hasMovedUp = true;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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