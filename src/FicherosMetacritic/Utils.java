package FicherosMetacritic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static Date formatDate(String date) {
        date = date.replaceAll("\\s{2,}", " ");
        if (date.compareTo("") != 0) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH); // Formato: Jan 03, 2024
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH); // Formato: Thu Feb 28 00:00:00 WET 2019

            try {
                // Primero intenta con el formato "MMM dd, yyyy"
                return sdf1.parse(date);
            } catch (java.text.ParseException e1) {
                try {
                    // si falla, intenta con el formato que incluye la hora
                    return sdf2.parse(date);
                } catch (java.text.ParseException e2) {
                    System.out.println("La fecha no es válida. -> " + date);
                }
            }
        }
        System.out.println("Fecha vacía");
        return null; // Retorna null si la fecha no es válida o está vacía
    }
    public static boolean esArmstrong(int num){
        String numString = String.valueOf(num);
        int resultadoCalculo = 0;
        for (int i = 0; i < numString.length(); i++) { // ALGORITMO ARMSTRONG
            resultadoCalculo += (int) Math.pow(Character.getNumericValue(numString.charAt(i)), numString.length());
        }
        return num == resultadoCalculo;
    }
    public static boolean esPar(int num){
        return num%2==0;
    }
}
