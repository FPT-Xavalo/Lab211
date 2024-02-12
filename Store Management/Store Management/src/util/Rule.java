/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Scanner;

public class Rule {

    Scanner sc = new Scanner(System.in);

    //những quy tắc cần phải tuân theo và các ngoại lệ khác
    //có thể kiểm tra số nguyên trong 1 range/khoảng cho trước
    //có câu thông báo động, tùy ngữ cảnh 
    public int getAnQuatity(String inputMsg, String errorMsg) {
        int n;
        while (true) {
            try {
                System.out.print(inputMsg);
                n = Integer.parseInt(sc.nextLine());
                if (n < 0) {
                    System.out.println(errorMsg);
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println(errorMsg);
            }
        }

        return n;
    }

    public int inputChoiceMain(int numberChoice) {
        int n;
        while (true) {
            try {
                System.out.print("Input your choice (1..." + numberChoice + "): ");
                n = Integer.parseInt(sc.nextLine());
                if (n < 1 || n > numberChoice) {
                    throw new Exception();
                }
                break;
            } catch (Exception e) {
                System.out.println("Choose from 1 to " + numberChoice + "!");
            }
        }
        return n;
    }

    public String inputReceiptCode(String inputMsg, String errorMsg) {
        int n;
        while (true) {
            try {
                System.out.print(inputMsg);
                String input = sc.nextLine();
                if (input.length() == 7 && input.matches("\\d+")) {
                    n = Integer.parseInt(input);
                    String formattedNumber = String.format("%07d", n);
                    return formattedNumber;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(errorMsg);
            }
        }
    }

    public int checkConfirm(String inputMsg, String errorMsg) {
        int n;
        while (true) {
            try {
                System.out.print(inputMsg);
                n = Integer.parseInt(sc.nextLine());
                if (n < 0 || n > 1) {
                    throw new Exception();
                }
                break;
            } catch (Exception e) {
                System.out.println(errorMsg);
            }
        }
        return n;
    }
}
