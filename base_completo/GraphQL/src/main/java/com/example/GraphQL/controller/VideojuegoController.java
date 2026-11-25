package com.example.GraphQL.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VideojuegoController {

    record Estudio(String id, String nombre, String pais) {}
    record Videojuego(String id, String titulo, String genero, String estudioId) {}

    private final List<Estudio> estudios = List.of(
        new Estudio("1", "CD Projekt Red", "Polonia"),
        new Estudio("2", "Larian Studios", "Bélgica")
    );

    private final List<Videojuego> juegos = new ArrayList<>(List.of(
        new Videojuego("101", "The Witcher 3", "RPG", "1"),
        new Videojuego("102", "Baldur's Gate 3", "RPG", "2")
    ));

    @QueryMapping
    public List<Videojuego> videojuegos() {
        return juegos;
    }

    @QueryMapping
    public Videojuego videojuegoPorId(@Argument String id) {
        return juegos.stream()
                .filter(j -> j.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @SchemaMapping(typeName = "Videojuego", field = "estudio")
    public Estudio estudio(Videojuego juego) {
        return estudios.stream()
                .filter(e -> e.id().equals(juego.estudioId()))
                .findFirst()
                .orElse(null);
    }

    @MutationMapping
    public Videojuego agregarVideojuego(@Argument String titulo, @Argument String genero, @Argument String estudioId) {
        String nuevoId = String.valueOf(juegos.size() + 101);
        Videojuego nuevo = new Videojuego(nuevoId, titulo, genero, estudioId);
        juegos.add(nuevo);
        return nuevo;
    }
}