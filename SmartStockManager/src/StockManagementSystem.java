import java.sql.*;
import java.util.Scanner;

public class StockManagementSystem {

    static final String url = "jdbc:mysql://localhost:3306/stock_management";
    static final String user = "root";
    static final String password = "root123";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Driver not found!");
        }

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== STOCK MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Low Stock Alert");
            System.out.println("7. Total Stock Value");
            System.out.println("8. Exit");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addProduct(sc);
                    break;

                case 2:
                    viewProducts();
                    break;

                case 3:
                    searchProduct(sc);
                    break;

                case 4:
                    updateProduct(sc);
                    break;

                case 5:
                    deleteProduct(sc);
                    break;

                case 6:
                    lowStock();
                    break;

                case 7:
                    totalValue();
                    break;

                case 8:
                    System.out.println("Exited Successfully!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // ---------------- ADD ----------------
    static void addProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.print("Enter ID: ");
            int id = sc.nextInt();

            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            System.out.print("Enter Price: ");
            double price = sc.nextDouble();

            String sql = "INSERT INTO products VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, qty);
            ps.setDouble(4, price);

            ps.executeUpdate();
            System.out.println("Product Added Successfully!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- VIEW ----------------
    static void viewProducts() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT * FROM products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\nID | NAME | QTY | PRICE");

            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " +
                        rs.getString(2) + " | " +
                        rs.getInt(3) + " | " +
                        rs.getDouble(4));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- SEARCH ----------------
    static void searchProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.print("Enter ID: ");
            int id = sc.nextInt();

            String sql = "SELECT * FROM products WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Found: " + rs.getString("name") +
                        " | Qty: " + rs.getInt("quantity") +
                        " | Price: " + rs.getDouble("price"));
            } else {
                System.out.println("Product Not Found!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- UPDATE ----------------
    static void updateProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.print("Enter ID: ");
            int id = sc.nextInt();

            System.out.print("Enter New Quantity: ");
            int qty = sc.nextInt();

            System.out.print("Enter New Price: ");
            double price = sc.nextDouble();

            String sql = "UPDATE products SET quantity=?, price=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, qty);
            ps.setDouble(2, price);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Updated Successfully!");
            else
                System.out.println("Product Not Found!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- DELETE ----------------
    static void deleteProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.print("Enter ID: ");
            int id = sc.nextInt();

            String sql = "DELETE FROM products WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Deleted Successfully!");
            else
                System.out.println("Product Not Found!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- LOW STOCK ----------------
    static void lowStock() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT * FROM products WHERE quantity < 5";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\nLOW STOCK ITEMS:");

            while (rs.next()) {
                System.out.println(rs.getString("name") +
                        " | Qty: " + rs.getInt("quantity"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- TOTAL VALUE ----------------
    static void totalValue() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT SUM(quantity * price) AS total FROM products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                System.out.println("Total Stock Value = ₹" + rs.getDouble(1));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}