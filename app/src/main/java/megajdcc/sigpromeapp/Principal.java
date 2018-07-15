package megajdcc.sigpromeapp;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        menu = (Toolbar) findViewById(R.id.menuprincipal);
        setSupportActionBar(menu);
        menu.setLogo(R.mipmap.ic_launcher);
        Serv = (Button) findViewById(R.id.servpendientes);
        cita = (Button) findViewById(R.id.gestcitas);
        historia = (Button) findViewById(R.id.histmedico);
//        String tipoperson = Persona.getApellido();
//        System.out.println(tipoperson);
//        if(Persona.getTipopersona().equalsIgnoreCase("Estudiante")){
//            Serv.setEnabled(false);
//
//        }


        inf  = (Button) findViewById(R.id.infpersonal);


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
      getMenuInflater().inflate(R.menu.menu_sigprome,menu);
      return true;
    }

    //Cmapos de clases
    private Toolbar menu;
    private Button Serv,inf,historia,cita;
}
