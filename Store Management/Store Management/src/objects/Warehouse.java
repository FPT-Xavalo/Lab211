/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Warehouse {

    private LocalDateTime creationTime;
    private String Code;
    private List<ReceiptProduct> listProducts = new ArrayList<>();
    private String type; //để hỗ trợ hàm toString

    public Warehouse() {
    }

    public Warehouse(String Code, LocalDateTime creationTime, String type) {
        this.Code = Code;
        this.creationTime = creationTime.now();
        this.type = type;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public List<ReceiptProduct> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ReceiptProduct> listProducts) {
        this.listProducts = listProducts;
    }

    public int calculateTotalQuantity() {
        int totalQuantity = 0;
        for (ReceiptProduct product : listProducts) {
            totalQuantity += product.getQuantity();
        }
        return totalQuantity;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return type + "{" + "creationTime=" + printDate(creationTime) + ", Code=" + Code + ", listProducts=" + listProducts.toString() + ", TotalQuantity=" + calculateTotalQuantity() + "}";
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
