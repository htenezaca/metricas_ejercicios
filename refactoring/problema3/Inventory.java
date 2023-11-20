package refactoring.problema3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Inventory {

    static final String CSV_FILE_PRODUCTS = "./refactoring/problema3/data/products.csv";
    static final String CSV_FILE_SALES = "./refactoring/problema3/data/sales.csv";
    static final String CSV_FILE_ORDERS = "./refactoring/problema3/data/orders.csv";

    public static void main(String[] args) {

        List<Product> products = readFromCSV(CSV_FILE_PRODUCTS, data -> new Product(
                Integer.parseInt(data[0]),
                data[1],
                Integer.parseInt(data[2])));

        List<Sale> sales = readFromCSV(CSV_FILE_SALES, data -> new Sale(
                Integer.parseInt(data[0].trim()),
                data[1].trim(),
                Integer.parseInt(data[2].trim()),
                Integer.parseInt(data[3].trim())));

        List<Order> orders = readFromCSV(CSV_FILE_ORDERS, data -> new Order(
                Integer.parseInt(data[0].trim()),
                data[1].trim(),
                Integer.parseInt(data[2].trim()),
                Integer.parseInt(data[3].trim())));

        for (Order order : orders) {
            Product item = products.get(order.getItemId());
            item.setQuantity(item.getQuantity() + order.getQuantity());
        }

        for (Sale sale : sales) {
            Product item = products.get(sale.getItemId());
            item.setQuantity(item.getQuantity() - sale.getQuantity());
        }

        for (Product product : products) {
            System.out.println(product.getItem() + " " + product.getQuantity());
        }
    }

    private static <T> List<T> readFromCSV(String csvFile, Function<String[], T> createObject) {
        List<T> objects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Ignora la primera línea (encabezad)
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                objects.add(createObject.apply(data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
