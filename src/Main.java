import java.util.Iterator;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class Main {
    public static void main(String[] args){
        String prueba = "Cosa 1,Cosa 2,Cosa 3";
        String[] array = prueba.split(",");
        System.out.println(array.length);
        for(int i = 0; i < array.length; i++)
            System.out.println(array[i]);

        //UI ui = new UI();

    }

}
