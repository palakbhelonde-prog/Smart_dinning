import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** SmartDine local web server and REST API. No third-party libraries required. */
public class Server {
    private static final int PORT = 8080;
    private static final String MENU_JSON = "[{\"id\":1,\"name\":\"Truffle Alfredo Pasta\",\"category\":\"italian\",\"price\":329,\"rating\":4.9,\"origin\":\"Italian\",\"tag\":\"Chef's Favourite\",\"description\":\"Silky parmesan cream, delicate truffle notes and freshly cracked pepper.\",\"image\":\"https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=900&q=85\"},{\"id\":2,\"name\":\"Margherita Pizza\",\"category\":\"italian\",\"price\":249,\"rating\":4.8,\"origin\":\"Italian\",\"tag\":\"Classic\",\"description\":\"Tomato, creamy mozzarella, fragrant basil and a beautifully crisp crust.\",\"image\":\"https://images.unsplash.com/photo-1579751626657-72bc17010498?auto=format&fit=crop&w=900&q=85\"},{\"id\":3,\"name\":\"Wild Mushroom Risotto\",\"category\":\"italian\",\"price\":349,\"rating\":4.8,\"origin\":\"Italian\",\"tag\":\"Signature\",\"description\":\"Slow-stirred arborio rice, roasted mushrooms, herbs and parmesan.\",\"image\":\"https://images.unsplash.com/photo-1476124369491-e7addf5db371?auto=format&fit=crop&w=900&q=85\"},{\"id\":4,\"name\":\"Classic Lasagna\",\"category\":\"italian\",\"price\":329,\"rating\":4.7,\"origin\":\"Italian\",\"tag\":\"Comfort Food\",\"description\":\"Layers of pasta, rich tomato sauce, béchamel and bubbling cheese.\",\"image\":\"https://images.unsplash.com/photo-1574894709920-11b28e7367e3?auto=format&fit=crop&w=900&q=85\"},{\"id\":5,\"name\":\"Pesto Penne\",\"category\":\"italian\",\"price\":279,\"rating\":4.7,\"origin\":\"Italian\",\"tag\":\"Fresh & Green\",\"description\":\"Penne tossed with basil pesto, parmesan, cherry tomatoes and olive oil.\",\"image\":\"https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=900&q=85\"},{\"id\":6,\"name\":\"Bruschetta\",\"category\":\"italian\",\"price\":189,\"rating\":4.6,\"origin\":\"Italian\",\"tag\":\"Small Plates\",\"description\":\"Toasted rustic bread topped with tomatoes, basil and extra virgin olive oil.\",\"image\":\"https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?auto=format&fit=crop&w=900&q=85\"},{\"id\":7,\"name\":\"French Onion Soup\",\"category\":\"french\",\"price\":199,\"rating\":4.8,\"origin\":\"French\",\"tag\":\"Traditional\",\"description\":\"Sweet caramelised onions in a rich broth with golden toasted bread.\",\"image\":\"https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=85\"},{\"id\":8,\"name\":\"Ratatouille\",\"category\":\"french\",\"price\":279,\"rating\":4.7,\"origin\":\"French\",\"tag\":\"Garden Fresh\",\"description\":\"A Provençal medley of tender vegetables, tomato and fragrant herbs.\",\"image\":\"https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=900&q=85\"},{\"id\":9,\"name\":\"Mushroom Vol-au-Vent\",\"category\":\"french\",\"price\":299,\"rating\":4.8,\"origin\":\"French\",\"tag\":\"Elegant\",\"description\":\"Flaky golden pastry filled with creamy mushrooms and fresh thyme.\",\"image\":\"https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=85\"},{\"id\":10,\"name\":\"Croque Monsieur\",\"category\":\"french\",\"price\":259,\"rating\":4.7,\"origin\":\"French\",\"tag\":\"Bistro Classic\",\"description\":\"A golden grilled sandwich with béchamel, melted cheese and herbs.\",\"image\":\"https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=900&q=85\"},{\"id\":11,\"name\":\"Pad Thai Noodles\",\"category\":\"thai\",\"price\":279,\"rating\":4.9,\"origin\":\"Thai\",\"tag\":\"Bestseller\",\"description\":\"Wok-tossed rice noodles, crunchy peanuts, lime and aromatic Thai herbs.\",\"image\":\"https://images.unsplash.com/photo-1559314809-0d155014e29e?auto=format&fit=crop&w=900&q=85\"},{\"id\":12,\"name\":\"Thai Green Curry\",\"category\":\"thai\",\"price\":299,\"rating\":4.8,\"origin\":\"Thai\",\"tag\":\"Spice & Soul\",\"description\":\"Fragrant green curry with coconut milk, vegetables and Thai basil.\",\"image\":\"https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?auto=format&fit=crop&w=900&q=85\"},{\"id\":13,\"name\":\"Tom Yum Soup\",\"category\":\"thai\",\"price\":219,\"rating\":4.8,\"origin\":\"Thai\",\"tag\":\"Hot & Sour\",\"description\":\"Aromatic lemongrass, lime, mushrooms and a lively chilli finish.\",\"image\":\"https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=85\"},{\"id\":14,\"name\":\"Thai Basil Fried Rice\",\"category\":\"thai\",\"price\":249,\"rating\":4.7,\"origin\":\"Thai\",\"tag\":\"Wok Favourite\",\"description\":\"Fragrant jasmine rice stir-fried with vegetables, garlic and basil.\",\"image\":\"https://images.unsplash.com/photo-1512058564366-18510be2db19?auto=format&fit=crop&w=900&q=85\"},{\"id\":15,\"name\":\"Fresh Thai Spring Rolls\",\"category\":\"thai\",\"price\":179,\"rating\":4.6,\"origin\":\"Thai\",\"tag\":\"Light Bites\",\"description\":\"Crisp vegetable rolls with a sweet chilli dipping sauce.\",\"image\":\"https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=900&q=85\"},{\"id\":16,\"name\":\"Tiramisu\",\"category\":\"dessert\",\"price\":189,\"rating\":4.9,\"origin\":\"Italian Dessert\",\"tag\":\"Guest Favourite\",\"description\":\"Espresso-soaked layers, delicate mascarpone cream and cocoa.\",\"image\":\"https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?auto=format&fit=crop&w=900&q=85\"},{\"id\":17,\"name\":\"Crème Brûlée\",\"category\":\"dessert\",\"price\":199,\"rating\":4.9,\"origin\":\"French Dessert\",\"tag\":\"Signature\",\"description\":\"Silky vanilla custard beneath a delicate caramelised sugar crust.\",\"image\":\"https://images.unsplash.com/photo-1470324161839-ce2bb6fa6bc3?auto=format&fit=crop&w=900&q=85\"},{\"id\":18,\"name\":\"Chocolate Fondant\",\"category\":\"dessert\",\"price\":219,\"rating\":4.8,\"origin\":\"French Dessert\",\"tag\":\"Chocolate Love\",\"description\":\"Warm chocolate cake with a soft, molten centre.\",\"image\":\"https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=900&q=85\"},{\"id\":19,\"name\":\"Mango Sticky Rice\",\"category\":\"dessert\",\"price\":179,\"rating\":4.8,\"origin\":\"Thai Dessert\",\"tag\":\"Tropical\",\"description\":\"Sweet mango, coconut-infused sticky rice and a gentle sesame finish.\",\"image\":\"https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=900&q=85\"},{\"id\":20,\"name\":\"Vanilla Panna Cotta\",\"category\":\"dessert\",\"price\":169,\"rating\":4.7,\"origin\":\"Italian Dessert\",\"tag\":\"Delicate\",\"description\":\"A silky vanilla cream dessert finished with a berry coulis.\",\"image\":\"https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=900&q=85\"},{\"id\":21,\"name\":\"Italian Lemon Soda\",\"category\":\"drink\",\"price\":119,\"rating\":4.7,\"origin\":\"Italian\",\"tag\":\"Refreshing\",\"description\":\"Bright lemon, sparkling water and a refreshing citrus finish.\",\"image\":\"https://images.unsplash.com/photo-1513558161293-cdaf765edfd4?auto=format&fit=crop&w=900&q=85\"},{\"id\":22,\"name\":\"Iced Matcha Latte\",\"category\":\"drink\",\"price\":179,\"rating\":4.8,\"origin\":\"Japanese Inspired\",\"tag\":\"Modern Classic\",\"description\":\"Earthy green tea blended with chilled milk over ice.\",\"image\":\"https://images.unsplash.com/photo-1515823064-d6e0c04616a7?auto=format&fit=crop&w=900&q=85\"},{\"id\":23,\"name\":\"Thai Iced Tea\",\"category\":\"drink\",\"price\":149,\"rating\":4.8,\"origin\":\"Thai\",\"tag\":\"Sweet & Creamy\",\"description\":\"A fragrant tea with creamy milk and a beautiful amber colour.\",\"image\":\"https://images.unsplash.com/photo-1556679343-c7306c1976bc?auto=format&fit=crop&w=900&q=85\"},{\"id\":24,\"name\":\"Berry Sparkler\",\"category\":\"drink\",\"price\":159,\"rating\":4.7,\"origin\":\"House Special\",\"tag\":\"Fruit Forward\",\"description\":\"A sparkling berry cooler with fresh citrus and mint.\",\"image\":\"https://images.unsplash.com/photo-1513558161293-cdaf765edfd4?auto=format&fit=crop&w=900&q=85\"}]";
    private static final List<String> ORDERS = new ArrayList<>();
    private static int nextOrderNumber = 1001;
    private static Path webRoot;

