package co.edu.ustavillavo.impostor.domain.dto;

import co.edu.ustavillavo.impostor.domain.enums.Role;

import java.util.UUID;

/*
    Clase Plana (Java puro) para la lógica del dto
    El Record es una clase especial de java en la que declaras atributos y esos atributos siempre serán finales (Inmodificables).
    También crea los getters para obtener los datos del record. No hay Setters.


*/

public record Assignment(
        UUID id,
        UUID roomId,
        UUID playerId,
        Role role,
        String word
) {}
