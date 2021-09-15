# byohttp #

IC-6821 Diseño de Software  
Proyecto 1  

Build your own http server (byohttp).


## Cómo configurar y construir el software ##

Para construir el software ejecutar

```bash
./gradlew build
```

Para ejecutar las pruebas automatizadas

```bash
./gradlew test
```

El reporte de pruebas se encuentra en `build/reports/tests/test/index.html`, para ver las pruebas

```bash
xdg-open build/reports/tests/test/index.html
```

## Cómo ejecutar el software ##

Para ejecutar el software, primero es necesario construir el binario

```bash
 ./gradlew installDist
 ```

El binario estará disponible en la ruta `build/install/byohttp/bin`

```bash
cd build/install/byohttp/bin
```

Para ejecutar el binario

```bash
./byohttp {puerto} {ruta a recursos} {ruta a mapeo de tipos mime}
```

## Diseño ##

