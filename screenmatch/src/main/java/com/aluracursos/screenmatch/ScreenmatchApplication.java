package com.aluracursos.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aluracursos.screenmatch.models.DatosSerie;
import com.aluracursos.screenmatch.services.APIConsumation;
import com.aluracursos.screenmatch.services.ConvierteDatos;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		APIConsumation api = new APIConsumation();
		String json = api.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&apikey=4fc7c187");
		ConvierteDatos convierteDatos = new ConvierteDatos();
		var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);
	}

}
