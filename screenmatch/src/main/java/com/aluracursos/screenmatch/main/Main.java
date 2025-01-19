package com.aluracursos.screenmatch.main;

import java.util.ArrayList;
import java.util.List;

import com.aluracursos.screenmatch.models.DatosSerie;
import com.aluracursos.screenmatch.models.DatosTemporada;
import com.aluracursos.screenmatch.services.APIConsumation;
import com.aluracursos.screenmatch.services.ConvierteDatos;

public class Main {

    private APIConsumation api = new APIConsumation();
    private final String URLBASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4fc7c187";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    
    public void muestraMenu() {
        System.out.println("Por favor, seleccione el nombre de la serie que deseas buscar:");
        var nombreSerie = System.console().readLine();
        var json = api.obtenerDatos(URLBASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);

        List<DatosTemporada> temporadas = new ArrayList<>(); 
		for(int i = 1; i <= datos.totalTemporadas(); i++) {
			json = api.obtenerDatos(URLBASE+nombreSerie.replace(" ", "+")+"&Season=" + i + API_KEY);
			var datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporada.class);
			temporadas.add(datosTemporada);
		}
		temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }

}
