/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Control.EventManagement;
import Model.Event;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Soram
 */
public class EventView {

    public static void main(String[] args) {
        EventManagement system = new EventManagement();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("+--------MENU QUẢN LÍ SỰ KIỆN-------+");
                System.out.println("|1. Thêm sự kiện mới                |");
                System.out.println("|2. Kiểm tra sự kiện đã có          |");
                System.out.println("|3. Tìm sự kiện bằng địa điểm       |");
                System.out.println("|4. Cập nhật hoặc xóa sự kiện       |");
                System.out.println("|5. Lưu file                        |");
                System.out.println("|6. In tất cả sự kiện trong file    |");
                System.out.println("|7. Thoát                           |");
                System.out.println("+-----------------------------------+");
                System.out.print("Nhập lựa chọn của bạn: ");
                int choice = scanner.nextInt();
                String choiceYN;
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        system.addEvent();
                        break;
                    case 2:
                        while (true) {
                            System.out.println("Vui lòng nhập event ID: ");
                            String eventID = scanner.nextLine();
                            system.eventExitsInFile(eventID);
                            System.out.println("Bạn có muốn tìm 1 sự kiện nữa không (Y/N)");
                            choiceYN = scanner.nextLine();
                            if (choiceYN.equalsIgnoreCase("y")) {
                                // Continue check another event
                            } else if (choiceYN.equalsIgnoreCase("n")) {
                                break;
                            } else {
                                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 'Y' hoặc 'N'.");
                            }
                        }
                        break;
                    case 3:
                        while (true) {
                            System.out.println("Vui lòng nhập địa điểm sự kiện: ");
                            String place = scanner.nextLine();
                            system.findMatchingEventInfo(place);
                            System.out.println("Bạn có muốn tìm 1 sự kiện nữa không (Y/N)");
                            choiceYN = scanner.nextLine();
                            if (choiceYN.equalsIgnoreCase("y")) {
                                // Continue adding another event
                            } else if (choiceYN.equalsIgnoreCase("n")) {
                                break;
                            } else {
                                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 'Y' hoặc 'N'.");
                            }
                        }
                        break;

                    case 4:
                        updateAndDeleteMenu(system);
                        break;
                    case 5:
                        system.saveDataToFile();
                        break;
                    case 6:
                        system.printEventFromFile();
                        break;
                    case 7:
                        System.out.println("Cảm ơn vì đã sử dụng dích vụ của chúng tôi.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine();
            }
        }
    }

    public static void updateAndDeleteMenu(EventManagement system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("+---------MENU XÓA VÀ SỬA--------+");
                System.out.println("|1. Cập nhật chi tiết sự kiện.   |");
                System.out.println("|2. Xóa sự kiện                  |");
                System.out.println("|3. Quay lại menu chính          |");
                System.out.println("+--------------------------------+");
                System.out.print("Nhập lựa chọn của bạn:");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        system.updateEvent(scanner);
                        break;
                    case 2:
                        system.deleteEvent(scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.Vui lòng nhập lại!!!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine();
            }
        }
    }
}
