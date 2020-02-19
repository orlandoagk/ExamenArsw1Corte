package edu.eci.arsw.exams.moneylaunderingapi.model;

public class SpringException extends Exception {
    public static final String RecursoNoEncontrado = "El recurso no se encontro";
    public SpringException(String text){
        super(text);
    }
}
