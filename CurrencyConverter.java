import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class JsonParser {
    public static double getDouble(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx == -1) return -1;
        int start = idx + search.length();
        int end = start;
        while (end < json.length() &&
               (Character.isDigit(json.charAt(end)) ||
                json.charAt(end) == '.' || json.charAt(end) == '-')) end++;
        try { return Double.parseDouble(json.substring(start, end)); }
        catch (NumberFormatException e) { return -1; }
    }

    public static String getString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;
        int start = idx + search.length();
        int end = json.indexOf("\"", start);
        return (end == -1) ? null : json.substring(start, end);
    }
}

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    private static final Map<String, String> CURRENCIES = new LinkedHashMap<>();
    static {
        CURRENCIES.put("USD", "US Dollar");
        CURRENCIES.put("EUR", "Euro");
        CURRENCIES.put("GBP", "British Pound");
        CURRENCIES.put("INR", "Indian Rupee");
        CURRENCIES.put("JPY", "Japanese Yen");
        CURRENCIES.put("AUD", "Australian Dollar");
        CURRENCIES.put("CAD", "Canadian Dollar");
        CURRENCIES.put("CHF", "Swiss Franc");
        CURRENCIES.put("CNY", "Chinese Yuan");
        CURRENCIES.put("SGD", "Singapore Dollar");
        CURRENCIES.put("AED", "UAE Dirham");
        CURRENCIES.put("SAR", "Saudi Riyal");
    }

    private static final Map<String, String> SYMBOLS = new HashMap<>();
    static {
        SYMBOLS.put("USD", "$");
        SYMBOLS.put("EUR", "EUR");
        SYMBOLS.put("GBP", "GBP");
        SYMBOLS.put("INR", "Rs.");
        SYMBOLS.put("JPY", "JPY");
        SYMBOLS.put("AUD", "AUD");
        SYMBOLS.put("CAD", "CAD");
        SYMBOLS.put("CHF", "CHF");
        SYMBOLS.put("CNY", "CNY");
        SYMBOLS.put("SGD", "SGD");
        SYMBOLS.put("AED", "AED");
        SYMBOLS.put("SAR", "SAR");
    }

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        banner();
        boolean again = true;
        while (again) {
            String base   = selectCurrency("BASE CURRENCY (FROM)");
            String target = selectCurrency("TARGET CURRENCY (TO)");
            double amount = inputAmount(base);
            System.out.println();
            System.out.println("  Fetching live exchange rate...");
            System.out.println();
            convert(base, target, amount);
            System.out.print("  Convert another? (y/n): ");
            again = scanner.nextLine().trim().equalsIgnoreCase("y");
            System.out.println();
        }
        sep();
        System.out.println("  Thank you for using Currency Converter!");
        sep();
    }

    private void banner() {
        sep();
        System.out.println("        LIVE CURRENCY CONVERTER");
        System.out.println("      Real-time Exchange Rates");
        sep();
        System.out.println();
    }

    private String selectCurrency(String label) {
        List<String> codes = new ArrayList<>(CURRENCIES.keySet());
        while (true) {
            sep();
            System.out.println("  SELECT " + label);
            System.out.println("------------------------------------------");
            for (int i = 0; i < codes.size(); i++) {
                String c = codes.get(i);
                System.out.printf("  %2d.  %-4s  -  %s%n", i + 1, c, CURRENCIES.get(c));
            }
            sep();
            System.out.print("  Choose (1-" + codes.size() + "): ");
            int choice = readInt();
            if (choice >= 1 && choice <= codes.size()) {
                String sel = codes.get(choice - 1);
                System.out.println("  >> " + sel + " - " + CURRENCIES.get(sel) + " selected.");
                System.out.println();
                return sel;
            }
            System.out.println("  ERROR: Enter a number between 1 and " + codes.size());
            System.out.println();
        }
    }

    private double inputAmount(String base) {
        while (true) {
            System.out.printf("  Enter amount in %s (%s): ", CURRENCIES.get(base), base);
            double amt = readDouble();
            if (amt > 0) return amt;
            System.out.println("  ERROR: Amount must be positive.");
            System.out.println();
        }
    }

    private void convert(String base, String target, double amount) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + base))
                    .GET()
                    .build();
            HttpResponse<String> resp =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() != 200) {
                System.out.println("  ERROR: API returned status " + resp.statusCode());
                return;
            }

            String json   = resp.body();
            double rate   = JsonParser.getDouble(json, target);

            if (rate <= 0) {
                System.out.println("  ERROR: Rate not found for " + target);
                return;
            }

            double result = amount * rate;
            String date   = JsonParser.getString(json, "date");
            String bSym   = SYMBOLS.getOrDefault(base,   base);
            String tSym   = SYMBOLS.getOrDefault(target, target);

            sep();
            System.out.println("          CONVERSION RESULT");
            System.out.println("------------------------------------------");
            System.out.printf("  From   : %-4s  (%s)%n", base, CURRENCIES.get(base));
            System.out.printf("  To     : %-4s  (%s)%n", target, CURRENCIES.get(target));
            System.out.println("------------------------------------------");
            System.out.printf("  Amount         : %s %.2f%n", bSym, amount);
            System.out.printf("  Exchange Rate  : 1 %s = %s %.6f%n", base, tSym, rate);
            System.out.println("------------------------------------------");
            System.out.printf("  CONVERTED AMT  : %s %.2f%n", tSym, result);
            System.out.printf("  Rate Date      : %s%n", date != null ? date : "live");
            sep();
            System.out.println();

        } catch (java.net.ConnectException e) {
            System.out.println("  ERROR: No internet connection. Check your network.");
            System.out.println();
        } catch (Exception e) {
            System.out.println("  ERROR: " + e.getMessage());
            System.out.println();
        }
    }

    private int readInt() {
        try   { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private double readDouble() {
        try   { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static void sep() {
        System.out.println("==========================================");
    }

    public static void main(String[] args) {
        new CurrencyConverter().run();
    }
}