/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_objects;

import static data_objects.ProductGroups.LONGSHELFLIFE;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
public class WarehouseManagement implements IWarehouseManagement {

    private List<Warehouse> importReceiptList = new ArrayList();
    private List<Warehouse> exportReceiptList = new ArrayList();
    private Scanner sc = new Scanner(System.in);
    Rule rule = new Rule();

    @Override
    public void createImportReceipt(ProductDao productDao) {
        Warehouse importReceipt = null;
        String type = "ImportReceipt";
        String importCode;
        if (importReceiptList.isEmpty()) {
            importCode = rule.inputReceiptCode("Enter import receipt code: ", "Import code must be 7 digits!!!");
        } else {
            Warehouse lastReceipt = importReceiptList.get(importReceiptList.size() - 1);
            String codeLastReceipt = lastReceipt.getCode();
            int code = Integer.parseInt(codeLastReceipt);
            code++;
            importCode = String.format("%07d", code);
        }

        importReceipt = new Warehouse(importCode, LocalDateTime.MIN, type);
        List<ReceiptProduct> receiptProducts = importReceipt.getListProducts();

        while (true) {
            System.out.print("Enter product code (0 to finish):");
            String productCode = sc.nextLine();

            if (productCode.equalsIgnoreCase("0")) {
                break;
            }
            Product product = productDao.findProductObject(productCode);
            ReceiptProduct tmp = findProductObjectInReceipt(receiptProducts, productCode);

            if (product == null) {
                //Mặt hàng k có trong cửa hàng
                System.out.println("Product not found!");
                continue;

            } else if (product != null) {
                //Mặt hàng có trong cửa hàng
                addProductToReceipt(product, receiptProducts);

            } else if (product != null && tmp != null) {
                //Mặt hàng có sẵn trong cửa hàng và đã có trong biên lai

                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(sc.nextLine());
                tmp.setQuantity(tmp.getQuantity() + quantity);
                System.out.println("Product added to import receipt!");

            }
        }
        if (importReceipt.getListProducts().isEmpty()) {
            System.out.println("Empty product. Create receipt failed!");
            return;
        }

        if (confirm() == true) {
            importReceiptList.add(importReceipt);
            System.out.println("Receipt created successfully!");
            System.out.println("Here is your Receipt!!!");
            printImportReceipt(importReceipt);
        } else {
            importReceipt = null;
            System.out.println("Create receipt failed!");
        }
    }

    @Override
    public void createExportReceipt(ProductDao productDao) {
        Warehouse exportReceipt = null;
        List<ReceiptProduct> wareHouseProducts = getAllImportProduct();
        String type = "ExportReceipt";
        String exportCode;

        if (exportReceiptList.isEmpty()) {
            exportCode = rule.inputReceiptCode("Enter export receipt code: ", "Invalid input. Please enter a 7-digit.");
        } else {
            Warehouse lastReceipt = exportReceiptList.get(exportReceiptList.size() - 1);
            String codeLastReceipt = lastReceipt.getCode();
            int code = Integer.parseInt(codeLastReceipt);
            code++;
            exportCode = String.format("%07d", code);
        }

        exportReceipt = new Warehouse(exportCode, LocalDateTime.MIN, type);
        List<ReceiptProduct> receiptProduct = exportReceipt.getListProducts();

        while (true) {
            System.out.print("Enter product code (0 to finish):");
            String productCode = sc.nextLine();

            if (productCode.equalsIgnoreCase("0")) {
                break;
            }
            Product product = productDao.findProductObject(productCode);
            ReceiptProduct tmp = findProductObjectInReceipt(receiptProduct, productCode);
            boolean checkProductInImpReceipt = checkReceiptProductInWareHouse(product);

            if (product == null || !checkProductInImpReceipt) {
                //Mặt hàng không có trong cửa hàng và không có trong importList

                System.out.println("Product not found or expired!");
                continue;
            }

            int quantity = rule.getAnQuatity("Enter quantity: ", "Error: Invalid quantity.");
            if (quantity == 0) {
                System.out.println("Error: Invalid quantity.");
                continue;
            }
            if (product != null && tmp == null && checkProductInImpReceipt) {
                //Mặt hàng đã có trong cửa hàng có số lượng đáp ứng đủ của cửa hàng và chưa có trong biên lai

                ReceiptProduct x = getReceiptProductInWareHouse(product, quantity);
                if (x != null && x.getQuantity() >= quantity) {

                    ReceiptProduct exportProduct = new ReceiptProduct(product, x.getManufacturingDate(), x.getExpirationDate(), quantity);
                    receiptProduct.add(exportProduct);
                    x.setQuantity(x.getQuantity() - quantity);
                    System.out.println("Product added to export receipt!");

                } else {
                    System.out.println("Not enough quantity! Remaining quantity: " + x.getQuantity());
                }

            } else if (product != null && tmp != null) {
                //Mặt hàng đã có trong cửa hàng có số lượng đáp ứng đủ của cửa hàng và đã có sẵn trong biên lai

                ReceiptProduct x = getReceiptProductInWareHouse(product, quantity);
                if (x != null && x.getQuantity() >= quantity) {
                    //Số lượng có sẵn   số lượng nhập   số lượng hàng có trong biên lai nhập lúc trc

                    tmp.setQuantity(tmp.getQuantity() + quantity);
                    x.setQuantity(x.getQuantity() - quantity);
                    System.out.println("Product added to export receipt!");
                } else {
                    System.out.println("Not enough quantity! Remaining quantity: " + x.getQuantity());
                }

            }
        }

        if (exportReceipt.getListProducts().isEmpty()) {
            System.out.println("Empty product. Create receipt failed!");
            return;
        }

        if (confirm() == true) {
            exportReceiptList.add(exportReceipt);
            System.out.println("Receipt created successfully!");
            System.out.println("Here is your Receipt!!!");
            printExportReceipt(exportReceipt, wareHouseProducts);
        } else {
            for (ReceiptProduct x : exportReceipt.getListProducts()) {
                for (ReceiptProduct y : wareHouseProducts) {
                    //trả lại số lượng trong importProduct 

                    if (y.getProduct().equals(x.getProduct()) && y.getManufacturingDate().equals(x.getManufacturingDate()) && y.getExpirationDate().equals(x.getExpirationDate())) {
                        y.setQuantity(y.getQuantity() + x.getQuantity());
                    }
                }
            }

            exportReceipt = null;
            System.out.println("Create receipt failed");
        }
    }

