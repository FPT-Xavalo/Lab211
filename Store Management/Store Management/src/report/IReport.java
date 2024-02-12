/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import data_objects.ProductDao;
import data_objects.WarehouseManagement;

/**
 *
 * @author Admin
 */
public interface IReport {
    public void showExpiredProduct(WarehouseManagement listReceipt);
    public void showProductSale(WarehouseManagement listReceipt);
    public void showOutOfStock(WarehouseManagement listReceipt);
    public void showReceiptProduct(WarehouseManagement listReceipt);
}
