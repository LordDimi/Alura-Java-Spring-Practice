package com.aluracursos.screenmatch.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aluracursos.screenmatch.models.DatosEpisodio;
import com.aluracursos.screenmatch.models.DatosSerie;
import com.aluracursos.screenmatch.models.DatosTemporada;
import com.aluracursos.screenmatch.models.Episodio;
import com.aluracursos.screenmatch.services.APIConsumation;
import com.aluracursos.screenmatch.services.ConvierteDatos;

public class Main {

    private final APIConsumation api = new APIConsumation();
    private final String URLBASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4fc7c187";
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    
    public void muestraMenu() {
        System.out.println("Por favor, seleccione el nombre de la serie que deseas buscar:");
        var nombreSerie = System.console().readLine();
        getBestEpisodeByRating(setEpisode(nombreSerie));
        getBestSeasonByRating(setEpisode(nombreSerie));
    }

    private List<DatosTemporada> setSeries(String nombreSerie){
        var json = api.obtenerDatos(URLBASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);
        
        List<DatosTemporada> temporadas = new ArrayList<>(); 
		for(int i = 1; i <= datos.totalTemporadas(); i++) {
			json = api.obtenerDatos(URLBASE+nombreSerie.replace(" ", "+")+"&Season=" + i + API_KEY);
			var datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporada.class);
			temporadas.add(datosTemporada);
		}
        return temporadas;
    }

    private List<DatosEpisodio> setEpisodes(String nombreSerie){
        List<DatosTemporada> temporadas = setSeries(nombreSerie);
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
        .flatMap(t -> t.episodios().stream()).collect(Collectors.toList());
        return datosEpisodios;
    }

    private List<Episodio> setEpisode(String nombreSerie){
        List<DatosTemporada> temporadas = setSeries(nombreSerie);
        List<Episodio> episodio = temporadas.stream()
            .flatMap(t->t.episodios().stream()
                .map(d->new Episodio(t.numero(),d)))
            .collect(Collectors.toList());
        return episodio;
    }

    public void getEpisodiosPorAño(List<Episodio> episodios){
        System.out.println("Indica el año a partir del cual deseas ver los episodios:");
        int fecha = Integer.parseInt(System.console().readLine());

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        DateTimeFormatter dTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
            .filter(e->e.getFechaDeLanzamiento()!=null&&e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
            .forEach(e->System.out.println(
                "\n Temporada: "+e.getTemporada()+
                "\n Episodio: "+e.getTitulo()+
                "\n Fecha de lanzamiento: "+e.getFechaDeLanzamiento().format(dTimeFormatter)
            ));
    }

    public void getTop5Episodios(List<DatosEpisodio> datosEpisodios){
        System.out.println("Los 5 episodios mejor evaluados son:");
        datosEpisodios.stream()
            .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
            .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
            .map(e -> e.titulo().toUpperCase())
            .limit(5)
            .forEach(System.out::println);
    }

    public void getEpisodeByName(List<Episodio> episodios){
        System.out.println("Indica el nombre del episodio que deseas buscar:");
        String pedazoTitulo = System.console().readLine();
        DateTimeFormatter dTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
            .filter(e->e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
            .findFirst()
            .ifPresentOrElse(e->System.out.println(
                "\n Temporada: "+e.getTemporada()+
                "\n Episodio: "+e.getTitulo()+
                "\n Fecha de lanzamiento: "+e.getFechaDeLanzamiento().format(dTimeFormatter)
            ),()->System.out.println("No se encontró el episodio"));
    }

    public void getBestSeasonByRating(List<Episodio> episodios){
        Map<Integer , Double> evaluacionesPorTemporada = episodios.stream()
            .filter(e -> e.getEvaluacion() > 0.0)
            .collect(Collectors.groupingBy(Episodio::getTemporada,
                Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(
            "La mejor serie es la temporada: "+evaluacionesPorTemporada.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue)).get().getKey()
            +" con una evaluacion de: "+evaluacionesPorTemporada.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue)).get().getValue());

    }

    public void getBestEpisodeByRating(List<Episodio> episodios){
        DoubleSummaryStatistics statistics = episodios.stream()
            .filter(e -> e.getEvaluacion() > 0.0)
            .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));

        System.out.println("El episodio mejor evaluado es: " + episodios.stream()
            .filter(e -> e.getEvaluacion() == statistics.getMax())
            .findFirst().get().getTitulo() 
            + " de la temporada: " + episodios.stream().filter(e -> e.getEvaluacion() == statistics.getMax())
            .findFirst().get().getTemporada()+ " con una evaluación de: "+statistics.getMax() );
    }
}
