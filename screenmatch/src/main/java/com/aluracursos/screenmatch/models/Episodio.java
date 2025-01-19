package com.aluracursos.screenmatch.models;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double evaluacion;
    private LocalDate fechaDeLanzamiento;

    public Episodio(Integer numero, DatosEpisodio datosEpisodio) {
        this.temporada = numero;
        this.titulo = datosEpisodio.titulo();
        this.numeroEpisodio = datosEpisodio.numeroEpisodio();
        try {
            this.evaluacion = Double.valueOf(datosEpisodio.evaluacion());
        } catch (NumberFormatException e) {
            this.evaluacion = 0.0;
        }
        try {
            this.fechaDeLanzamiento = LocalDate.parse(datosEpisodio.fechaDeLanzamiento());
        } catch (DateTimeException e) {
            this.fechaDeLanzamiento = null;
        }
    }
    public Integer getTemporada() {
        return temporada;
    }
    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }
    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }
    public Double getEvaluacion() {
        return evaluacion;
    }
    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }
    public LocalDate getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }
    public void setFechaDeLanzamiento(LocalDate fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    @Override
    public String toString() {
        return "Episodio "+numeroEpisodio+"[\ntemporada=" + temporada + ",\n titulo=" + titulo 
                + ",\n evaluacion=" + evaluacion + ",\n fechaDeLanzamiento=" + fechaDeLanzamiento + "\n]";
    }

    
}
