package megajdcc.sigpromeapp;

/**
 * Created by Jnatn'h on 3/5/2018.
 */

public class TipoPersona {

    //Campos de clases ..
    private int id;
    private static String tipopersona;

    public TipoPersona(String tipopersona){
        TipoPersona.setTipoPersona(tipopersona);
    }
    public TipoPersona(int idtipoperson,String tipopersona){
        TipoPersona.setTipoPersona(tipopersona);
        this.id = idtipoperson;
    }
    public TipoPersona(int idtipoperson) {
        this.id = idtipoperson;
    }
    public static void setTipoPersona(String tipopersona){
        TipoPersona.tipopersona = tipopersona;
    }
    public static String getTipopersona(){
        return TipoPersona.tipopersona;
    }
}
