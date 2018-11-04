package megajdcc.sigpromeapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import android.app.ProgressDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *  La Clase MainActivity es la clase madre o raiz del proyecto Sisme app , aca es donde se inicializa la aplicaccion, es decir donde comienza todo ...
 *  @author Crespo jhonatan
 *  @author megajdcc2009@gmail.com
 *  @since 1.0
 */
public class MainActivity extends AppCompatActivity implements  Response.Listener<JSONObject>{
    JsonRequest jrq;
    private EditText user,contra;
     long cedulas;
     String contraseña,ipweb;
     Button entrar;
     Persona paciente;

    ProgressDialog progreso;
    Context c ;
    /**
     * El metodo onCreate, se inicializa cuando se llama por primera vez a la activitity en este caso y para explicarme bien a la vista deseada.
     * En este metodo es donde se ejecuntan la configuracion inicial de la vista...
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = getApplicationContext();
        entrar = findViewById(R.id.entrar);
        user = findViewById(R.id.cedula);
        contra = findViewById(R.id.clave);

        progreso = new ProgressDialog(this);
        progreso.setIndeterminate(false);
        progreso.setCancelable(false);
        progreso.setCanceledOnTouchOutside(false);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearse();
            }
        });
    }

    /**
     * El metodo loguearse, es una funcionalidad, implementada con la intencion y como su nombre lo dice loguearse identificarse en la aplicacion ...
     */
    private void loguearse(){



        progreso.setMessage("Iniciando sesión...");
        progreso.show();

        cedulas = Long.parseLong(user.getText().toString());
        contraseña = contra.getText().toString();
        ipweb  = getString(R.string.ipweb);

        String url = ipweb+"peticion=login&us="+cedulas+"&contra="+contraseña;

        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this, new Response.ErrorListener(){
            /**
             * El metodo onErrorResponse es de devolucion de llamada que indica que se ha producido un error con el codigo que se ha enviado en la solitud ...
             * @param error parametro del tipo Object VolleyError ...
             */

            @Override
            public void onErrorResponse(VolleyError error){

                progreso.hide();
                Toast.makeText(getApplication(), "No se ha podido Conectar "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

    );
        jrq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000 ;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Solicitud.getInstance(this).addToRequestQueue(jrq);
    }


    @Override
    public void onResponse(JSONObject response) {
            if(!response.isNull("datos")){
                try {
                    JSONArray datosjson = response.optJSONArray("datos");
                    JSONObject objectjson;
                    objectjson = datosjson.getJSONObject(0);

                    if(Long.parseLong(objectjson.optString("cedulapersona")) == 0){
                        System.out.println("incorrect");
                        progreso.hide();
                        Toast.makeText(getApplication(),"Datos Incorrectos",Toast.LENGTH_LONG).show();
                    }else{
                        String cedul = objectjson.optString("cedulapersona");

                        Long cedula = Long.parseLong(cedul);
                        progreso.setMessage("Verificado!, Iniciando...");
                        Intent principal =  new Intent();
                        principal.putExtra("CedulaPersona",cedula);
                        principal.setClass(getApplicationContext(), Principal.class);
                        principal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                        onNewIntent(principal);
                        progreso.hide();
                        startActivity(principal);
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                        progreso.hide();
                        Toast.makeText(getApplication(),"Datos Nulos",Toast.LENGTH_SHORT).show();
            }
        }


    }

