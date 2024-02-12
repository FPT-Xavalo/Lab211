/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */
public class ReceiptProduct {

    private Product product;
    private int quantity;
    private LocalDateTime manufacturingDate;
    private LocalDateTime expirationDate;

    public ReceiptProduct(Product product, LocalDateTime manufacturingDate, LocalDateTime expirationDate, int quantity) {

        this.product = product;
        this.quantity = quantity;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDateTime manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String toString() {
        return "(" + product.getProductCode() + "," + product.getProductName() + "," + product.getProductGroup() + "," + printDate(manufacturingDate) + "," + printDate(expirationDate) + "," + quantity + ")";
    }

    public String printWareHouse() {
        String msg;
        msg = String.format("| %-4s| %-14s| %-14s| %-21s| %-17s| %-9d|", product.getProductCode(), product.getProductName(), product.getProductGroup(), printDate(manufacturingDate), printDate(expirationDate), quantity);
        return msg;

    }

    public String printDate(LocalDateTime dateTime) {
        String date;
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = dateTime.format(formatter);
        } else {
            date = "N/A";
        }
        return date;
    }

}
