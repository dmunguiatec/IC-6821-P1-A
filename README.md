# IC-6821 Proyecto 1 #

Servidor HTTP.

## ¿Cómo configurar y compilar? ##

Instalar Open JDK 11 (https://sdkman.io/)

```bash
./gradlew clean build
```

## ¿Cómo ejecutar? ##

```bash
./gradlew run --args='{PUERTO} {RUTA A RECURSOS}'
```

por ejemplo

```bash
./gradlew run --args='80 /var/www'
```
