/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import data_objects.ProductDao;
import static data_objects.ProductGroups.FORDAILYUSE;
import static data_objects.ProductGroups.LONGSHELFLIFE;
import data_objects.WarehouseManagement;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import objects.ReceiptProduct;
import objects.Warehouse;

/**
 *
 * @author Admin
 */
public class Report implements IReport {

    String header = String.format("|%-5s|%-15s|%-15s|%-22s|%-18s|%10s|", " Code", " Name", " Group", " Manufacturing date", " Expiration date", " Quantity ");
    private Scanner sc = new Scanner(System.in);

    @Override
    public void showExpiredProduct(WarehouseManagement listReceipt) {
        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(listReceipt.getImportReceiptList());
        List<ReceiptProduct> expiredProducts = new ArrayList<>();
        for (int i = 0; i < allReiceipt.size(); i++) {
            Warehouse x = allReiceipt.get(i);
            List<ReceiptProduct> listProduct = x.getListProducts();
            for (int j = 0; j < listProduct.size(); j++) {
                ReceiptProduct product = listProduct.get(j);
                if (product.getProduct().getProductGroup().matches(LONGSHELFLIFE)) {
                    if (product.getManufacturingDate().isAfter(product.getExpirationDate())) {
                        expiredProducts.add(product);
                    }
                }
            }
        }
        if (expiredProducts.isEmpty()) {
            System.out.println("There are no expired products");
            return;
        }
        System.out.println("List of expired products:");
        System.out.println(header);
        for (int i = 0; i < expiredProducts.size(); i++) {
            System.out.println(expiredProducts.get(i).printWareHouse());
        }
    }

    @Override
    public void showProductSale(WarehouseManagement listReceipt) {
        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(listReceipt.getImportReceiptList());
        List<ReceiptProduct> productsSale = new ArrayList<>();
        for (int i = 0; i < allReiceipt.size(); i++) {
            Warehouse x = allReiceipt.get(i);
            List<ReceiptProduct> listProduct = x.getListProducts();
            for (int j = 0; j < listProduct.size(); j++) {
                ReceiptProduct product = listProduct.get(j);
                if (product.getProduct().getProductGroup().matches(LONGSHELFLIFE)) {
                    if (product.getQuantity() > 0 && product.getManufacturingDate().isBefore(product.getExpirationDate())) {
                        productsSale.add(product);
                    }
                } else if (product.getProduct().getProductGroup().matches(FORDAILYUSE)) {
                    productsSale.add(product);
                }
            }
        }

        if (productsSale.isEmpty()) {
            System.out.println("There are no products for sale.");
            return;
        }
        System.out.println("The products that the store is selling:");
        System.out.println(header);
        for (int i = 0; i < productsSale.size(); i++) {
            System.out.println(productsSale.get(i).printWareHouse());
        }
    }

    @Override
    public void showOutOfStock(WarehouseManagement listReceipt) {
        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(listReceipt.getImportReceiptList());
        List<ReceiptProduct> productsOutOfStock = new ArrayList<>();
        for (int i = 0; i < allReiceipt.size(); i++) {
            Warehouse x = allReiceipt.get(i);
            List<ReceiptProduct> listProduct = x.getListProducts();
            for (int j = 0; j < listProduct.size(); j++) {
                ReceiptProduct product = listProduct.get(j);
                if (product.getQuantity() < 3) {
                    productsOutOfStock.add(product);
                }
            }
        }
        if (productsOutOfStock.isEmpty()) {
            System.out.println("No products are out of stock.");
            return;
        }
        for (int i = 0; i < productsOutOfStock.size() - 1; i++) {
            for (int j = i + 1; j < productsOutOfStock.size(); j++) {
                if (productsOutOfStock.get(i).getQuantity() > productsOutOfStock.get(j).getQuantity()) {
                    ReceiptProduct tmp = productsOutOfStock.get(i);
                    productsOutOfStock.set(i, productsOutOfStock.get(j));
                    productsOutOfStock.set(j, tmp);
                }
            }
        }
        System.out.println("Products that are about to go out of stock gradually increase in quantity:");
        System.out.println(header);
        for (int i = 0; i < productsOutOfStock.size(); i++) {
            System.out.println(productsOutOfStock.get(i).printWareHouse());
        }
    }

    public void writeFileReceiptProduct(ProductDao productDao, WarehouseManagement listReceipt) {
        try {
            FileWriter writer = new FileWriter("src\\database\\warehouse.dat");
            List<Warehouse> result = getReceiptProduct(listReceipt);
            if (result.isEmpty()) {
                System.out.println("Product does not exist");
            } else {
                for (Warehouse x : result) {
                    writer.write(x.toString()+ "\n");
                }
            }
            System.out.println("Write succesfully!!!");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public List<Warehouse> getReceiptProduct(WarehouseManagement listReceipt) {
        System.out.print("Enter the product code: ");
        String productCode = sc.nextLine();

        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(listReceipt.getImportReceiptList());
        allReiceipt.addAll(listReceipt.getExportReceiptList());

        List<Warehouse> result = new ArrayList<>();
        for (int i = 0; i < allReiceipt.size(); i++) {
            Warehouse x = allReiceipt.get(i);
            List<ReceiptProduct> listProduct = x.getListProducts();
            for (int j = 0; j < listProduct.size(); j++) {
                ReceiptProduct product = listProduct.get(j);
                if (product.getProduct().getProductCode().equalsIgnoreCase(productCode)) {
                    result.add(x);
                }
            }
        }
        return result;
    }

    @Override
    public void showReceiptProduct(WarehouseManagement listReceipt) {
        List<Warehouse> result = getReceiptProduct(listReceipt);
        if (result.isEmpty()) {
            System.out.println("Product does not exist");
        }
        for (Warehouse x : result) {
            System.out.println(x.toString());
        }
    }

}
