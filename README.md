# 📋 Tarea Semanal: Proyecto E2E - Entrega 1

## Descripción 💡

Esta tarea implica la implementación inicial del proyecto E2E. Deben desarrollar las **entidades** correspondientes a la
base de datos, la capa de **lógica de negocios** a través de los **servicios**, y la capa de **controladores** para
manejar las peticiones HTTP y sus respuestas.

> 📄 El modelamiento entidad-relación del proyecto se encuentra en el siguiente enlace:
> [Modelo E-R del Proyecto E2E](https://docs.google.com/document/d/1E9Go41IqIFuJCAHrUg3JQfKZiDV5f97oWwVkbD-o43Y/edit?usp=sharing)

## Indicaciones

**IMPORTANTE** 🚨

- No se debe mover ni cambiar el nombre ni la ubicación de ningún archivo, ya que esto podría ocasionar fallas en la
  calificación automática y resultar en una puntuación de 0.
- Se especificó el uso de `enums` para ciertos atributos en algunas clases. A continuación se detallan estos `enums`,
  cuya correcta implementación es crucial para evitar posibles fallos en las pruebas automatizadas.

| Nombre del `enum` | Es atributo de... | Valores                                               |
|-------------------|-------------------|-------------------------------------------------------|
| Category          | `Driver`          | X, XL, BLACK                                          |
| Status            | `Ride`            | REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELED |
| Role              | `User`            | ADMIN, PASSENGER, DRIVER                              |

- La tabla intermedia `UserLocation`, que relaciona `Passenger` y `Coordinate`, utiliza una clave primaria compuesta, lo
  que significa que tiene dos claves primarias. JPA no puede implementar esto mediante anotaciones directas, por lo que
  se debe seguir el enfoque detallado en
  este [enlace](https://www.codejava.net/frameworks/spring/spring-data-jpa-composite-primary-key-examples). Esta
  peculiaridad técnica permite manejar tablas intermedias con atributos adicionales, mientras se aprende a sortear esta
  limitación de JPA.
- Aunque el uso de tablas intermedias con atributos adicionales no es inusual ni problemático a nivel de base de datos,
  puede causar un problema específico al serializar a JSON debido a la generación de referencias circulares. Para evitar
  este problema, utilicen la siguiente anotación en las clases de entidades mencionadas anteriormente:
  `@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Foo.class)` (en
  este caso funcionaría para una clase llamada `Foo`, cambiar esta parte según sea necesario).

## 💡 Inyección de dependencias

Se recomienda utilizar **inyección por constructor** en lugar de inyección por campos.
Esto permite una mayor facilidad
de pruebas unitarias y evita problemas de acoplamiento oculto.

### ✅ Inyección por constructor con `@RequiredArgsConstructor`

Usa la anotación `@RequiredArgsConstructor` de Lombok para generar un constructor con los atributos `final`. Ejemplo:

```java

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
}
```

### 🚫 Inyección por campos (no recomendada)

```java

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
}
```

## 📚 Anotaciones importantes

Aquí una descripción rápida de algunas anotaciones que verás frecuentemente en las entidades:

### `@MapsId`

Se utiliza en relaciones donde una entidad usa la clave primaria de otra entidad (útil en claves compuestas). Más info:
[Documentación `@MapsId`](https://www.baeldung.com/jpa-composite-primary-keys#using-mapid)

### `@Column`

Define cómo se mapea un atributo de la entidad a una columna de la base de datos.  
[Documentación `@Column`](https://www.geeksforgeeks.org/spring-data-jpa-column-annotation/)

### `@JoinColumn`

Se usa para indicar la columna que se utilizará como clave foránea en relaciones entre entidades.  
[Documentación `@JoinColumn`](https://www.baeldung.com/jpa-join-column)

### Entidades ⚙️

Las entidades requeridas fueron presentadas al final de la primera semana. Las entidades adicionales agregadas por los
alumnos no serán consideradas en la calificación automática, pero se permite su inclusión si es necesario, siempre y
cuando se discuta con los profesores.

### Endpoints 🌐

A continuación se detallan los endpoints que deben implementar por entidad y sus descripciones:

> Los `Query Parameters` deben de tener el mismo nombre que se indica en estas tablas.

#### Recurso `Driver`

| Método HTTP | URI                   | Path Parameter | Query Parameter     | Request Body | Http Status      | Response Body |
|-------------|-----------------------|----------------|---------------------|--------------|------------------|---------------|
| GET         | /driver/{id}          | id             | -                   |              | 200 (OK)         | `Driver`      |
| POST        | /driver               |                | -                   | `Driver`     | 201 (Created)    | `Driver`      |
| DELETE      | /driver/{id}          | id             | -                   |              | 204 (No Content) |               |
| PUT         | /driver/{id}          | id             | -                   | `Driver`     | 200 (OK)         | `Driver`      |
| PATCH       | /driver/{id}/location | id             | latitude, longitude |              | 200 (OK)         | `Driver`      |
| PATCH       | /driver/{id}/car      | id             | -                   | `Vehicle`    | 200 (OK)         | `Driver`      |

#### Recurso `Passenger`

| Método HTTP | URI                                   | Path Parameter   | Query Parameter | Request Body | Http Status      | Response Body        |
|-------------|---------------------------------------|------------------|-----------------|--------------|------------------|----------------------|
| GET         | /passenger/{id}                       | id               | -               |              | 200 (OK)         | `Passenger`          |
| DELETE      | /passenger/{id}                       | id               | -               |              | 204 (No Content) |                      |
| PATCH       | /passenger/{id}                       | id               | -               | `Coordinate` | 200 (OK)         | `Passenger`          |
| GET         | /passenger/{id}/places                | id               | -               |              | 200 (OK)         | `List\<Coordinate\>` |
| DELETE      | /passenger/{id}/places/{coordinateId} | id, coordinateId | -               |              | 204 (No Content) |                      |

#### Recurso `Review`

| Método HTTP | URI          | Path Parameter | Query Parameter | Request Body | Http Status      | Response Body |
|-------------|--------------|----------------|-----------------|--------------|------------------|---------------|
| POST        | /review      | -              | -               | `Review`     | 201 (CREATED)    | `Review`      |
| DELETE      | /review/{id} | id             | -               | -            | 204 (NO CONTENT) |

#### Recurso `Ride`

| Método HTTP | URI                              | Path Parameter   | Query Parameter | Request Body | Http Status      | Response Body  |
|-------------|----------------------------------|------------------|-----------------|--------------|------------------|----------------|
| POST        | /ride                            | -                | -               | `Ride`       | 201 (CREATED)    | `Ride`         |
| PATCH       | /ride/{rideId}/assign/{driverId} | rideId, driverId | -               |              | 200 (OK)         | `Ride`         |
| DELETE      | /ride/{id}                       | id               | -               | -            | 204 (No Content) |                |
| GET         | /ride/{passengerId}              | passengerId      | page, size      | -            | 200 (OK)         | `Page\<Ride\>` |
| PATCH       | /ride/{id}                       | id               | -               |              | 200 (OK)         | `Ride`         |

## 🛠️ Cómo ejecutar las pruebas localmente 🛠️

Si deseas ejecutar localmente los test para conocer tu avance usa el script ``runtest`` ubicado en la raiz del proyecto.
Primero asegúrate que la variable de entorno `JAVA_HOME` está correctamente definida en tu sistema

```bash
./runtest [NombreDelTest]
```

Puedes encontrar los test en las subcarpetas de la carpeta `src/test/java/`

### ⚠️ Al ejecutar los test tener en cuenta:

- Debes tener el archivo `mvnw` que autogenera Spring Initializr en la raíz del proyecto.
- Los test que contienen la palabra **Integration** testean la aplicación completa, se recomienda ejecutarlos al final.
- Los test que contienen la palabra  **Repository** testean la capa de persistencia junto con las entidades y usan
  Docker, asegúrate que se esté ejecutando.
- Los test que tiene un nombre como  **EntidadTest** testean la entidad correspondiente y su relación con otras
  entidades.

¡Buena suerte! 🚀
