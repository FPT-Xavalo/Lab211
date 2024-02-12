package application.management;

import data_objects.StoreData;
import data_objects.WarehouseManagement;
import data_objects.ProductDao;
import java.util.List;
import report.Report;
import java.util.Scanner;
import objects.Product;
import util.Rule;

public class StoreManagement {

    public static Scanner sc = new Scanner(System.in);
    public static Rule rule = new Rule();

    public static void main(String[] args) {
        int choice;
        ProductDao productDao = new ProductDao();
        WarehouseManagement wareHouseManagement = new WarehouseManagement();
        List<Product> products = productDao.getProducts();
        do {
            printMenu();
            choice = rule.inputChoiceMain(5);
            switch (choice) {
                case 1:
                    productManagement(productDao, wareHouseManagement);
                    break;
                case 2:
                    warehouseManagement(productDao, wareHouseManagement);
                    break;
                case 3:
                    Report(products, productDao, wareHouseManagement);
                    break;
                case 4:
                    storeData(productDao, wareHouseManagement);
                    break;
                case 5:
                    System.out.println("Bye bye, see you next time");
                    break;
            }
        } while (choice != 5);

    }

    public static void productManagement(ProductDao productDao, WarehouseManagement wareHouseManagement) {
        int choice;
        do {
            printMenuProductManagement();
            choice = rule.inputChoiceMain(5);
            switch (choice) {
                case 1:
                    productDao.addProduct();
                    break;
                case 2:
                    System.out.print("Enter product code you want to update: ");
                    String uCode = sc.nextLine();
                    productDao.updateProduct(uCode, wareHouseManagement);
                    break;
                case 3:
                    System.out.print("Enter product code you want to delete: ");
                    String dCode = sc.nextLine();
                    productDao.deleteProduct(dCode, wareHouseManagement);
                    break;
                case 4:
                    productDao.printAllProducts();
                    break;
                case 5:
                    System.out.println("Quitting...!");
                    break;

            }
        } while (choice != 5);
    }

    public static void warehouseManagement(ProductDao productDao, WarehouseManagement wareHouseManagement) {
        int choice;
        do {
            printMenuWarehouseManagement();
            choice = rule.inputChoiceMain(3);
            switch (choice) {
                case 1:
                    wareHouseManagement.createImportReceipt(productDao);
                    break;
                case 2:
                    wareHouseManagement.createExportReceipt(productDao);
                    break;
                case 3:
                    System.out.println("Quitting...!");
                    break;
            }
        } while (choice != 3);
    }

    public static void Report(List<Product> products, ProductDao productDao, WarehouseManagement wareHouseManagement) {
        int choice;
        Report report = new Report();
        do {
            printMenuReport();
            choice = rule.inputChoiceMain(5);
            switch (choice) {
                case 1:
                    report.showExpiredProduct(wareHouseManagement);
                    break;
                case 2:
                    report.showProductSale(wareHouseManagement);
                    break;
                case 3:
                    report.showOutOfStock(wareHouseManagement);
                    break;
                case 4:
                    showProductData(report, products, productDao, wareHouseManagement);
                    break;
                case 5:
                    System.out.println("Quitting...!");
                    break;
            }
        } while (choice != 5);
    }

    public static void showProductData(Report report, List<Product> products, ProductDao productDao, WarehouseManagement wareHouseManagement) {
        int choice;
        do {
            printMenuDisplayData();
            choice = rule.inputChoiceMain(3);
            switch (choice) {
                case 1:
                    report.writeFileReceiptProduct(productDao, wareHouseManagement);
                    break;
                case 2:
                    report.showReceiptProduct(wareHouseManagement);
                    break;
                case 3:
                    System.out.println("Quitting...!");
                    break;
            }
        } while (choice != 3);
    }

    public static void storeData(ProductDao productDao, WarehouseManagement wareHouseManagement) {
        int choice;
        StoreData storeData = new StoreData();
        do {
            printMenuStoreData();
            choice = rule.inputChoiceMain(4);
            switch (choice) {
                case 1:
                    storeData.storeListProduct(productDao);
                    break;
                case 2:
                    storeData.storeWarehouseInformation(wareHouseManagement);
                    break;
                case 3:
                    productDao.getProductFromFile("src\\database\\product.dat");
                    break;
                case 4:
                    System.out.println("Quitting...!");
                    break;
            }
        } while (choice != 4);
    }

    public static void printMenu() {
        System.out.println("+------------------------------------------------------------------+");
        System.out.println("|               STORE MANAGEMENT AT CONVENIENCE STORE              |");
        System.out.println("|- Choose the following functions to work:                         |");
        System.out.println("|1. Manage products/items of the store.                            |");
        System.out.println("|2. Warehouse management.                                          |");
        System.out.println("|3. Report.                                                        |");
        System.out.println("|4. Store data to files.                                           |");
        System.out.println("|5. Quit.                                                          |");
        System.out.println("+------------------------------------------------------------------+");
    }

    public static void printMenuProductManagement() {
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("|1. Manage products/items of the store:                          |");
        System.out.println("|       1.1 Add a new product.                                   |");
        System.out.println("|       1.2 Update product information.                          |");
        System.out.println("|       1.3 Delete product.                                      |");
        System.out.println("|       1.4 Show all product.                                    |");
        System.out.println("|       1.5 Quit.                                                |");
        System.out.println("+----------------------------------------------------------------+");
    }

    public static void printMenuWarehouseManagement() {
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("|2. Manage products/items of the store:                          |");
        System.out.println("|       2.1 Create an import receipt.                            |");
        System.out.println("|       2.2 Create an export receipt.                            |");
        System.out.println("|       2.3 Quit.                                                |");
        System.out.println("+----------------------------------------------------------------+");
    }

    public static void printMenuReport() {
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("|3. Report:                                                      |");
        System.out.println("|       3.1 Products that have expired.                          |");
        System.out.println("|       3.2 The products that the store is selling.              |");
        System.out.println("|       3.3 Products that are running out of stock.              |");
        System.out.println("|       3.4 Import/export receipt of a product.                  |");
        System.out.println("|       3.5 Quit.                                                |");
        System.out.println("+----------------------------------------------------------------+");
    }

    public static void printMenuDisplayData() {
        System.out.println("+-----------------------------------------------------------+");
        System.out.println("|3.4. Shows product’s data:                                 |");
        System.out.println("|       1 The warehouse.dat file.                           |");
        System.out.println("|       2 Warehouse’s collection into the screen.           |");
        System.out.println("|       3 Quit.                                             |");
        System.out.println("+-----------------------------------------------------------+");
    }

    public static void printMenuStoreData() {
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("|4. Store data to files:                                         |");
        System.out.println("|       4.1 The list product.                                    |");
        System.out.println("|       4.2 List warehouse information.                          |");
        System.out.println("|       4.3 Add product from file.                               |");
        System.out.println("|       4.4 Quit.                                                |");
        System.out.println("+----------------------------------------------------------------+");
    }

}
