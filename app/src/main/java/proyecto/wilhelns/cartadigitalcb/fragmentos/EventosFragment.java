package proyecto.wilhelns.cartadigitalcb.fragmentos;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import proyecto.wilhelns.cartadigitalcb.R;


public class EventosFragment extends Fragment {


    public EventosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventos_fragment, container, false);

        WebView w = (WebView)view.findViewById(R.id.webview);

        WebSettings webSettings = w.getSettings();
        webSettings.setJavaScriptEnabled(true);
        w.setWebViewClient(new WebViewClient());

        w.loadUrl("https://www.openbar.com.pe");
        return  view;
    }

}
