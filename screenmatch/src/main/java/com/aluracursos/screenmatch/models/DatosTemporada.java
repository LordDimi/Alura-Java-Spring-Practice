package com.aluracursos.screenmatch.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosTemporada(
    @JsonAlias("Season") Integer numero,
    @JsonAlias("Episodes") List<DatosEpisodio> episodios
) {

}
