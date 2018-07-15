package megajdcc.sigpromeapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


/**
 *  La Clase MainActivity es la clase madre o raiz del proyecto Sisme app , aca es donde se inicializa la aplicaccion, es decir donde comienza todo ...
 *  @author Crespo jhonatan
 *  @author megajdcc2009@gmail.com
 *  @since 1.0
 */
public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    private JsonRequest jrq;
    private EditText user,contra;
    private long cedulas;
    private String contraseña,ipweb;
    private Button entrar;
    private Persona paciente;
    private TipoPersona tipopaciente;

    /**
     * El metodo onCreate, se inicializa cuando se llama por primera vez a la activitity en este caso y para explicarme bien a la vista deseada.
     * En este metodo es donde se ejecuntan la configuracion inicial de la vista...
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se invoca al metodo onCreate creado por los desarrolladores del mismo ya que no deseamos afectar la funcionalidad de lo que ellos hicieron ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//Establecemos el contenido de la actividad osea del objeto que deseamos que nos muestre y que ya hemos diseñado...
        entrar = (Button) findViewById(R.id.entrar);//Capturamos instancia del objeto Button 'Entrar'
        user = (EditText) findViewById(R.id.cedula);//Capturamos instancia del objeto EditText 'User'
        contra = (EditText) findViewById(R.id.clave);//Capturamos instancia del objeto EditText 'password'

        //Colocamos a la escucha lo que ocurra con el Button 'Entrar' de manera que cuando se presione ya tenga un oyente que accionará ciertas acciones...
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

        cedulas = Long.parseLong(user.getText().toString());// parseamo  o convertimos el objeto contenido en el EditText a entero ya que damos por entendido es la cedula de la persona.
        contraseña = contra.getText().toString(); // asignamos a la variable local contrasena el password digitado por el usuario ...
        ipweb  = getString(R.string.ipweb); // asignamos el recurso string  de la ruta a conectar via http ...

        String url = ipweb+"peticion=login&us="+cedulas+"&contra="+contraseña+""; // concatenamos la ruta  con la peticion exigida ...

        // Instanciamos un JsonobjectRequest con los respectivos parametros, el primero el metodo utilizado via http para la solicitud seguido de la ruta de la solitud,
        // Un objeto del tipo JsonObject, en este caso no es necesario por lo tal la colocamos en null, y los dos ultimos parametros hacen referencia a los listener o metodos de escuchas
        // que tomaran accion con la solicitud cuando se realiza con exito o no ...
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        Solicitud.getInstance(this).addToRequestQueue(jrq);//Utilizamos el metodo static de la clase Solicitud para ejecutar la instancia del JsonObjectRequest...
    }

    /**
     * El metodo onErrorResponse es de devolucion de llamada que indica que se ha producido un error con el codigo que se ha enviado en la solitud ...
     * @param error parametro del tipo Object VolleyError ...
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        // Mostramos en pantalla movil que los datos ingresados no son validos y espicificamos el error devuelto en la llamada...
        Toast.makeText(this, "Datos Ingresado no son validos", Toast.LENGTH_SHORT).show();
        // Y procedemos a Limpiar las cajas  tanto de usuario como de password...
        user.setText("");
        contra.setText("");
    }

    /**
     * Cuando se recibe una respuesta a la solicitud ya sea un conjunto de resultado en formato json o simplemnte un null, se invoca este metodo para indicar de que si se realizo
     * la llamada con exito ...
     * @param response  del type JsonObject ...
     * @exception JSONException
     */
    @Override
    public void onResponse(JSONObject response) {

        try {
            JSONArray datosjson = response.optJSONArray("datos");// obtenemos el objeto con el nombre datos del Json obtenido en la respuesta...
            JSONObject objectjson = null;//inicializamos un JsonObject en null, para despues utilizarlo al pasar los diferentes clave valor del json al lenguage java ...
            objectjson = datosjson.getJSONObject(0); // obtenemos el JsonObject de la posicion 0 en el json ...

            // Procedemos a asignar los valores de las claves dentro del JsonObject a las variables  correspondientes en java...
            String cedul = objectjson.optString("cedulapersona");
            Long cedula = Long.parseLong(cedul);

            // Levantamos instancias de la clase Persona pasandole como parametro al constructor de la clase la cedula obtenida en el json ...
            paciente = new Persona(cedula);
            // y procedemos a utilizar la function capturarDatos de la clase persona y le pasamos el contexto de la clase activity como parametro ...
            paciente.capturarDatos(this);

            // Levantamos instancia de Intent para indicar la intencion que existe de poder validarse y entrar en la aplicacion ...
                Intent principal =  new Intent();
            //  a la intencion le pasamos utilizando el metodo setClass el contexto de la clase activity  y la clase contenedora de la activity ...
                principal.setClass(this, Principal.class);
                // Esto es opcional todavia esta en estudio la utilizacion de este metodo ...
                principal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // a la activity origen le decimos que existe una nueva intencion preparada a la espera de ser inicializada ...
                this.onNewIntent(principal);
             // inicializamos la actividad principal donde ya esta activity pierde la referencia y la pricipal empieza a ser la base de todas las activity consiguiente ....
                startActivity(principal);

            // manejamos la exception imprimiendo la cadena del origen...
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.finish(); // El metodo finish se utiliza para limpiar toda referencia que pudiera existir a esta activity ...
    }

}
