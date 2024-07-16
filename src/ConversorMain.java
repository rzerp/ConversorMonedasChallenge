import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorMain {
    private static final String API_KEY = "1edcaea9ef190e56915c90d3";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/%s/latest/";

    public static void main(String[] args) {
        int opcionSeleccionada = 0;
        Scanner teclado = new Scanner(System.in);

        while (opcionSeleccionada != 7) {
            mostrarMenu();
            System.out.println("Elija una opción válida:");
            opcionSeleccionada = teclado.nextInt();

            if (opcionSeleccionada == 7) {
                System.out.println("Saliendo de la aplicación...");
                break;
            }

            System.out.println("Ingrese el valor a convertir: ");
            double valorAConvertir = teclado.nextDouble();

            try {
                realizarConversion(opcionSeleccionada, valorAConvertir);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error al realizar la conversión: " + e.getMessage());
            }
        }

        teclado.close();
    }

    private static void mostrarMenu() {
        System.out.println("========================================");
        String menu = """
                1) Dólar --> Sol Peruano
                2) Sol Peruano --> Dólar
                3) Dólar --> Boliviano
                4) Boliviano --> Dólar
                5) Dólar --> Peso Chileno
                6) Peso Chileno --> Dólar
                7) Salir
                """;
        System.out.println(menu);
        System.out.println("========================================");
    }

    private static void realizarConversion(int opcionSeleccionada, double valorAConvertir) throws IOException, InterruptedException {
        switch (opcionSeleccionada) {
            case 1:
                convertirMoneda("USD", "PEN", valorAConvertir);
                break;
            case 2:
                convertirMoneda("PEN", "USD", valorAConvertir);
                break;
            case 3:
                convertirMoneda("USD", "BOB", valorAConvertir);
                break;
            case 4:
                convertirMoneda("BOB", "USD", valorAConvertir);
                break;
            case 5:
                convertirMoneda("USD", "CLP", valorAConvertir);
                break;
            case 6:
                convertirMoneda("CLP", "USD", valorAConvertir);
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private static void convertirMoneda(String from, String to, double valor) throws IOException, InterruptedException {
        String url = String.format(API_URL, API_KEY) + from;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        double tasaConversion = obtenerTasaConversion(response.body(), to);
        double resultadoConversion = valor * tasaConversion;
        System.out.println("La cantidad de " + valor + " (" + from + ") equivale a " + resultadoConversion + " (" + to + ")");
    }

    private static double obtenerTasaConversion(String jsonResponse, String monedaDestino) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");
        return conversionRates.get(monedaDestino).getAsDouble();
    }
}