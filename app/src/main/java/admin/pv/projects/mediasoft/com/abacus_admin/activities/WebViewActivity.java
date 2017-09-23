package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;

public class WebViewActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private WebView webView;
    String url = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS) ;
        setContentView(R.layout.activity_web_view);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,Window.PROGRESS_VISIBILITY_ON);

        setupToolBar();

        webView = (WebView) findViewById(R.id.webview);

        Intent intent = getIntent() ;
        if (intent.hasExtra("url")) url = intent.getStringExtra("url") ;

        webView.canGoForward() ;
        webView.canGoBack() ;
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                setTitle(getString(R.string.loading));
                setProgress(newProgress * 100);
                if (newProgress == 100){
                    setTitle(R.string.app_name);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Url.getWebUrl(url));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        webView.loadUrl(Url.getWebUrl(url));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }



    private void setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
