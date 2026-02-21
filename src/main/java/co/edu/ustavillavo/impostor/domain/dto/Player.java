package co.edu.ustavillavo.impostor.domain.dto;

import java.util.UUID;

/*
    Clase Plana (Java puro) para la lógica del dto
    El Record es una clase especial de java en la que declaras atributos y esos atributos siempre serán finales (Inmodificables).
    También crea los getters para obtener los datos del record. No hay Setters.


*/



public record Player(
        UUID id,
        UUID roomId,
        String nickname,
        boolean alive
) { }
