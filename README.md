# Conversor de Divisas o Monedas

Este programa desarrollado en Java permite al usuario intercambiar monedas utilizando la API proporcionada por ExchangeRate-API.

## Características

### Inicio del Programa

Al correr la aplicación, se presenta un menú con diversas opciones para convertir entre monedas.

## Menú de Opciones

El menú despliega las siguientes alternativas:

1) Dólar --> Sol Peruano
2) Sol Peruano --> Dólar
3) Dólar --> Boliviano
4) Boliviano --> Dólar
5) Dólar --> Peso Chileno
6) Peso Chileno --> Dólar
7) Salir

## Ejemplos

### Convertir de Dólar a Sol Peruano
![Dolar a Sol](img/dolar_a_sol.png)

### Convertir de Sol Peruano a Dólar
![Sol_a_Dolar](img/sol_a_dolar.png)

## Proceso de Conversión

Luego de seleccionar una opción válida, se solicita al usuario ingresar el monto a convertir. Posteriormente, se efectúa la conversión utilizando la API y se muestra el resultado.

## Finalización del Programa

La opción 7) Salir permite cerrar la aplicación de manera controlada.

## Tecnologías Empleadas

- Java 17
- API de ExchangeRate-API
- Librería HttpClient para realizar peticiones HTTP
- Librería Gson para analizar respuestas JSON

## Requisitos

- JDK 17 o versión superior
- Conexión a Internet para acceder a la API de ExchangeRate-API

## Configuración de API

Antes de ejecutar el programa, asegúrese de configurar la variable API_KEY en ConversorMain.java con su propia clave de API de ExchangeRate-API.