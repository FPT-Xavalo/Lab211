/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_objects;

public interface IProductDao {
    public void addProduct();
    public void updateProduct(String productCode, WarehouseManagement wareHouseManagement);
    public void deleteProduct(String productCode, WarehouseManagement wareHouseManagement);
    public void printAllProducts();
}
