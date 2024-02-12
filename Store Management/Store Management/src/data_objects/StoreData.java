/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_objects;

import java.io.FileWriter;
import java.io.IOException;
import objects.Product;
import objects.Warehouse;

/**
 *
 * @author Admin
 */
public class StoreData {

    String header = String.format("|%-6s|%-15s|%-22s|%-18s|%10s|", "Code", " Name", " Manufacturing date", " Expiration date", " Quantity ");
    private static final String PRODUCT_FILE = "src\\database\\product.dat";
    private static final String WAREHOUSE_FILE = "src\\database\\warehouse.dat";

    public void storeListProduct(ProductDao productDao) {
        try {
            FileWriter writer = new FileWriter(PRODUCT_FILE);
            for (Product x : productDao.getProducts()) {
                writer.write(x.getProductCode() + "," + x.getProductName() + "," + x.getProductGroup() + "\n");
            }
            writer.close();
            System.out.println("Write succesfully!!!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void storeWarehouseInformation(WarehouseManagement wareHouseManagement) {
        try {
            FileWriter writer = new FileWriter(WAREHOUSE_FILE);

            for (Warehouse x : wareHouseManagement.getImportReceiptList()) {
                writer.write(x.toString()+ "\n");
            }
            for (Warehouse x : wareHouseManagement.getExportReceiptList()) {
                writer.write(x.toString()+ "\n");
            }
            writer.close();
            System.out.println("Write succesfully!!!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}
