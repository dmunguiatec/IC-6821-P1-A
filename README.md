IC-6821 Diseño de Software  
Prof. Diego Munguia Molina  
IC-AL
---
# Proyecto 2 - Build your own HTTP Server

## Objetivos de aprendizaje

* Modelar componentes de software tomando en cuenta los principios de diseño SOLID (SRP, OCP, LSP, ISP, DIP). (III, IV)

## Especificación

El proyecto consiste en diseñar y desarrollar un servidor web capaz de comunicarse con navegadores a través del protocolo HTTP.

### Épica 

Desarrollar un servidor web que soporte los métodos GET y HEAD para responder a solicitudes por recursos en el sistema de archivos.

### Historias de usuario

1. Como el usuario agente (*navegador*), quiero enviar al servidor una solicitud (*request*) con el método GET, para obtener como respuesta (*response*) un archivo almacenado en el sistema de archivos del servidor.

Solicitud a `GET http://localhost:8008/helloworld.html`

```text
GET /helloworld.html HTTP/1.1
User-Agent: PostmanRuntime/7.22.0
Accept: */*
Cache-Control: no-cache
Host: localhost
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```
Respuesta

```html
HTTP/1.1 200 OK
Server: byohttp/0.0.1
Date: Tue, 10 Mar 2020 01:57:21 GMT
Content-Type: text/html
Content-Length: 95
Last-Modified: Tue, 10 Mar 2020 01:57:00 GMT
Connection: keep-alive
Accept-Ranges: bytes

<html>
<head>
<title>Hello world!</title>
</head>
<body>
<h1>Hello world!</h1>
</body>
</html>
```

2. Como el usuario agente, quiero enviar al servidor una solicitud (*request*) con el método `HEAD`, para obtener como respuesta (*response*) un archivo almacenado en el sistema de archivos del servidor.

Solicitud a `HEAD http://localhost:8008/helloworld.html`

```text
HEAD /helloworld.html HTTP/1.1
User-Agent: PostmanRuntime/7.22.0
Accept: */*
Cache-Control: no-cache
Host: localhost
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```

Respuesta

```text
HTTP/1.1 200 OK
Server: byohttp/0.0.1
Date: Tue, 10 Mar 2020 01:57:21 GMT
Content-Type: text/html
Content-Length: 95
Last-Modified: Tue, 10 Mar 2020 01:57:00 GMT
Connection: keep-alive
Accept-Ranges: bytes
```

3. Como el usuario agente, quiero enviar al servidor una solicitud (*request*) con cualquier método excepto `GET` o `HEAD`, para obtener como respuesta (*response*) un código de error `501 Not Implemented`.

Solicitud a `PUT http://localhost:8008/helloworld.html`

```text
PUT /helloworld.html HTTP/1.1
User-Agent: PostmanRuntime/7.22.0
Accept: */*
Cache-Control: no-cache
Host: localhost
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```

Respuesta

```html
HTTP/1.1 501 Not Implemented
Server: byohttp/0.0.1
Date: Tue, 10 Mar 2020 16:31:16 GMT
Content-Type: text/html
Content-Length: 157
Connection: close

<html>
<head><title>501 Not Implemented</title></head>
<body>
<center><h1>501 Not Implemented</h1></center>
<hr><center>byohttp/0.0.1</center>
</body>
</html>
```

4. Como el usuario agente, quiero enviar al servidor una solicitud (*request*) con el método `GET` o `HEAD` de un recurso que no existe en el servidor, para obtener como respuesta (*response*) un código de error `404 Not Found`.

```text
GET /helloworld.html HTTP/1.1
User-Agent: PostmanRuntime/7.22.0
Accept: */*
Cache-Control: no-cache
Host: localhost
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```

Respuesta

```html
HTTP/1.1 404 Not Found
Server: byohttp/0.0.1
Date: Tue, 10 Mar 2020 16:35:02 GMT
Content-Type: text/html
Content-Length: 153
Connection: keep-alive

<html>
<head><title>404 Not Found</title></head>
<body>
<center><h1>404 Not Found</h1></center>
<hr><center>byohttp/0.0.1</center>
</body>
</html>
```

### Arquitectura

![Diagrama de arquitectura](./Arquitectura.png)

* El servidor se comunicará con los usuarios agentes a través de un socket escuchando en un puerto TCP (ej. `8008`).
* La comunicación con el servidor debe ser asincrónica, es decir cada solicitud debe ser procesada por su propio hilo.
* El servidor tendrá acceso a un directorio específico donde se encontrarán los recursos que servirá.
* Los siguientes datos deben ser parámetros configurables del sistema:
  - El puerto en el que escucha el socket.
  - La ruta en el sistema de archivos donde se encuentran los recursos servidos.
  - La asociación entre extensiones de archivo y tipos `MIME`.
* El servidor debe mantener una bitácora (*log*) donde se registren todas las solicitudes recibidas y respuestas generadas, así como cualquier excepción inesperada y un mensaje de inicialización. En primera instancia esta bitácora se enviará a la salida estándar (`stdout`). En el futuro se podrá configurar la salida a la que se enviará la bitácora.
  - Ejemplo de mensaje de inicialización
```
[INFO] byohttp server startup successful, listening on port 8000
Mime type mapping file: ./mappings/mime.csv
Resource directory: ./www
```

  - Ejemplo de mensaje de bitácora para solicitud
