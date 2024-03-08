import Confirmacion.Interfaz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TCK {

    public static void main(String[] args) {
        String numeros = "1,2,3,4,5,6";
        String[] arraynumeros= numeros.split(",");
        ArrayList<Integer> pares= new ArrayList<Integer>();
        ArrayList<Integer> impares= new ArrayList<Integer>();
        Map<String, ArrayList<Integer>> map= new HashMap<>();

        for (String numero : arraynumeros
             ) {
            int valor= Integer.parseInt(numero);
            if(valor %2==0){
                pares.add(valor);
            }else{
                impares.add(valor);
            }
        }
        map.put("pares",pares);
        map.put("impares",impares);

    }
}
