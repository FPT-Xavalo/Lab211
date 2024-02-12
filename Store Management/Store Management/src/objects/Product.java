/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Admin
 */
public class Product  {

    private String productCode;
    private String productName;
    private String productGroup;

    public Product() {
    }

    public Product(String productCode, String productName, String productGroup) {
        this.productCode = productCode;
        this.productName = productName;
        this.productGroup = productGroup;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    @Override
    public String toString() {
        String msg;
        msg = String.format("| %-6s| %-15s| %-14s|", productCode, productName, productGroup);
        return msg;
    }

  
    
    

}
