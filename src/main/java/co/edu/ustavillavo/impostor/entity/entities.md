Entidades
---
---
Las entidades son parte importante para una aplicación web ya que
estas son las que ayudan a mantener la persistencia en la base de datos
y la conexión con la misma mediante el ORM de Spring Boot Hibernate.

---
Persistencia
---
La persistencia es un concepto importante para la permanencia en las bases
de datos, es decir, mantener los datos tal cual como se debería y asegurar la 
integridad de la misma permitiendo que los datos sean duraderos.

---
ORM (Object Relational Mapping)
---
es una herramienta en backend que permite a los desarrolladores
trabajar con bases de datos usando objetos del lenguaje de programación
en lugar de escribir consultas SQL directamente. Su importancia radica
en que simplifica la manipulación de datos, hace que el código sea más limpio
y mantenible, y ayuda a evitar errores comunes en las consultas, permitiendo además
cambiar de motor de base de datos con menor esfuerzo. En Java, se utiliza el ORM
Hibernate.

---
Diagrama Entidad Relación (DER)
--


```mermaid
erDiagram

    SALA {
        UUID sala_id PK
        string codigo
        string estado
        UUID jugador_anfitrion_id FK
        string categoria
        int cantidad_impostores
        byte ronda_actual
        string palabra_secreta
        string equipo_ganador
    }

    JUGADOR {
        UUID jugador_id PK
        UUID sala_id FK
        string apodo
        boolean vivo
    }

    ASIGNACION {
        UUID asignacion_id PK
        UUID sala_id FK
        string rol
        string palabra
    }

    VOTO {
        UUID voto_id PK
        UUID sala_id FK
        int numero_ronda
        UUID jugador_votante_id FK
        UUID jugador_votado_id FK
    }

    SALA ||--o{ JUGADOR : contiene
    SALA ||--o{ ASIGNACION : asigna
    SALA ||--o{ VOTO : registra

    SALA ||--|| JUGADOR : anfitrion

    JUGADOR ||--|| VOTO : emite
    JUGADOR ||--|| VOTO : recibe