    public void printExportReceipt(Warehouse exportReceipt, List<ReceiptProduct> wareHouseProducts) {
        System.out.println("+--------------------------------------------------------------------------------+");
        System.out.println("|                               EXPORT RECEIPT                                   |");
        System.out.printf("|Receipt Code: %66s|\n", exportReceipt.getCode());
        System.out.printf("|Creation Time: %65s|\n", exportReceipt.getCreationTime());
        System.out.println("+--------------------------------------------------------------------------------+");

        System.out.println("| Product Name |     Group     | Manufacturing date | Expiration date | Quantity |");
        for (ReceiptProduct x : exportReceipt.getListProducts()) {
            System.out.printf("| %-13s| %-14s| %-19s| %-16s| %-9s|\n",
                    x.getProduct().getProductName(),x.getProduct().getProductGroup(), printDate(x.getManufacturingDate()), printDate(x.getExpirationDate()), x.getQuantity());
        }
        System.out.println("+--------------------------------------------------------------------------------+");
        System.out.printf("|Total quantity: %64d|\n", exportReceipt.calculateTotalQuantity());
        System.out.println("+--------------------------------------------------------------------------------+");

    }

    public void printImportReceipt(Warehouse importReceipt) {
        System.out.println("+--------------------------------------------------------------------------------+");
        System.out.println("|                              IMPORT RECEIPT                                    |");
        System.out.printf("|Receipt Code: %66s|\n", importReceipt.getCode());
        System.out.printf("|Creation Time: %65s|\n", importReceipt.getCreationTime());
        System.out.println("+--------------------------------------------------------------------------------+");

        System.out.println("| Product Name |     Group     | Manufacturing date | Expiration date | Quantity |");

        for (ReceiptProduct x : importReceipt.getListProducts()) {
            System.out.printf("| %-13s| %-14s| %-19s| %-16s| %-9s|\n",
                    x.getProduct().getProductName(), x.getProduct().getProductGroup(), printDate(x.getManufacturingDate()), printDate(x.getExpirationDate()), x.getQuantity());
        }
        System.out.println("+--------------------------------------------------------------------------------+");
        System.out.printf("|Total quantity: %64d|\n", importReceipt.calculateTotalQuantity());
        System.out.println("+--------------------------------------------------------------------------------+");

    }

    public List<Warehouse> getImportReceiptList() {
        return importReceiptList;
    }

    public List<Warehouse> getExportReceiptList() {
        return exportReceiptList;
    }

    public ReceiptProduct findProductObjectInReceipt(List<ReceiptProduct> receiptProduct, String productCode) {
        //Tìm kiếm sản phẩm trong biên lai

        if (receiptProduct.isEmpty()) {
            return null;
        }
        for (int i = 0; i < receiptProduct.size(); i++) {
            if (receiptProduct.get(i).getProduct().getProductCode().equalsIgnoreCase(productCode)) {
                return receiptProduct.get(i);
            }
        }
        return null;
    }

