/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_objects;

import static application.management.StoreManagement.rule;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class ProductGroups {
    
    public static final String FORDAILYUSE = "For daily use";
    public static final String LONGSHELFLIFE = "Long shelf";

    public ProductGroups() {
    }

    public static String getFORDAILYUSE() {
        return FORDAILYUSE;
    }

    public static String getLONGSHELFLIFE() {
        return LONGSHELFLIFE;
    }

    public String chooseGroup() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            printMenu();
            choice = rule.inputChoiceMain(2);
            switch (choice) {
                case 1:
                    return FORDAILYUSE;
                case 2:
                    return LONGSHELFLIFE;
            }
        } while (choice != 2);
        return null;
    }

    public static void printMenu() {
        System.out.println("Choose product group: ");
        System.out.println("+--------------------------------+");
        System.out.println("|            GROUP               |");
        System.out.println("|1.Products for daily use.       |");
        System.out.println("|2.Products with long shelf life.|");
        System.out.println("+--------------------------------+");
    }
}
