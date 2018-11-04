package megajdcc.sigpromeapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jnatn'h on 3/5/2018.
 */

public class InfoPersonal extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    String cedul;
    EditText telf, correo;
    String ipweb;
    //RequestQueue request;
    JsonRequest jrq;
    private Toolbar menu;
    ProgressDialog progreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion_personal);

        menu = (Toolbar) findViewById(R.id.menuinfopersonal);
        setSupportActionBar(menu);
        menu.setNavigationContentDescription("Volver");
        TextInputLayout contdireccion = (TextInputLayout) findViewById(R.id.contdireccion);
        contdireccion.setFocusable(false);
        menu.setTitle("Info Personal");
        menu.setLogo(R.mipmap.ic_launcher);
        menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        EditText nomb = (EditText) findViewById(R.id.nombres);
        EditText apelli = (EditText) findViewById(R.id.apellidos);
        EditText cedula = (EditText) findViewById(R.id.inf_cedula);
        EditText fechanacimiento = (EditText) findViewById(R.id.fechaNacimiento);
        telf = (EditText) findViewById(R.id.telefono);
        EditText genero = (EditText) findViewById(R.id.genero);
        EditText direccion = (EditText) findViewById(R.id.direccion);
        correo = (EditText) findViewById(R.id.correo);

        String nombre = Persona.getNombre();
        String correoo = Persona.getCorreo();
        String apellido = Persona.getApellido();
        Long cedul1 = Persona.getCedula();
        cedul = String.valueOf(cedul1);
        Long telef = Persona.getTelefono();
        String telfo = telef.toString();
        telf.setText(telfo);
        cedula.setText(cedul);
        String gener = String.valueOf(Persona.getGenero());
        if(gener.equalsIgnoreCase("F")){
            gener = "Femenino";
        }else if(gener.equalsIgnoreCase("M")){
            gener = "Masculino";
        }
        String direcc = Persona.getDireccion();

        String fechan = Persona.getFechanacimiento().toString();

        fechanacimiento.setText(fechan);



        apelli.setText(apellido);
        nomb.setText(nombre);
        genero.setText(gener);
        direccion.setText(direcc);
        correo.setText(correoo);
        //capturarTelefono(cedul1);
        //this.request = Volley.newRequestQueue(this);
        Button edittelf = (Button) findViewById(R.id.editelf);
        Button editcorreo = (Button) findViewById(R.id.editcorreo);
        editcorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button grabar = (Button) findViewById(R.id.grabar);
                grabar.setEnabled(true);
                habilitarEdicionCorreo();
            }
        });
        edittelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habilitarEdicionTelefono();
                Button grabar = (Button) findViewById(R.id.grabar);
                grabar.setEnabled(true);
            }
        });
        telf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button grabar = (Button) findViewById(R.id.grabar);
                grabar.setEnabled(true);
            }
        });
        Button grabar = (Button) findViewById(R.id.grabar);

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                grabar();
            }
        });
    }
    public void grabar(){
        progreso = new ProgressDialog(this);
        progreso.setMessage("Registrando.");
        progreso.setCancelable(false);
        progreso.setCanceledOnTouchOutside(false);
        progreso.show();
        correo  = (EditText) findViewById(R.id.correo);
        String corr = correo.getText().toString();
        telf = (EditText) findViewById(R.id.telefono);
        Long telfono = Long.parseLong(telf.getText().toString());
        ipweb = getString(R.string.ipweb);
        String url = ipweb+"peticion=modifperson&cedulperson="+Persona.getCedula()+"&telefono="+telfono+"&correo="+corr+"";
        System.out.println(url);
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        jrq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 2000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
//        this.request.add(jrq);
        Solicitud.getInstance(this).addToRequestQueue(jrq);
    }
    private void capturarTelefono(Long telef){
      //  RequestQueue rq =

    }
    private void habilitarEdicionCorreo(){
        EditText correo = (EditText) findViewById(R.id.correo);
        correo.setEnabled(true);
    }

    public void habilitarEdicionTelefono(){
        EditText info_telef = (EditText) findViewById(R.id.telefono);
        info_telef.setEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_infopersonal,menu);
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this, "No se pudo actualizar los datos personales", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Persona perso = new Persona(Persona.getCedula());
        perso.capturarDatos(this);
        progreso.hide();
        Toast.makeText(this, "Datos personales modificados", Toast.LENGTH_SHORT).show();
    }
}