    public Product getProductInWareHouse(Product p) {
        //Tìm Product trong biên lai
        List<Warehouse> allReiceipt = new ArrayList<>();
        allReiceipt.addAll(importReceiptList);
        allReiceipt.addAll(exportReceiptList);
        for (int i = 0; i < allReiceipt.size(); i++) {
            List<ReceiptProduct> list = allReiceipt.get(i).getListProducts();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getProduct().getProductCode().equalsIgnoreCase(p.getProductCode())) {
                    return p;
                }
            }
        }
        return null;
    }

    public ReceiptProduct getReceiptProductInWareHouse(Product p, int quantity) {
        //Tìm ProductReceipt trong biên lai import
        int max = -1, maxButNot = -1;
        List<Warehouse> listImpProduct = new ArrayList<>(importReceiptList);
        ReceiptProduct result = null;
        ReceiptProduct resultNotEnough = null;
        for (int i = 0; i < listImpProduct.size(); i++) {
            List<ReceiptProduct> list = listImpProduct.get(i).getListProducts();
            for (int j = 0; j < list.size(); j++) {
                ReceiptProduct x = list.get(j);
                if (x.getProduct().getProductCode().equalsIgnoreCase(p.getProductCode()) && x.getQuantity() >= quantity) {
                    if (max <= x.getQuantity()) {
                        max = x.getQuantity();
                        result = x;
                    }
                } else if (x.getProduct().getProductCode().equalsIgnoreCase(p.getProductCode()) && x.getQuantity() < quantity) {
                    if (maxButNot <= x.getQuantity()) {
                        maxButNot = x.getQuantity();
                        resultNotEnough = x;
                    }
                }
            }
        }
        if (result == null) {
            return resultNotEnough;
        }
        return result;
    }

    public boolean checkReceiptProductInWareHouse(Product p) {
        //check xem product đã nằm trong biên lai chưa
        if (p == null) {
            return false;
        }
        List<Warehouse> imptProducts = new ArrayList<>(importReceiptList);
        for (int i = 0; i < imptProducts.size(); i++) {
            List<ReceiptProduct> list = imptProducts.get(i).getListProducts();
            for (int j = 0; j < list.size(); j++) {
                ReceiptProduct x = list.get(j);
                if (x.getProduct().getProductCode().equalsIgnoreCase(p.getProductCode()) && x.getManufacturingDate().isBefore(x.getExpirationDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addProductToReceipt(Product product, List<ReceiptProduct> receiptProducts) {
        LocalDateTime manufacturingDate = null;
        LocalDateTime expirationDate = null;
        int quantity;

        if (product.getProductGroup().matches(LONGSHELFLIFE)) {
            manufacturingDate = inputDate("Manufacturing Date (dd/MM/yyyy): ");
            expirationDate = inputDate("Expiration Date (dd/MM/yyyy): ");

        } else {
            manufacturingDate = inputDate("Manufacturing Date (dd/MM/yyyy): ");
            expirationDate = manufacturingDate.plusDays(3);
        }

        quantity = rule.getAnQuatity("Input product quantity: ", "Error: Invalid quantity!!");
        if (quantity == 0) {
            System.out.println("Error: Invalid quantity!!");
            return;
        }
        
        ReceiptProduct receiptProduct = new ReceiptProduct(product, manufacturingDate, expirationDate, quantity);
        receiptProducts.add(receiptProduct);

        System.out.println("Product added to import receipt!");
    }

    public LocalDateTime inputDate(String msg) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = null;
        String dateString;
        while (dateTime == null) {
            try {
                System.out.print(msg);
                dateString = sc.nextLine().trim();
                dateTime = LocalDate.parse(dateString, formatter).atTime(LocalTime.MIDNIGHT);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
        return dateTime;
    }

    public String printDate(LocalDateTime dateTime) {
        String date = null;
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = dateTime.format(formatter);
        }
        return date;
    }

    public boolean confirm() {
        int result = rule.checkConfirm("Do you agree to create receipts?(No -> 0, Yes -> 1):", "Error: Invalid choice.");
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public List<ReceiptProduct> getAllImportProduct() {
        List<ReceiptProduct> impProduct = new ArrayList<>();

        for (int i = 0; i < importReceiptList.size(); i++) {
            Warehouse oneImportReceipt = importReceiptList.get(i);
            for (int j = 0; j < oneImportReceipt.getListProducts().size(); j++) {
                ReceiptProduct oneProduct = oneImportReceipt.getListProducts().get(j);
                impProduct.add(oneProduct);
            }
        }
        return impProduct;
    }

}
