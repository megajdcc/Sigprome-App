package megajdcc.sigpromeapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;


public class Cita  extends AppCompatActivity {

    private Toolbar menu;
    DatePickerDialog post,fcit;
    Long cedula;
    Date fecha;
    Calendar c;
    JsonRequest jrq,jrq1,jrq2;
    ProgressBar progress;
    Button postponer;
    FloatingActionButton newcita;
    String fechapospuesta;
    TextView citaspendiente,fechapendiente;
    Boolean disponible;
    int idcita;
    ProgressDialog dialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cita);
        menu = (Toolbar) findViewById(R.id.menucita);
        setSupportActionBar(menu);
        menu.setNavigationContentDescription("Volver");
        menu.setTitle("Gestión de Citas");

        menu.setLogo(R.mipmap.ic_launcher);
        menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cedula = Persona.getCedula();
        postponer =  findViewById(R.id.btnpost);
        newcita =  findViewById(R.id.newcita);
        citaspendiente =  findViewById(R.id.tcitaspendiente);
        fechapendiente =  findViewById(R.id.fechapendiente);
        this.citapendiente();
        this.listarhistoria();

        newcita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = new GregorianCalendar();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int ano = c.get(Calendar.YEAR);

                fcit = new DatePickerDialog(Cita.this,android.R.style.Theme_Holo_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int myear, int month, int mday) {
                        dialogo = new ProgressDialog(Cita.this);
                        dialogo.setMessage("Verificando Disponibilidad");
                        dialogo.setIndeterminate(false);
                        dialogo.setCancelable(false);
                        dialogo.setCanceledOnTouchOutside(false);
                        dialogo.show();
                        String ipweb = getString(R.string.ipweb);
                        month +=1;
                        fechapospuesta = myear + "-" +month+ "-" + mday;
                        String url = ipweb + "peticion=verificarfecha&fecha=" + fechapospuesta;

                        jrq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if(!response.isNull("datos")){
                                    JSONArray datosjson = response.optJSONArray("datos");
                                    JSONObject objectjson;

                                    if(datosjson.isNull(0)){
                                        Toast.makeText(getApplication(),"No se pudo verificar la fecha...",Toast.LENGTH_SHORT).show();
                                    }else{
                                        try {
                                            objectjson = datosjson.getJSONObject(0);
                                            int cuenta = Integer.parseInt(objectjson.optString("cuenta"));

                                            System.out.println("Cuenta : "+cuenta);

                                            if(cuenta >= 0 && cuenta < 10 ){
//                                                Toast.makeText(getApplication(),"La fecha esta disponible...",Toast.LENGTH_SHORT).show();
                                                dialogo.setMessage("La fecha esta disponible, Actualizando...");

                                                asignarcita(fechapospuesta);
                                            }else{
                                                Toast.makeText(getApplication(),"La fecha no esta disponible, por favor seleccione otra fecha ...",Toast.LENGTH_SHORT).show();

                                                dialogo.setMessage("No se puede registrar la cita ");
                                                dialogo.hide();
                                             //   Cita.this.asignarcita(fechapospuesta);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }

                                }


                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast mensaje = new Toast(Cita.this);
                                mensaje.setDuration(Toast.LENGTH_LONG);
                                mensaje.setText("Hubo un error en el servidor ...");
                                mensaje.setGravity(Gravity.CENTER,0,0);
                                mensaje.show();
                            }
                        });

                        Solicitud.getInstance(Cita.this).addToRequestQueue(jrq);


                    };

                },day,mes,ano);

                fcit.setTitle("Seleccione la fecha de la cita...");
                fcit.setCancelable(false);
                fcit.setCanceledOnTouchOutside(false);
                Calendar fechaactual = new GregorianCalendar();

                fcit.updateDate(fechaactual.get(Calendar.YEAR),fechaactual.get(Calendar.MONTH),fechaactual.get(Calendar.DAY_OF_MONTH));
                fcit.show();

            }
        });
        postponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = new GregorianCalendar();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int mes  = c.get(Calendar.MONTH);
                int ano = c.get(Calendar.YEAR);


                post = new DatePickerDialog(Cita.this,android.R.style.Theme_Holo_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int myear, int month, int mday) {
                        dialogo = new ProgressDialog(Cita.this);
                        dialogo.setIndeterminate(true);
                        dialogo.setCanceledOnTouchOutside(false);
                        dialogo.setCancelable(false);
                        dialogo.setMessage("Verificando Fecha..");
                        dialogo.show();
                        String ipweb  = getString(R.string.ipweb);
                        month += 1;
                        fechapospuesta = myear+"-"+month+"-"+mday;
                        String url = ipweb+"peticion=verificarfecha&fecha="+fechapospuesta;
                        System.out.println(url);

                        jrq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onResponse(JSONObject response) {
                                if(!response.isNull("datos")){
                                    try {
                                        JSONArray datosjson = response.optJSONArray("datos");
                                        JSONObject objectjson;

                                        if(datosjson.isNull(0)){
                                            Toast.makeText(getApplication(),"No se pudo verificar la fecha...",Toast.LENGTH_SHORT).show();
                                        }else{
                                            objectjson = datosjson.getJSONObject(0);

                                            int cuenta = Integer.parseInt(objectjson.optString("cuenta"));

                                            System.out.println("Cuenta : "+cuenta);

                                            if(cuenta >= 0 && cuenta < 10 ){
                                                dialogo.setMessage("Fecha Disponible, asignando fecha.");

                                                Cita.this.posponercita();

                                              //  Cita.this.asignarcita(fechapospuesta);
                                            }else{
                                                disponible = false;

                                                Toast.makeText(getApplication(),"Fecha no disponible, seleccione otra fecha",Toast.LENGTH_SHORT).show();
                                                dialogo.hide();
                                            }
                                        }
                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplication(),"Hubo un error al verificar la fecha de la cita. ",Toast.LENGTH_LONG).show();
                            }
                        });
                        jrq.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 40000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 40000;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });
                        Solicitud.getInstance(Cita.this).addToRequestQueue(jrq);




                    //    fechapendiente.setText(fechapospuesta);

                    }
                },day,mes,ano);
                post.setTitle("Fecha a posponer...");

                post.setCancelable(false);
                post.setCanceledOnTouchOutside(false);
                Calendar fechaactual = new GregorianCalendar();

                post.updateDate(fechaactual.get(Calendar.YEAR),fechaactual.get(Calendar.MONTH),fechaactual.get(Calendar.DAY_OF_MONTH));
                post.show();
            }
        });




    }

    private void asignarcita(String fechaelegida){
        String ip  = getString(R.string.ipweb);
        String direccion = ip+"peticion=asignarcita&cedula="+Persona.getCedula()+"&fecha="+fechaelegida;
        System.out.println(direccion);
        jrq1 = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if(!response.isNull("datos")){
                    JSONArray datosjson = response.optJSONArray("datos");
                    JSONObject objectjson;

                    if(datosjson.isNull(0)){
                        Toast.makeText(getApplication(),"Cita no registrada...",Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            objectjson = datosjson.getJSONObject(0);
                            if(objectjson.optBoolean("registrado")){
                                dialogo.hide();
                                fechapendiente.setText(fechapospuesta);
                                citaspendiente.setText("Citas Pendiente: "+1);
                                Toast.makeText(getApplication(),"Cita registrada...",Toast.LENGTH_SHORT).show();
                                postponer.setActivated(true);
                            //    Cita.this.listarhistoria();
                            }else{
                                dialogo.hide();
                                Toast.makeText(getApplication(),"Cita no registrada...",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Problema con la Petición al servidor...",Toast.LENGTH_SHORT).show();
            }
        });
        jrq1.setRetryPolicy(new RetryPolicy() {
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

        jrq1.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 40000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 40000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Solicitud.getInstance(this).addToRequestQueue(jrq1);
        dialogo.setMessage("Cita Registrada!!!");
        fechapendiente.setText(fechaelegida);
        citaspendiente.setText("Cita pendiente: "+1);

        dialogo.hide();
    }
    private void citapendiente(){
        String ipweb  = getString(R.string.ipweb);

        String url = ipweb+"peticion=consultacita&cedula="+this.cedula;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                if(!response.isNull("datos")){
                    try {
                        JSONArray datosjson = response.optJSONArray("datos");
                        JSONObject objectjson;

                        if(datosjson.isNull(0)){
                            Toast.makeText(getApplication(),"No tienes Citas pendiente.",Toast.LENGTH_SHORT).show();
                            postponer.setEnabled(false);
                            postponer.setActivated(false);
                            postponer.setClickable(false);
                        }else{
                            newcita.setEnabled(false);
                            newcita.hide();

                            objectjson = datosjson.getJSONObject(0);

                            idcita = Integer.parseInt(objectjson.optString("id"));

//                            GregorianCalendar fechapendient = new GregorianCalendar();
//                            try {
//                                fechapendient.setGregorianChange(new Date(DateFormat.getDateInstance().parse(objectjson.optString("fecha")).getTime()));
//
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//

                            citaspendiente.setText("Citas Pendiente: "+1);
//                            fechapendiente.setText(fechapendient.get(Calendar.DAY_OF_WEEK) + " del mes de "
//                            +fechapendient.getDisplayName(Calendar.MONTH,Calendar.LONG, new Locale("es","ve"))+ " del "+ fechapendient.get(Calendar.YEAR));
                        fechapendiente.setText(objectjson.optString("fecha"));
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Hubo un error con el servidor.",Toast.LENGTH_SHORT).show();
            }
        });
        Solicitud.getInstance(this).addToRequestQueue(jrq);

    }
    private void listarhistoria(){
        String ipweb  = getString(R.string.ipweb);

        String url = ipweb+"peticion=historialcita&cedula="+this.cedula;
        System.out.println(url);
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {
                if(!response.isNull("datos")){
                    try {
                        JSONArray datosjson = response.optJSONArray("datos");
                        JSONObject objectjson;

                        if(datosjson.isNull(0)){
                            Toast.makeText(getApplication(),"No tienes historial de citas.",Toast.LENGTH_SHORT).show();
                        }else{
                            objectjson = datosjson.getJSONObject(0);


                            LinearLayout listfecha = (LinearLayout) findViewById(R.id.listfecha);
                            LinearLayout listasignada = (LinearLayout) findViewById(R.id.listasistidida);

                            int cant = objectjson.length();

                            for (int i = 0; i < cant; i++){
                                objectjson = datosjson.getJSONObject(i);

                                TextView fechas = new TextView(Cita.this);
                                fechas.setText(objectjson.optString("fecha"));
                                fechas.setTextColor(R.color.negro);
                                fechas.setTextSize(14);
                                fechas.setGravity(Gravity.CENTER_HORIZONTAL);
                                listfecha.addView(fechas);

                                TextView asignadas = new TextView(Cita.this);
                                int valor = Integer.parseInt(objectjson.optString("procesada"));

                                if(valor == 1){
                                    asignadas.setText("Asistida");
                                }else{
                                    asignadas.setText("No asistio");
                                }

                                asignadas.setTextColor(R.color.negro);
                                asignadas.setTextSize(14);
                                asignadas.setGravity(Gravity.CENTER_HORIZONTAL);
                                listasignada.addView(asignadas);


                            }
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Error en el resultado de la peticion al servidor...",Toast.LENGTH_SHORT).show();
            }
        });
//        jrq.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 40000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 40000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
        Solicitud.getInstance(this).addToRequestQueue(jrq);

    }
    private void posponercita(){
        String ip  = getString(R.string.ipweb);
        String direccion = ip+"peticion=posponercita&idcita="+idcita+"&fecha="+fechapospuesta;
        dialogo.setMessage("Posponiendo Cita...");
        jrq2 = new JsonObjectRequest(Request.Method.GET, direccion, null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                if(!response.isNull("datos")) {
                    JSONArray datosjson = response.optJSONArray("datos");
                    JSONObject objectjson;

                    if(datosjson.isNull(0)){
                        Toast.makeText(getApplication(),"No se pudo actualizr la fecha, error de servidor.",Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            objectjson = datosjson.getJSONObject(0);


                            if(objectjson.optBoolean("pospuesto")){
                                dialogo.hide();
                                fechapendiente.setText(fechapospuesta);
                                citaspendiente.setText("Citas Pendiente: "+1);
                                Toast.makeText(getApplication(),"Cita Pospuesta...",Toast.LENGTH_LONG).show();
                              //  Cita.this.listarhistoria();
                            }else{
                                dialogo.hide();
                                Toast.makeText(getApplication(),"No se pudo posponer la cita.",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Error en la petición al servidor...",Toast.LENGTH_SHORT).show();
            }
        });
        Solicitud.getInstance(Cita.this).addToRequestQueue(jrq2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cita,menu);
        return true;
    }
}
