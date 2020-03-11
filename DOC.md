# Documentación técnica #

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

Para ejecutar el software

```bash
./gradlew run --args='arg1 arg2'
```

## Diseño ##

