package fes.aragon.test;

import fes.aragon.herramientas.ErrorLexico;
import fes.aragon.modulos.lexico.Lexico;
import fes.aragon.modulos.lexico.Token;

public class TestLexico {
    public static void main(String[] args) {
        try {
            Lexico lexico = new Lexico();
            Token t = null;
            do {
                t = lexico.siguienteToken();
            }while(t!=null);  
            System.out.println(lexico.getTokenReconocidos());
            System.out.println(lexico.getErrores());
        } catch (ErrorLexico ex){
            ex.printStackTrace();
        }
    }    
}