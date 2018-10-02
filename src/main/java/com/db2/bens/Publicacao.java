package com.db2.bens;

public class Publicacao {
    private String texto;

    public Publicacao(String texto) {
        this.texto = texto;
    }

    public Publicacao() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
