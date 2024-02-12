/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_objects;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import objects.Product;
import objects.ReceiptProduct;
import objects.Warehouse;
import util.Rule;

/**
 *
 * @author Admin
 */
public class ProductDao implements IProductDao {

    ProductGroups group = new ProductGroups();
    private List<Product> products = new ArrayList<Product>();
    Scanner sc = new Scanner(System.in);
    Rule rule = new Rule();

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void addProduct() {
        String productCode;
        String productName;
        String productGroup;

        while (true) {
            System.out.print("Input product code: ");
            productCode = sc.nextLine();
            if (isProductCodeDuplicate(productCode)) {
                System.out.println("Error: Product code already exists!!!");
            } else if (productCode.isEmpty()) {
                System.out.println("This field cannot be left blank!!!");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Input product name: ");
            productName = sc.nextLine();
            if (productName.isEmpty()) {
                System.out.println("This field cannot be left blank!!!");
            } else if (isProductNameDuplicate(productName)) {
                System.out.println("Error: Product name already exists!!!");
            } else {
                break;
            }
        }

        productGroup = group.chooseGroup();

        Product product = new Product(productCode, productName, productGroup);
        products.add(product);

        System.out.println("Add product successfully");
    }

    @Override
    public void updateProduct(String productCode, WarehouseManagement listReceipt) {
        String header = String.format("| %-6s| %-15s| %-14s|", "Code", " Name", " Group");
        String productName1 = null, productGroup1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductCode().equalsIgnoreCase(productCode)) {

                while (true) {
                    System.out.print("Input new name: ");
                    productName1 = sc.nextLine();
                    if (isProductNameDuplicate(productName1)) {
                        System.out.println("Error: Product name already exists!!!");
                    } else {
                        break;
                    }
                }

                if (productName1.isEmpty()) {
                    System.out.println("Product information remains unchanged!");
                    System.out.println(header);
                    System.out.println(products.get(i).toString());
                    return;
                }

                productGroup1 = group.chooseGroup();

                Product productUpdate = new Product(productCode, productName1, productGroup1);
                products.set(i, productUpdate);
                System.out.println("Product information updated successfully!");
                System.out.println(header);
                System.out.println(products.get(i).toString());
                updateProductInReceipt(productCode, productName1, listReceipt);
                return;
            }
        }

        System.out.println("Product does not exist");
    }

    public void updateProductInReceipt(String productCode, String productName1, WarehouseManagement listReceipt) {
        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(listReceipt.getImportReceiptList());
        allReiceipt.addAll(listReceipt.getExportReceiptList());
        for (int i = 0; i < allReiceipt.size(); i++) {
            Warehouse x = allReiceipt.get(i);
            List<ReceiptProduct> listProduct = x.getListProducts();
            for (int j = 0; j < listProduct.size(); j++) {
                ReceiptProduct product = listProduct.get(j);
                if (product.getProduct().getProductCode().equalsIgnoreCase(productCode)) {
                    product.getProduct().setProductName(productName1);
                }
            }
        }
    }

    @Override
    public void deleteProduct(String productCode, WarehouseManagement wareHouseManagement) {
        int flag = 0;
        boolean isInWareHouse = checkProductInWareHouse(productCode, wareHouseManagement);
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductCode().equalsIgnoreCase(productCode)) {
                if (!isInWareHouse && confirm()) {
                    products.remove(i);
                    System.out.println("Delete successful!");
                    flag = 1;
                } else {
                    System.out.println("Delete failed!");
                    flag = 1;
                }
            }
        }
        if (flag == 0) {
            System.out.println("Product not found!");
        }
    }

    public boolean isProductCodeDuplicate(String productCode) {
        for (Product x : products) {
            if (x.getProductCode().equalsIgnoreCase(productCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean isProductNameDuplicate(String productName) {
        for (Product x : products) {
            if (x.getProductName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void printAllProducts() {
        if (products.isEmpty()) {
            System.out.println("Product not found!");
            return;
        }
        String header = String.format("|%-7s|%-16s|%-15s|", " CODE", " NAME", " GROUP");
        System.out.println(header);
        for (Product x : products) {
            System.out.println(x.toString());
        }
    }

    public boolean confirm() {
        System.out.println("Confirm you want to delete?(No -> 0, Yes -> 1)");
        int result = Integer.parseInt(sc.nextLine());
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Product findProductObject(String productCode) {
        if (products.isEmpty()) {
            return null;
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductCode().equalsIgnoreCase(productCode)) {
                return products.get(i);
            }
        }
        return null;
    }

    public void getProductFromFile(String path) {
        try {
            Scanner scanner = new Scanner(new FileReader(path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length > 1) {
                    String productCode = data[0];
                    if (isProductCodeDuplicate(productCode)) {
                        continue;
                    }
                    String productName = data[1];
                    String productGroup = data[2];

                    Product product = new Product(productCode, productName, productGroup);
                    products.add(product);
                }
            }
            System.out.println("Add the product successfully!");
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkProductInWareHouse(String productCode, WarehouseManagement wareHouseManagement) {
        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(wareHouseManagement.getImportReceiptList());
        allReiceipt.addAll(wareHouseManagement.getExportReceiptList());

        for (int i = 0; i < allReiceipt.size(); i++) {
            Warehouse x = allReiceipt.get(i);
            List<ReceiptProduct> listProduct = x.getListProducts();
            for (int j = 0; j < listProduct.size(); j++) {
                ReceiptProduct product = listProduct.get(j);
                if (product.getProduct().getProductCode().equalsIgnoreCase(productCode)) {
                    return true;
                }
            }
        }
        return false;
    }

}
