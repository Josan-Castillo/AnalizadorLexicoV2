package fes.aragon.modulos.lexico;

import fes.aragon.herramientas.ErrorLexico;
import fes.aragon.herramientas.Herramientas;
import fes.aragon.herramientas.RangoDeValores;
import java.io.IOException;
import java.util.ArrayList;

public class Lexico {
    private ArrayList<String> codigo;
    private ArrayList<ErrorLexico> errores;
    private ArrayList<Simbolo> simbolos;
    private String[] palabrasReservadas;
    private int numeroLinea;
    private int numeroErrores;
    private int estado;
    private int indiceSimbolos;
    private String tokenReconocidos;
    private Herramientas hr;
    private char simbolo;
    
    public Lexico() throws ErrorLexico{
        hr = new Herramientas();
        this.numeroLinea = 0;
        this.numeroErrores = 0;
        this.tokenReconocidos = "";
        this.estado = 0;
        this.indiceSimbolos++;
        this.errores = new ArrayList<>();
        this.simbolos=new ArrayList<>();
        try {
            this.codigo = hr.lectura();
            this.crearTablas();
            hr.setLinea(codigo.get(this.numeroLinea));
            simbolo = hr.siguienteCaracter();
            
        } catch (IOException ex) {
            numeroErrores++;
            this.errores.add(new ErrorLexico("Error en el archivo", hr.getColumnaLinea() + 1, numeroLinea + 1));
            throw new ErrorLexico("Error en el archivo");
        }
    }
    private void crearTablas(){
        this.palabrasReservadas = new String[8];
        this.palabrasReservadas[0] = "inicio";
        this.palabrasReservadas[1] = "fin";
        this.palabrasReservadas[2] = "entero";
        this.palabrasReservadas[3] = "real";
        this.palabrasReservadas[4] = "mientras";
        this.palabrasReservadas[5] = "fin-mientras";
        this.palabrasReservadas[6] = "si";
        this.palabrasReservadas[7] = "si-no";
    }
    public Token siguienteToken(){
        boolean tokenEncontrado = false;
        Token t = null;
        Simbolo s = null;
        String palabra = "";
        
        while (numeroLinea < this.codigo.size() && numeroErrores < 5 && tokenEncontrado == false) {
            switch(this.estado){
                case 0:
                    if(simbolo == ' ' || simbolo == '\t'){
                        this.estado = 0;
                        this.numeroLinea++;
                        if(numeroLinea<this.codigo.size()){
                            hr.setLinea(codigo.get(this.numeroLinea));
                            hr.setColumnaLinea(0);
                            simbolo = hr.siguienteCaracter();
                        } else {
                            break;
                        }   
                    } else if (simbolo == ':'){
                        this.estado = 9;
                    } else if (RangoDeValores.es_Letra(simbolo)){
                        this.estado = 1;
                    //} else if (RangoDeValores.es_NumeroEnteroPositivo(simbolo)){
                        //this.estado = 3;
                    }
                    break;
                case 1:
                    do {
                        if(RangoDeValores.es_Letra(simbolo) || RangoDeValores.es_NumeroEnteroPositivo(simbolo)){
                            palabra += simbolo;
                        }
                        simbolo = hr.siguienteCaracter();
                    } while(RangoDeValores.es_Letra(simbolo) || RangoDeValores.es_NumeroEnteroPositivo(simbolo));
                    this.estado=2;
                    break;
                case 2:
                    this.estado=0;
                    tokenEncontrado = true;
                    t = IdentificadorDeAsignarId(palabra);
                    break;
                /*case 3:
                    do {
                        if(RangoDeValores.es_NumeroEnteroPositivo(simbolo)){
                            palabra += simbolo;
                        }
                        simbolo = hr.siguienteCaracter();
                    } while(RangoDeValores.es_NumeroEnteroPositivo(simbolo));
                    if(simbolo == '.'){
                       this.estado = 5; 
                    } else {
                        this.estado = 4;
                    }
                    break;
                case 4:
                    this.estado=0;
                    tokenEncontrado = true;
                    t = IdentificadorDeAsignarId(palabra);
                    break;
                case 5:
                    */
                case 9:
                    palabra+=simbolo;
                    simbolo = hr.siguienteCaracter();
                    if(simbolo == '='){
                       this.estado=10; 
                    } else {
                        this.estado = 0;
                        ErrorLexico error = new ErrorLexico("Expresión mal estructurada", hr.getColumnaLinea()+1, numeroLinea+1);
                        this.errores.add(error);
                        this.numeroErrores++;
                    }
                    break;
                case 10:
                    this.estado = 0;
                    tokenEncontrado = true;
                    palabra += simbolo;
                    simbolo = hr.siguienteCaracter();
                    t = new Token(0, hr.getColumnaLinea()+1, numeroLinea+1, -1, palabra, palabra);
                    tokenReconocidos += t.getToken()+"\n";
                    break;
            }
        }
        return t;
    }
    private Token IdentificadorDeAsignarId(String palabra){
        String tipoToken = "";
        Token t = null;
        Simbolo s = null;
        boolean encontrado = false;
        
        for(int i=0; i<this.palabrasReservadas.length;i++){
            if(palabra.equals(this.palabrasReservadas[i])){
                tipoToken = this.palabrasReservadas[i];
                encontrado = true;
                break;
            }
        }
        if(encontrado){
            t = new Token(0, hr.getColumnaLinea(), numeroLinea+1, -1, palabra, tipoToken);
            tokenReconocidos += t.getToken()+"\n";
        } else { 
            encontrado = false;
            if(palabra.length() < 16) {
                int j = 0;
                for (int i = 0; i < this.simbolos.size(); i++) {
                    if(palabra.equals(this.simbolos.get(i).getSimbolo())){
                        encontrado =true;
                        j = i;
                        break;
                    }
                }
                if(encontrado){
                   t = new Token(0, ((hr.getColumnaLinea()-palabra.length())+1), numeroLinea+1, j, palabra, "Identificador");
                    tokenReconocidos += t.getToken()+"\n";
                } else {
                    s = new Simbolo(1, ((hr.getColumnaLinea()-palabra.length())+1), numeroLinea+1, palabra, "", false, false);
                    simbolos.add(s);
                    t = new Token(1, ((hr.getColumnaLinea()-palabra.length())+1), numeroLinea+1, j, palabra, "Identificador");
                    tokenReconocidos += t.getToken()+"\n";
                }
            } else {
                ErrorLexico error = new ErrorLexico("Identificador demasiado largo",((hr.getColumnaLinea()-palabra.length())+1), numeroLinea+1);
                this.errores.add(error);
                this.numeroErrores++;
            }
        }
        return t;
    }
    public String getTokenReconocidos() {
        return tokenReconocidos;
    }
    public void setTokenReconocidos(String tokenReconocidos) {
        this.tokenReconocidos = tokenReconocidos;
    }
    public ArrayList<ErrorLexico> getErrores() {
        return errores;
    }

    public void setErrores(ArrayList<ErrorLexico> errores) {
        this.errores = errores;
    }
    
}