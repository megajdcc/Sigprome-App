package megajdcc.sigpromeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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

import static megajdcc.sigpromeapp.R.*;

/**
 * Created by Jnatn'h on 14/5/2018.
 */

public class Servicios extends AppCompatActivity {
    private String ipweb ;
    // Campos de clases ...
    private Toolbar menu;
    private Date fecha;
    private String opcion,estado;
    private EditText fech,opc,est;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.servicios);

        menu = (Toolbar) findViewById(id.menuservicio);
        setSupportActionBar(menu);
        menu.setNavigationContentDescription("Volver");
        menu.setTitle("Servicios");
        menu.setLogo(mipmap.ic_launcher);
        menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        this.asignarServicios();
    }

    private void asignarServicios(){
        final Context contex = (Context) this;
       // RequestQueue rq = Volley.newRequestQueue(this,null);
        ipweb= getString(string.ipweb);
        String url = ipweb+"peticion=Servicios&cedulaperson="+Persona.getCedula()+"";
        Response.Listener<JSONObject> respo = new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            public void onResponse(JSONObject response) {

                JSONArray datosjson = response.optJSONArray("datos");
                JSONObject objectjson = null;
                int cant = datosjson.length();
                LinearLayout conten = (LinearLayout) findViewById(id.Contenido);
                TextInputEditText opcion = (TextInputEditText) findViewById(id.opcion);
                TextInputEditText fecha = (TextInputEditText) findViewById(id.fecha);
                TextInputEditText estado = (TextInputEditText) findViewById(id.estado);
                TextView cantServi = (TextView) findViewById(id.cantServi);
                cantServi.setText(cantServi.getText() + " "+ cant + " Pendientes");

                try {
                        for (int i =0; i<cant;i++) {
                        objectjson = datosjson.getJSONObject(i);

                            estado = new TextInputEditText(conten.getContext());
                            estado.setHint("Estado");
                            estado.setTextSize(14);
                            estado.setHighlightColor(color.colorAccent);
//                            estado.setHintTextColor(color.colorAccent);

                            estado.setFocusable(false);

                            estado.setText(objectjson.optString("estado"));
                            conten.addView(estado, i);

                            opcion = new TextInputEditText(conten.getContext());
                            opcion.setHint("Opción");
                            opcion.setTextSize(14);
                            opcion.setHintTextColor(color.colorAccent);
                            opcion.setFocusable(false);
                            opcion.setText(objectjson.optString("opcion"));
                            conten.addView(opcion, i);
//
//                            fecha = new TextInputEditText(conten.getContext());
//                            fecha.setHint("Opción");
//                            fecha.setTextSize(14);
//                            fecha.setHintTextColor(color.colorAccent);
//                            fecha.setFocusable(false);
//                            fecha.setText(objectjson.optString("fecha"));
//                            conten.addView(fecha , i);

                    }


                    //  paciente= new Usuario(idus,this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                conten.removeView( opcion1);
//                conten.removeView(fecha1);
//                conten.removeView(estado1);
            }

        };
        Response.ErrorListener respo1  = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contex, "Se cayo la conexión, fallo de red."+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        JsonRequest jrq = new JsonObjectRequest(Request.Method.GET,url,null,respo,respo1);
        Solicitud.getInstance(this).addToRequestQueue(jrq);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_servicio,menu);
        return true;
    }
}
