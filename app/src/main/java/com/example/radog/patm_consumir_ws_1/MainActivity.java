package com.example.radog.patm_consumir_ws_1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    String ciudad;
    String pais;
    String url;
    private RequestQueue qSolicitudes;

    @BindView(R.id.etPais) EditText etPais;
    @BindView(R.id.etCiudad) EditText etCiudad;
    @BindView(R.id.tvMensaje) TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ciudad = pais = "";
    }

    @Override
    protected void onStart() {
        super.onStart();
        qSolicitudes = Volley.newRequestQueue(this);
    }

    @OnClick(R.id.bntEnviar)
    public void btnEnviar() {
        tvMensaje.setText("");
        getWS();
    }

    private void getWS() {
        pais = etPais.getText().toString();
        ciudad = etCiudad.getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + ciudad + "," + pais + "&APPID=8c42dbbbe7d92583161f702e84fc5316";

        StringRequest solGETCte = new StringRequest(Request.Method.GET, url, this, this) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        qSolicitudes.add(solGETCte);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        tvMensaje.setText(error.toString());
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject objResponse = new JSONObject(response);
            JSONObject objCoord = new JSONObject(objResponse.getString("coord"));

            JSONArray arrWeather = new JSONArray(objResponse.getString("weather"));
            JSONObject objWeather = arrWeather.getJSONObject(0);

            JSONObject objMain = new JSONObject(objResponse.getString("main"));

            String reporte = objResponse.getString("name") +
                    "\nCódigo: " + objResponse.getString("cod") +
                    "\nLongitud: " + objCoord.getString("lon") +
                    "\nLatitud: " + objCoord.getString("lat") +
                    "\nClima: " + objWeather.getString("description") +
                    "\nHumedad: " + objMain.getString("humidity") + "%";

            tvMensaje.setText(reporte);

        } catch (Exception e) {
            tvMensaje.setText(e.toString());
        }
    }
}