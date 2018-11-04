package megajdcc.sigpromeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static java.security.AccessController.getContext;
import static megajdcc.sigpromeapp.R.layout.principal;

/**
 * Created by Jnatn'h on 3/5/2018.
 */

public class Persona{

    ProgressDialog progreso;
    Persona(long cedula){
        Persona.cedula = cedula;
    }
    Persona(){}
    //Metodos propios}
    public void capturarDatos(final Context ct){
        if(progreso == null){
            progreso = new ProgressDialog(ct);
            progreso.setMessage("Refrescando Datos");
        }else {

            progreso.setMessage("Filtrando Datos... ");
        }
        this.ct = ct;
        String donde = "InfPersonal";
        String ipweb = "http://www.megajdcc.com.ve/Sigprome/Peticion.php?";
        String url = ipweb+"peticion=infopersona&cedulaperson="+Persona.cedula;
        System.out.println(url);

        Response.Listener<JSONObject> respo = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                JSONArray datosjson = response.optJSONArray("datos");
                JSONObject objecjson;
                try {
                    objecjson = datosjson.getJSONObject(0);
                    System.out.println(objecjson.length());
                    Persona.setNombre(objecjson.optString("nombre"));
                    Persona.setTipopersona(objecjson.optString("tipopersona"));
                    Persona.setApellido(objecjson.optString("apellido"));
                    String cedul = objecjson.optString("cedula");
                    Persona.setCedula(Long.parseLong(cedul));
                    Persona.setFechanacimiento(objecjson.optString("fechanac"));
                    Persona.setTelefono(objecjson.optLong("telefono"));
                    Persona.setDireccion(objecjson.optString("direccion"));
                    char gen = objecjson.optString("genero").charAt(0);
                    Persona.setGenero(gen);
                    Persona.setCorreo(objecjson.getString("email"));
                    tipopaciente = new TipoPersona(Persona.getTipopersona());
                    progreso.hide();

                    Toast.makeText(ct, "Bienvenido " + Persona.getNombre() + " " + Persona.getApellido(), Toast.LENGTH_SHORT).show();

                    //  paciente= new Usuario(idus,this);


                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener respo1  = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Toast.makeText(ct, "Se cayo la conexión "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };


        jrq = new JsonObjectRequest(Request.Method.GET,url,null,respo,respo1);
        jrq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Solicitud.getInstance(this.ct).addToRequestQueue(jrq);
    }

    //Getters y Setters
    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Persona.id = id;
    }

    public static int getId_tipopersona() {
        return id_tipopersona;
    }

    public static void setId_tipopersona(int id_tipopersona) {
        Persona.id_tipopersona = id_tipopersona;
    }

    public static long getCedula() {
        return cedula;
    }

    public static void setCedula(long cedula) {
        Persona.cedula = cedula;
    }

    public static long getTelefono() {
        return telefono;
    }

    public static void setTelefono(long telefono) {
        Persona.telefono = telefono;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Persona.nombre = nombre;
    }

    public static String getApellido() {
        return apellido;
    }

    public static void setApellido(String apellido) {
        Persona.apellido = apellido;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion(String direccion) {
        Persona.direccion = direccion;
    }

    public static String getFechanacimiento() {
        return fechanacimiento;
    }

    public static void setFechanacimiento(String fechanacimiento) {
        Persona.fechanacimiento = fechanacimiento;
    }

    public static char getGenero() {
        return genero;
    }

    public static void setGenero(char genero) {
        Persona.genero = genero;
    }

    public static String getTipopersona() {
        return tipopersona;
    }

    public static void setTipopersona(String tipopersona) {
        Persona.tipopersona = tipopersona;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Persona.correo = correo;
    }
    public void setProgress(ProgressDialog progress){
        this.progreso = progress;
    }
    //Campos de clases...
    private static int id,id_tipopersona;
    private static long cedula;
    private static long telefono;
    private static String nombre,apellido,direccion,tipopersona,correo;
    private static String fechanacimiento;
    private static char genero;
    private TipoPersona tipopaciente;
    private Context ct;
    // Campos de Lib asociada a la clase..

    //private RequestQueue rq;
    private JsonRequest jrq;






}