    public static void main(String[] args) throws Exception {
        webRoot = locateWebRoot();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", PORT), 0);
        server.createContext("/api/menu", Server::menu);
        server.createContext("/api/orders", Server::orders);
        server.createContext("/api/test", Server::test);
        server.createContext("/", Server::staticFiles);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        System.out.println("====================================================");
        System.out.println("       SMARTDINE RESTAURANT MANAGEMENT SERVER");
        System.out.println("====================================================");
        System.out.println("Web app : http://localhost:" + PORT);
        System.out.println("Menu API: http://localhost:" + PORT + "/api/menu");
        System.out.println("Orders  : http://localhost:" + PORT + "/api/orders");
        System.out.println("Web root: " + webRoot.toAbsolutePath());
        System.out.println("Press Ctrl+C to stop the server.");
        server.start();
    }

    private static Path locateWebRoot() throws IOException {
        Path cwd = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        if (Files.exists(cwd.resolve("index.html"))) return cwd;
        if (Files.exists(cwd.resolve("HOTEL management system/index.html"))) return cwd.resolve("HOTEL management system");
        if (Files.exists(cwd.resolve("../index.html"))) return cwd.getParent();
        // IDEs often set the working directory to the java folder.
        if (Files.exists(cwd.resolve("../index.html"))) return cwd.resolve("..").normalize();
        throw new IOException("Could not locate index.html. Run Server.java from the project root or java folder.");
    }

