package fes.aragon.herramientas;

public class RangoDeValores {
    public static boolean es_Letra(char letra){ //Retorna verdadero si es letra y falso si no lo es 
        boolean valido = false; 
        if((letra>=65 && letra<=90) || (letra>=97 && letra<=122)){ //Realiza comparaciones de acuerdo al codigo ASCII
                valido = true;
        }
        return valido;
    }
    
    public static boolean es_NumeroEnteroPositivo(char entero){ //Retorna verdadero si es un entero positivo y falso si no lo es 
        boolean valido = false; 
        if((entero>=48) && (entero<=57)){ //Realiza comparaciones de acuerdo al codigo ASCII
                valido = true;
        }
        return valido;
        }
    public static boolean fin_Cadena(char letra){
        boolean valido = false;
        if (letra == 59){
            valido = true;
        }
        return valido;
    }
}
