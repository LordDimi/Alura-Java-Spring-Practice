package com.aluracursos.screenmatch.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.aluracursos.screenmatch.models.DatosEpisodio;
import com.aluracursos.screenmatch.models.DatosSerie;
import com.aluracursos.screenmatch.models.DatosTemporada;
import com.aluracursos.screenmatch.models.Episodio;
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

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
        .flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        System.out.println("Los 5 episodios mejor evaluados son:");
        datosEpisodios.stream()
        .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
            .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
            .limit(5)
            .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
            .flatMap(t->t.episodios().stream()
                .map(d->new Episodio(t.numero(),d)))
            .collect(Collectors.toList());

        episodios.forEach(System.out::println);
        
        System.out.println("Indica el año a partir del cual deseas ver los episodios:");
        int fecha = Integer.parseInt(System.console().readLine());

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        DateTimeFormatter dTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
            .filter(e->e.getFechaDeLanzamiento()!=null&&e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
            .forEach(e->System.out.println(
                "Temporada: "+e.getTemporada()+
                " Episodio: "+e.getTitulo()+
                " Fecha de lanzamiento: "+e.getFechaDeLanzamiento().format(dTimeFormatter)
            ));
            
    }

}