```
[REQUEST] 2024-09-26T14:30:00-06:00
GET /helloworld.html HTTP/1.1
User-Agent: PostmanRuntime/7.22.0
Accept: */*
Cache-Control: no-cache
Host: localhost
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
```
  - Ejemplo de mensaje de bitácora para respuesta
```
[RESPONSE] 2024-09-26T14:30:15-06:00
HTTP/1.1 200 OK
Server: byohttp/0.0.1
Date: Tue, 10 Mar 2020 01:57:21 GMT
Content-Type: text/html
Content-Length: 95
Last-Modified: Tue, 10 Mar 2020 01:57:00 GMT
Connection: keep-alive
Accept-Ranges: bytes
```
  - Ejemplo de mensaje de bitácora para excepción inesperada
```
[ERROR] 2024-09-26T14:30:15-06:00 java.lang.NullPointerException: Cannot invoke "Object.toString()" because "n" is null
	at edu.byohttp.SocketMessageRunnable.run(SocketMessageRunnable.java:35)
	at java.base/java.lang.Thread.run(Thread.java:1570)
```

* Es necesario considerar que es posible que el cliente envíe una solicitud vacía o malformada (es decir, que no cumpla con la sintaxis de un *request*), en cuyo caso el servidor debe responder con `400 Bad Request`.
* Cualquier error no esperado durante la resolución de una solicitud deberá manejarse como una respuesta `500 Internal Server Error`.
* El stream de entrada del request nunca envía `EOF`, la lectura del request se debe detener al encontrar la primera línea en blanco.
* En el futuro el servidor deberá soportar todos los métodos HTTP.
* En el futuro el servidor no sólo deberá servir recursos almacenados en el sistema de archivos, sino también recursos generados dinámicamente a través de la invocación de funciones siguiendo el protocolo `REST`.

### Responsabilidades funcionales

1. Leer el mensaje que ingresa a través del socket y cargarlo en memoria

2. Parsear este mensaje para separarlo en sus elementos estructurales (método, recurso, versión de protocolo, encabezados).

    a. Construir una solicitud (*request*) con base en estos datos

3. Para procesar la solicitud tenemos tres posibles casos:

   a. Caso Error: Si el método no es `GET` ni `HEAD`

   1. Construir una respuesta `501`

   2. Escribir la respuesta en la salida del socket (fin)

   b. Caso Get: Si el método es `GET`

   1. Buscar el recurso en el directorio de recursos

   2. Si no encuentra el recurso

      * Construir una respuesta `404`

      * Escribirla en la salida del socket (fin)

   3. Identificar su tipo `MIME` con base en la extensión en el nombre del recurso

   4. Determinar el tamaño del recurso en bytes

   5. Construir una respuesta `200` con base en estos metadatos

   6. Escribir la respuesta en la salida del socket

   7. Escribir el contenido del archivo en la salida del socket (fin)

   c. Caso Head: Si el método es `HEAD`

   1. Buscar el recurso en el directorio de recursos

   2. Si no encuentra el recurso

      * Construir una respuesta `404`

      * Escribirla en la salida del socket (fin)

   3. Identificar su tipo `MIME` con base en la extensión en el nombre del recurso

   4. Determinar el tamaño del recurso en bytes

   5. Construir una respuesta `200` con base en estos metadatos

   6. Escribir la respuesta en la salida del socket (fin)

### Recursos externos para referencia

* [Formato de mensajes HTTP](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol#Message_format)
* [Sockets](https://www.baeldung.com/a-guide-to-java-sockets)
* [Referencia oficial HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP)
* [Tipos MIME](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types)

### Metodologia

* El proyecto se desarrollará en grupos de 5 personas durante 3 semanas.
* Durante el período del proyecto no trabajaremos en otras asignaciones ni en nuevos contenidos. El tiempo del curso será totalmente dedicado al proyecto.
* Semanalmente cada equipo tendrá una sesión de revision con la persona docente para discutir el diseño, identificar mejoras y chequear el nivel de avance.
* Cada equipo designará una persona con el rol de líder, este rol tiene tres responsabilidades:
  - Coordinar reuniones de equipo
  - Coordinar asignación de tareas
  - Resolución de conflictos en la toma de decisiones
* Cada equipo designará dos personas con el rol de relatoras, estas personas tendrán la responsabilidad de tomar notas durante las reuniones de revisión que serán utilizadas posteriormente para hacer mejoras al código.
* El proyecto se desarrollará en Java siguiendo el paradigma de programación orientada a objetos.
* Se recomienda iniciar la implementación programando y subiendo al repositorio las interfaces. De esta forma todo el equipo tendrá disponible una base de código compilable para implementar cada uno de los módulos.
* Se recomienda iniciar la implementación de abajo hacia arriba. Iniciando con las clases que tengan menos dependencias y avanzando progresivamente hacia las clases que tengan más dependencias.
* Cada equipo entregará en el repositorio git el código que cumpla con los requerimientos funcionales establecidos.

### Rúbricas de evaluación

Disponibles en el siguiente enlace:

https://docs.google.com/spreadsheets/d/157_JSr9W_S5ucpk_DU5fr-R_7m114QeJPWeymJjw6Zw
