
DTO (DATA TRANSFER OBJECT)
---
---

El dto es un objeto de transferencia de datos, esto significa que son objetos
cuyo único propósito ess llevar datos de una parte del proyecto a otra. En este caso
en específico usamos Records, los cuales son unas clases especiales en Java que nos
permite crear un dto de manera rápida sin necesidad de establecer qué es privado y qué
es público o incluso evitar establecer los getters y/o setters, incluso evita crear código
innecesario.
---
Clases DTO del Proyecto
---
- ## Clase Player

```json
{
  "player": {
    "id": "UUID",
    "roomId": "UUID",
    "nickname": "String",
    "alive": "boolean"
  }
}
```
Es record sencillo que tiene un ```id```,
un `roomId` que es un cuarto, un sobrenombre llamado
`nickname` y por último un estado de vivo o muerto 
`alive`, en el JSON de arriba podemos ver qué tipo de dato
es cada uno de esos atributos de la clase.

- ## Clase Room
```json
{
  "room": {
    "id": "UUID",
    "code": "String",
    "Status": "RoomStatus (enum)",
    "hostPlayerId": "UUID",
    "category": "String",
    "impostorCount": "int",
    "currentRound": "Byte",
    "secretWord": "String",
    "winnerTeam": "String"
  }
}
```
Esta clase representa un "cuarto" en donde están los jugadores o ```Player```,
tiene distintos atributtos tales como
`id` (identificador único),
`code`(código de la sala),
`status`(estado de la sala),
`hostPlayerId`(el ID del jugador que crea la partida)
`category`(la categoría que se usará para escoger la palabra),
`impostorCount`(la cantidad de impostores que hay en la partida),
`currentRound`(la ronda en la que se está actualmente)
`secretWord`(la palabra secreta de cada partida)
`winnerTeam`(el equipo ganador entre Civiles e Impostor)

- ## Clase Vote
```json
{
  "vote": {
     "roomId" : "UUID",
    "roundNumber" : "int" ,
    "voterId" : "UUID" ,
     "votedId" : "UUID"
  }
}
```
Para cada `vote` hay un `roomId` del voto que fue hecho, para el cual tiene un
`roundNumber` o número de ronda en el que se hace el voto, tambien un
`voterId` que identifica quien esta haciendo el voto, y un `votedId` para a que
jugador se le hizo el voto
- ## Clase Assignment

```json
{
  "assignment": {
    "roomId": "UUID",
    "playerId": "UUID",
    "role": "Role",
    "word":  "String"
  }
}
```
En cada `assignment` o asignación del jugador para la ronda, hay un
`roomID` para la sala en la que se hacen las asignaciones, un 
`playerId` para el jugador al que se le esta asignando, un
`role` que se le asigna al jugador y un `word` para la palabra
qu el jugador tendra (la palabra secreta para civiles, palabra de pista para
el impostor)