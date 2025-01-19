package com.aluracursos.screenmatch.services;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clazz) {
        
        try {
            return mapper.readValue(json, clazz);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
