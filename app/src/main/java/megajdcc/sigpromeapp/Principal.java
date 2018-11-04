package megajdcc.sigpromeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import megajdcc.sigpromeapp.R;

/**
 * Created by Jnatn'h on 3/5/2018.
 */

public class Principal  extends AppCompatActivity {
    Persona paciente;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        progress = new ProgressDialog(this);
        progress.setIndeterminate(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos, por favor espere...");
        progress.show();
        Intent princi = getIntent();
        Long cedula = princi.getLongExtra("CedulaPersona",0);
        paciente = new Persona(cedula);
        paciente.setProgress(progress);
        paciente.capturarDatos(this);

        menu = (Toolbar) findViewById(R.id.menuprincipal);
        setSupportActionBar(menu);
        menu.setLogo(R.mipmap.ic_launcher);
        Serv = findViewById(R.id.servpendientes);
        cita = findViewById(R.id.gestcitas);
        historia =  findViewById(R.id.histmedico);





//        if(Persona.getCedula() < 1){
//            progress.setMessage("Cargando Datos, espere.!!!");
//            progress.show();
//        }else{
//            progress.hide();
//        }
//        String tipoperson = Persona.getApellido();
//        System.out.println(tipoperson);
//        if(Persona.getTipopersona().equalsIgnoreCase("Estudiante")){
//            Serv.setEnabled(false);
//
//        }


        inf  = findViewById(R.id.infpersonal);


        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaHistorial();
            }
        });
        inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vistaInfo();
            }
        });
        Serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vistaServi();
            }
        });
        cita.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                    vistaCita();
            }
        });
    }

    public void vistaInfo(){
        Intent infoPerson = new Intent();
        infoPerson.setClass(this,InfoPersonal.class);
        infoPerson.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.onNewIntent(infoPerson);
        startActivity(infoPerson);
    }
    private void vistaServi(){
        Intent servicio = new Intent();
        servicio.setClass(this,Servicios.class);
        servicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.onNewIntent(servicio);
        startActivity(servicio);
    }
    private void vistaHistorial(){
        Intent historial = new Intent();
        historial.setClass(this,Historial.class);
        historial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.onNewIntent(historial);
        startActivity(historial);
    }

    private void vistaCita(){
        Intent cita = new Intent();
        cita.setClass(this,Cita.class);
        cita.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.onNewIntent(cita);
        startActivity(cita);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
      getMenuInflater().inflate(R.menu.menu_sigprome,menu);
      return true;
    }

    //Cmapos de clases
    private Toolbar menu;
    private Button Serv,inf,historia,cita;
}
