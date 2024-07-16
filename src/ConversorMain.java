import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversorMain {
    private static final String API_KEY = "1edcaea9ef190e56915c90d3";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/%s/latest/";
    private static final List<ConversionRecord> historialConversiones = new ArrayList<>();

    public static void main(String[] args) {
        int opcionSeleccionada = 0;
        Scanner teclado = new Scanner(System.in);

        while (opcionSeleccionada != 8) {
            mostrarMenu();
            System.out.println("Elija una opción válida:");
            opcionSeleccionada = teclado.nextInt();

            if (opcionSeleccionada == 8) {
                System.out.println("Saliendo de la aplicación...");
                break;
            } else if (opcionSeleccionada == 7) {
                mostrarHistorialConversiones();
            } else {
                System.out.println("Ingrese el valor a convertir: ");
                double valorAConvertir = teclado.nextDouble();

                try {
                    realizarConversion(opcionSeleccionada, valorAConvertir);
                } catch (IOException | InterruptedException e) {
                    System.out.println("Error al realizar la conversión: " + e.getMessage());
                }
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
                7) Ver historial de conversiones
                8) Salir
                """;
        System.out.println(menu);
        System.out.println("========================================");
    }

    private static void realizarConversion(int opcionSeleccionada, double valorAConvertir) throws IOException, InterruptedException {
        String monedaOrigen, monedaDestino;
        switch (opcionSeleccionada) {
            case 1:
                monedaOrigen = "USD";
                monedaDestino = "PEN";
                break;
            case 2:
                monedaOrigen = "PEN";
                monedaDestino = "USD";
                break;
            case 3:
                monedaOrigen = "USD";
                monedaDestino = "BOB";
                break;
            case 4:
                monedaOrigen = "BOB";
                monedaDestino = "USD";
                break;
            case 5:
                monedaOrigen = "USD";
                monedaDestino = "CLP";
                break;
            case 6:
                monedaOrigen = "CLP";
                monedaDestino = "USD";
                break;
            default:
                System.out.println("Opción no válida");
                return;
        }

        double tasaConversion = obtenerTasaConversion(monedaOrigen, monedaDestino);
        double resultadoConversion = valorAConvertir * tasaConversion;
        String mensaje = String.format("La cantidad de %f %s equivale a %f %s", valorAConvertir, monedaOrigen, resultadoConversion, monedaDestino);
        System.out.println(mensaje);

        LocalDateTime fechaHora = LocalDateTime.now();
        ConversionRecord registro = new ConversionRecord(valorAConvertir, monedaOrigen, resultadoConversion, monedaDestino, fechaHora);
        historialConversiones.add(registro);
    }

    private static double obtenerTasaConversion(String monedaOrigen, String monedaDestino) throws IOException, InterruptedException {
        String url = String.format(API_URL, API_KEY) + monedaOrigen;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");
        return conversionRates.get(monedaDestino).getAsDouble();
    }

    private static void mostrarHistorialConversiones() {
        System.out.println("========================================");
        System.out.println("Historial de conversiones:");
        for (ConversionRecord registro : historialConversiones) {
            System.out.println(registro);
        }
        System.out.println("========================================");
    }
}

class ConversionRecord {
    private final double valorOriginal;
    private final String monedaOrigen;
    private final double valorConvertido;
    private final String monedaDestino;
    private final LocalDateTime fechaHora;

    public ConversionRecord(double valorOriginal, String monedaOrigen, double valorConvertido, String monedaDestino, LocalDateTime fechaHora) {
        this.valorOriginal = valorOriginal;
        this.monedaOrigen = monedaOrigen;
        this.valorConvertido = valorConvertido;
        this.monedaDestino = monedaDestino;
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("%f %s se convirtieron a %f %s el %s", valorOriginal, monedaOrigen, valorConvertido, monedaDestino, fechaHora.format(formatter));
    }
}