    private static void menu(HttpExchange ex) throws IOException {
        if (!allowMethod(ex, "GET")) return;
        send(ex, 200, "application/json; charset=utf-8", "{\"status\":\"success\",\"items\":" + MENU_JSON + "}");
    }

    private static synchronized void orders(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            StringBuilder out = new StringBuilder("{\"status\":\"success\",\"orders\":[");
            for (int i = 0; i < ORDERS.size(); i++) { if (i > 0) out.append(','); out.append(ORDERS.get(i)); }
            out.append("]}");
            send(ex, 200, "application/json; charset=utf-8", out.toString());
            return;
        }
        if (!method.equalsIgnoreCase("POST")) { sendError(ex, 405, "Use GET to list orders or POST to submit an order."); return; }
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            String customerName = stringField(body, "customerName");
            String email = stringField(body, "customerEmail");
            String phone = stringField(body, "customerPhone");
            String address = stringField(body, "customerAddress");
            String notes = stringField(body, "notes");
            String payment = stringField(body, "paymentMethod");
            String couponCode = stringField(body, "couponCode");
            if (customerName.isBlank() || email.isBlank() || phone.isBlank() || address.isBlank()) {
                sendError(ex, 400, "Customer name, email, phone and address are required."); return;
            }
            if (!List.of("UPI", "Credit / Debit Card", "Cash on Delivery").contains(payment)) { sendError(ex, 400, "Choose a valid payment method."); return; }
            String itemsText = arrayField(body, "items");
            if (itemsText == null) { sendError(ex, 400, "Order items are required."); return; }
            Matcher objectMatcher = Pattern.compile("\\{([^{}]*)\\}").matcher(itemsText);
            Map<Integer,Integer> quantities = new LinkedHashMap<>();
            while (objectMatcher.find()) {
                String item = objectMatcher.group(1);
                int id = (int) numberField(item, "id");
                int quantity = (int) numberField(item, "quantity");
                if (quantity < 1 || quantity > 99 || !menuHasId(id)) { sendError(ex, 400, "An order contains an invalid menu item or quantity."); return; }
                quantities.merge(id, quantity, Integer::sum);
            }
            if (quantities.isEmpty()) { sendError(ex, 400, "Your cart is empty."); return; }
            StringBuilder lines = new StringBuilder("[");
            double subtotal = 0;
            boolean first = true;
            Matcher menuMatcher = Pattern.compile("\\{\\\"id\\\":(\\d+),\\\"name\\\":\"((?:\\\\.|[^\"\\\\])*)\",\\\"category\\\":\"((?:\\\\.|[^\"\\\\])*)\",\\\"price\\\":([\\d.]+)").matcher(MENU_JSON);
            Map<Integer,MenuEntry> menu = readMenu();
            for (Map.Entry<Integer,Integer> entry : quantities.entrySet()) {
                MenuEntry dish = menu.get(entry.getKey());
                if (dish == null) { sendError(ex, 400, "Menu item not found."); return; }
                double lineTotal = dish.price * entry.getValue(); subtotal += lineTotal;
                if (!first) lines.append(','); first = false;
                lines.append("{\"id\":").append(dish.id).append(",\"name\":\"").append(escape(dish.name)).append("\",\"price\":").append(money(dish.price)).append(",\"quantity\":").append(entry.getValue()).append(",\"lineTotal\":").append(money(lineTotal)).append('}');
            }
            lines.append(']');
            double discount = "WELCOME10".equalsIgnoreCase(couponCode) ? Math.round(subtotal * 0.10 * 100.0) / 100.0 : 0; // validated demo coupon
            double taxable = Math.max(0, subtotal - discount);
            double gst = Math.round(taxable * 0.05 * 100.0) / 100.0;
            double delivery = subtotal <= 0 || subtotal >= 999 ? 0 : 40;
            double total = Math.round((taxable + gst + delivery) * 100.0) / 100.0;
            String orderId = "SD" + nextOrderNumber++;
            String order = "{\"orderId\":\"" + orderId + "\",\"customerName\":\"" + escape(customerName) + "\",\"customerEmail\":\"" + escape(email) + "\",\"customerPhone\":\"" + escape(phone) + "\",\"customerAddress\":\"" + escape(address) + "\",\"notes\":\"" + escape(notes) + "\",\"paymentMethod\":\"" + escape(payment) + "\",\"items\":" + lines + ",\"subtotal\":" + money(subtotal) + ",\"discount\":" + money(discount) + ",\"gst\":" + money(gst) + ",\"delivery\":" + money(delivery) + ",\"total\":" + money(total) + ",\"createdAt\":\"" + Instant.now().toString() + "\",\"status\":\"Received\"}";
            ORDERS.add(order);
            printInvoice(orderId, customerName, email, phone, address, notes, payment,
                    quantities, menu, subtotal, discount, gst, delivery, total);
            send(ex, 201, "application/json; charset=utf-8", "{\"status\":\"success\",\"message\":\"Order accepted by SmartDine backend.\",\"order\":" + order + "}");
        } catch (IllegalArgumentException err) {
            sendError(ex, 400, err.getMessage());
        } catch (Exception err) {
            err.printStackTrace();
            sendError(ex, 400, "Could not process the order. Check the submitted details.");
        }
    }

    /** Print a full itemized invoice in the Java server terminal. */
    private static void printInvoice(String orderId, String customerName, String email,
            String phone, String address, String notes, String payment,
            Map<Integer, Integer> quantities, Map<Integer, MenuEntry> menu,
            double subtotal, double discount, double gst, double delivery, double total) {
        System.out.println();
        System.out.println("+======================================================================+");
        System.out.println("|                         SMARTDINE RESTAURANT                         |");
        System.out.println("|                              TAX INVOICE                              |");
        System.out.println("+======================================================================+");
        System.out.printf("| Invoice No : %-24s Date : %-22s |%n", orderId,
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| Customer   : %-57s |%n", fit(customerName, 57));
        System.out.printf("| Email      : %-57s |%n", fit(email, 57));
        System.out.printf("| Phone      : %-57s |%n", fit(phone, 57));
        System.out.printf("| Address    : %-57s |%n", fit(address, 57));
        System.out.printf("| Payment    : %-57s |%n", fit(payment, 57));
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| %-3s | %-27s | %5s | %10s | %12s |%n", "No.", "Food Item", "Qty", "Unit Price", "Amount");
        System.out.println("|-----+-----------------------------+-------+------------+--------------|");
        int number = 1;
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            MenuEntry dish = menu.get(entry.getKey());
            if (dish == null) continue;
            int qty = entry.getValue();
            double lineTotal = dish.price * qty;
            System.out.printf("| %-3d | %-27s | %5d | %10s | %12s |%n",
                    number++, fit(dish.name, 27), qty, "₹" + money(dish.price), "₹" + money(lineTotal));
        }
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| %-48s %22s |%n", "Subtotal", "₹" + money(subtotal));
        System.out.printf("| %-48s %22s |%n", "Discount" + (discount > 0 ? " (WELCOME10)" : ""), "-₹" + money(discount));
        System.out.printf("| %-48s %22s |%n", "GST (5%)", "₹" + money(gst));
        System.out.printf("| %-48s %22s |%n", "Delivery fee", "₹" + money(delivery));
        System.out.println("+----------------------------------------------------------------------+");
        System.out.printf("| %-48s %22s |%n", "GRAND TOTAL", "₹" + money(total));
        System.out.println("+----------------------------------------------------------------------+");
        if (!notes.isBlank()) System.out.println("Special instructions: " + notes);
        System.out.println("Order status: Received");
        System.out.println("                         Thank you for dining with us!                  ");
        System.out.println("=======================================================================\n");
    }

    private static String fit(String value, int max) {
        if (value == null) return "";
        String clean = value.replace('\n', ' ').replace('\r', ' ');
        return clean.length() <= max ? clean : clean.substring(0, Math.max(0, max - 3)) + "...";
    }

    private static void test(HttpExchange ex) throws IOException {
        send(ex, 200, "application/json; charset=utf-8", "{\"status\":\"success\",\"message\":\"SmartDine Java backend connected\"}");
    }

    private static void staticFiles(HttpExchange ex) throws IOException {
        if (!ex.getRequestMethod().equalsIgnoreCase("GET") && !ex.getRequestMethod().equalsIgnoreCase("HEAD")) { sendError(ex, 405, "Only GET is supported for website files."); return; }
        String rawPath = ex.getRequestURI().getPath();
        String path;
        try { path = URLDecoder.decode(rawPath, StandardCharsets.UTF_8); } catch (Exception e) { path = "/"; }
        if (path.equals("/")) path = "/index.html";
        Path file = webRoot.resolve(path.substring(1)).normalize();
        if (!file.startsWith(webRoot) || !Files.exists(file) || Files.isDirectory(file)) { send(ex, 404, "text/plain; charset=utf-8", "404 - File not found"); return; }
        String type = contentType(file.getFileName().toString());
        byte[] bytes = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        if (ex.getRequestMethod().equalsIgnoreCase("HEAD")) { ex.sendResponseHeaders(200, -1); ex.close(); }
        else { ex.sendResponseHeaders(200, bytes.length); ex.getResponseBody().write(bytes); ex.close(); }
    }

    private static boolean allowMethod(HttpExchange ex, String method) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase(method)) return true;
        sendError(ex, 405, "Method not allowed."); return false;
    }
    private static void sendError(HttpExchange ex, int status, String message) throws IOException {
        send(ex, status, "application/json; charset=utf-8", "{\"status\":\"error\",\"message\":\"" + escape(message) + "\"}");
    }
    private static void send(HttpExchange ex, int status, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }
    private static String contentType(String name) {
        String n = name.toLowerCase();
        if (n.endsWith(".html")) return "text/html; charset=utf-8";
        if (n.endsWith(".css")) return "text/css; charset=utf-8";
        if (n.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (n.endsWith(".json")) return "application/json; charset=utf-8";
        if (n.endsWith(".svg")) return "image/svg+xml";
        if (n.endsWith(".png")) return "image/png";
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
        if (n.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }
    private static String stringField(String json, String key) {
        Matcher m = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\"\\\\])*)\\\"").matcher(json);
        if (!m.find()) return "";
        return m.group(1).replace("\\\"", "\"").replace("\\n", "\n").replace("\\r", "\r").replace("\\\\", "\\");
    }
    private static double numberField(String json, String key) {
        Matcher m = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)").matcher(json);
        if (!m.find()) throw new IllegalArgumentException("Missing numeric field: " + key);
        return Double.parseDouble(m.group(1));
    }
    private static String arrayField(String json, String key) {
        Matcher m = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL).matcher(json);
        return m.find() ? m.group(1) : null;
    }
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
    private static String money(double value) { return String.format(java.util.Locale.US, "%.2f", value); }
    private static boolean menuHasId(int id) { return readMenu().containsKey(id); }
    private static Map<Integer,MenuEntry> readMenu() {
        Map<Integer,MenuEntry> map = new LinkedHashMap<>();
        Matcher m = Pattern.compile("\\{\\\"id\\\":(\\d+),\\\"name\\\":\"((?:\\\\.|[^\"\\\\])*)\",\\\"category\\\":\"((?:\\\\.|[^\"\\\\])*)\",\\\"price\\\":([\\d.]+)").matcher(MENU_JSON);
        while (m.find()) map.put(Integer.parseInt(m.group(1)), new MenuEntry(Integer.parseInt(m.group(1)), unescape(m.group(2)), m.group(3), Double.parseDouble(m.group(4))));
        return map;
    }
    private static String unescape(String s) { return s.replace("\\\"", "\"").replace("\\\\", "\\"); }
    private static class MenuEntry {
        final int id; final String name, category; final double price;
        MenuEntry(int id, String name, String category, double price) { this.id=id; this.name=name; this.category=category; this.price=price; }
    }
}
