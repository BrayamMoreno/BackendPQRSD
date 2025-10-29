package com.pqrsdf.pqrsdf.dto;

public class Mensaje {

    private String mensaje;

    public Mensaje(String mensaje){
        this.mensaje = mensaje;
    }

    public String getMensaje(){
        return this.mensaje;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }
}
