package megajdcc.sigpromeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import megajdcc.sigpromeapp.R;

/**
 * Created by Jnatn'h on 23/5/2018.
 */

public class Historial extends AppCompatActivity {
    private Toolbar menu;
    String ipweb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historialmedico);

        menu = (Toolbar) findViewById(R.id.menuhistorial);
        setSupportActionBar(menu);
        menu.setNavigationContentDescription("Volver");
        menu.setTitle("Historial Médico");
        menu.setLogo(R.mipmap.ic_launcher);
        menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        fecha = (TextView) findViewById(R.id.ultimafech);
        talla = (TextView) findViewById(R.id.talla);
        peso = (TextView) findViewById(R.id.peso);
        presion = (TextView) findViewById(R.id.presion);

        glicemia = (TextView) findViewById(R.id.glicemia);
        sintoma = (EditText) findViewById(R.id.sintoma);
        diagnostico = (EditText) findViewById(R.id.diagnostico);
        diasreposo = (TextView) findViewById(R.id.diasreposo);
        motivoreposo = (EditText) findViewById(R.id.motivoreposo);

        this.capturardatos();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_historial,menu);
        return true;
    }
    private void capturardatos(){
        final Context contex = (Context) this;
     //   RequestQueue rq = Volley.newRequestQueue(this,null);
        ipweb =  getString(R.string.ipweb);
        String url = ipweb+"peticion=HistorialM&cedulaperson="+Persona.getCedula()+"";
        Response.Listener<JSONObject> respo = new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            public void onResponse(JSONObject response) {

                JSONArray datosjson = response.optJSONArray("datos");
                JSONObject objectjson = null;
                try {
                objectjson = datosjson.getJSONObject(0);
                    fecha.setText(objectjson.optString("ultimavisita"));
                    talla.setText(objectjson.optString("talla"));
                    peso.setText(objectjson.optString("peso"));
                    presion.setText(objectjson.optString("presion"));
                    glicemia.setText(objectjson.optString("glicemia"));
                    sintoma.setText(objectjson.optString("sintoma"));
                    diasreposo.setText(objectjson.optString("motivoreposo"));
                    diagnostico.setText(objectjson.optString("diagnostico"));
                    motivoreposo.setText(objectjson.optString("motivoreposo"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        Response.ErrorListener respo1  = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contex, "Se cayo la conexión, fallo de red."+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET,url,null,respo,respo1);
//        rq.add(jrq);
        Solicitud.getInstance(this).addToRequestQueue(jrq);
    }

    private TextView fecha,talla,peso,presion,glicemia,diasreposo;
    private EditText sintoma,diagnostico,motivoreposo;
}
