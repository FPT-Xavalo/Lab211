/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.Event;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Soram
 */
public class EventManagement {

    private ArrayList<Event> events;
    Scanner scanner = new Scanner(System.in);

    public EventManagement() {
        events = new ArrayList<>();
        loadDataFromFile();
    }

    public void addEvent() {
        while (true) {
            System.out.println("Nhập thông tin sự kiện:");
            System.out.print("Mã sự kiện: ");
            String eventId = scanner.nextLine();

            String eventNumberPattern = "^[^\\s]{5,}$"; //check pattern at least 5 characters long and no space
            Pattern pattern = Pattern.compile(eventNumberPattern); //create pattern for event name
            String eventName;
            while (true) {
                System.out.print("Tên sự kiện: ");
                eventName = scanner.nextLine().trim();
                Matcher matcher = pattern.matcher(eventName); //check input is match with pattern
                if (!matcher.matches()) {
                    System.out.println("Tên sự kiện không đúng cú pháp. Vui lòng nhập lại (e.g., F1234).");
                } else {
                    break;
                }
            }
            LocalDate currentDate = LocalDate.now(); // take current date
            LocalDate parsedDate = null;

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //create date pattern
            String eventDate = null;
            while (parsedDate == null || parsedDate.isBefore(currentDate)) {
                System.out.print("Ngày tổ chức sự kiện (yyyy-MM-dd): ");
                eventDate = scanner.nextLine();

                try {
                    parsedDate = LocalDate.parse(eventDate, dateFormat); // parse date input to date pattern

                    if (!isValidDate(parsedDate) || parsedDate.isBefore(currentDate)) { //check valid date and pass date
                        System.out.println("Ngày không hợp lệ hoặc trong quá khứ. Vui lòng nhập lại!!!");
                        parsedDate = null;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Ngày không đúng cú pháp. Vui lòng nhập lại!!!");
                }
            }
            System.out.print("Địa điểm sự kiện: ");
            String eventLocation = scanner.nextLine();
            int numberOfAttendees;
            while (true) {
                System.out.print("Sô lượng người tham gia: ");
                numberOfAttendees = scanner.nextInt();
                scanner.nextLine(); //consuming the newline because reads only the integer value and leaves the newline character in the input buffer
                if (numberOfAttendees < 1) {
                    System.out.println("Phải có ít nhất một người tham gia");
                } else {
                    break;
                }
            }

            String status;
            while (true) {
                System.out.print("Trạng thái (Available/Not Available): ");
                status = scanner.nextLine();
                //checking status is same (Available/Not Available) or other
                if (!status.equalsIgnoreCase("Available") && !status.equalsIgnoreCase("Not Available")) {
                    System.out.println("Không hợp lệ. Vui lòng nhập lại");
                } else {
                    break;
                }
            }

            Event event = new Event(eventId, eventName, eventDate, eventLocation, numberOfAttendees, status);
            events.add(event);//add new event to list
            System.out.println("Thêm sự kiện thành công!!!");
            System.out.println("Bạn có muốn thêm 1 sự kiện nữa không (Y/N)");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                // Continue adding another event
            } else if (choice.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 'Y' hoặc 'N'.");
            }
        }
    }

    public void findMatchingEventInfo(String place) {
        boolean foundMatchingEvent = false;

        for (Event event : events) {
            String matchEvent = event.getEventLocation();
            if (partialMatch(matchEvent, place)) {
                foundMatchingEvent = true;
                break;
            }
        }

        if (!foundMatchingEvent) {
            System.out.println("\nKhông có sự kiện nào tại địa điểm này.\n");
            return;
        }

        Collections.sort(events, Comparator.comparing(Event::getEventId).reversed());

        System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
        System.out.printf("| %-13s | %-20s | %-20s | %-20s | %-20s | %-16s |%n",
                "Mã sự kiện", "Tên sự kiện", "Ngày diễn ra", "Nơi tổ chức", "Số người tham gia", "Trạng thái");
        System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");

        for (Event event : events) {
            String matchEvent = event.getEventLocation();
            if (partialMatch(matchEvent, place)) {
                System.out.printf("| %-13s | %-20s | %-20s | %-20s | %-20d | %-16s |%n",
                        event.getEventId(), event.getEventName(), event.getEventDate(), event.getEventLocation(), event.getNumberOfAttendees(), event.getStatus());
            }
        }

        System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
    }

    private boolean partialMatch(String str1, String str2) {
        return str1.toLowerCase().contains(str2.toLowerCase()) || str2.toLowerCase().contains(str1.toLowerCase()) || str1.toUpperCase().contains(str2.toLowerCase()) || str1.toLowerCase().contains(str2.toUpperCase()) || str2.toLowerCase().contains(str1.toUpperCase()) || str2.toUpperCase().contains(str1.toLowerCase()) || str2.toUpperCase().contains(str1.toUpperCase());
    }

    public void printEventFromFile() {
        ArrayList<Event> existingEvents = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("src/Output/event.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String eventId = parts[0];
                    if (existingEvents.contains(eventId)) {
                        continue;
                    }
                    String eventName = parts[1];
                    String eventDate = parts[2];
                    String eventLocation = parts[3];
                    int numberOfAttendees = Integer.parseInt(parts[4]);
                    String status = parts[5];
                    Event event = new Event(eventId, eventName, eventDate, eventLocation, numberOfAttendees, status);
                    existingEvents.add(event);
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (existingEvents.isEmpty()) {
            System.out.println("\nKhông có sự kiện nào tại địa điểm này.\n");
        } else {
            Collections.sort(existingEvents, Comparator.comparing(Event::getEventId).reversed());
            System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
            System.out.printf("| %-13s | %-20s | %-20s | %-20s | %-20s | %-16s |%n",
                    "Mã sự kiện", "Tên sự kiện", "Ngày diễn ra", "Nơi tổ chức", "Số người tham gia", "Trạng thái");

            for (Event event : existingEvents) {
                System.out.printf("| %-13s | %-20s | %-20s | %-20s | %-20d | %-16s |%n",
                        event.getEventId(), event.getEventName(), event.getEventDate(), event.getEventLocation(), event.getNumberOfAttendees(), event.getStatus());
            }
            System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
        }
    }

    public void printEventFromList() {

        if (events.isEmpty()) {
            System.out.println("\nKhông có sự kiện nào tại địa điểm này.\n");
        } else {
            Collections.sort(events, Comparator.comparing(Event::getEventId).reversed());
            System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
            System.out.printf("| %-13s | %-20s | %-20s | %-20s | %-20s | %-16s |%n",
                    "Mã sự kiện", "Tên sự kiện", "Ngày diễn ra", "Nơi tổ chức", "Số người tham gia", "Trạng thái");
            System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
            for (Event event : events) {
                System.out.printf("| %-13s | %-20s | %-20s | %-20s | %-20d | %-16s |%n",
                        event.getEventId(), event.getEventName(), event.getEventDate(), event.getEventLocation(), event.getNumberOfAttendees(), event.getStatus());
            }
            System.out.println("+---------------+----------------------+----------------------+----------------------+----------------------+------------------+");
        }
    }

    public void eventExitsInFile(String eventID) {

        ArrayList<Event> existingEvents = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("src/Output/event.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String eventId = parts[0];

                    if (existingEvents.contains(eventId)) {
                        continue;
                    }

                    String eventName = parts[1];
                    String eventDate = parts[2];
                    String eventLocation = parts[3];
                    int numberOfAttendees = Integer.parseInt(parts[4]);
                    String status = parts[5];

                    Event event = new Event(eventId, eventName, eventDate, eventLocation, numberOfAttendees, status);

                    existingEvents.add(event);
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean eventExists = false;
        for (Event event : existingEvents) {
            if (event.getEventId().equals(eventID)) {
                eventExists = true;
                break;
            }
        }

        if (eventExists) {
            System.out.println("Event exists");
        } else {
            System.out.println("No Event Found!");
        }

    }

    private Event getEventById(String eventIdInput) {
        for (Event event : events) {
            if (event.getEventId().equals(eventIdInput)) {
                return event;
            }
        }
        return null;
    }

    public void updateEvent(Scanner scanner) {
        printEventFromList();
        while (true) {
            System.out.print("Nhập mã sự kiện để cập nhật: ");
            String eventId = scanner.nextLine();
            Event foundEvent = getEventById(eventId);
            if (foundEvent == null) {
                System.out.println("Mã sự kiện không tồn tại");
                return;
            }
            System.out.println("Enter new product information (leave blank to keep current value):");

            String eventNumberPattern = "^[^\\s]{5,}$"; // Pattern to check at least 5 characters long and no space
            Pattern pattern = Pattern.compile(eventNumberPattern); // Create pattern for event name
            String newEventName;
            while (true) {
                System.out.print("Tên sự kiện: ");
                newEventName = scanner.nextLine();
                Matcher matcher = pattern.matcher(newEventName); // Check input matches pattern
                if (newEventName.isEmpty()) {
                    break;
                } else if (!newEventName.isEmpty()) {
                    foundEvent.setEventName(newEventName);
                    break;
                } else if (!matcher.matches()) {
                    System.out.println("Tên sự kiện không đúng cú pháp. Vui lòng nhập lại (e.g., F1234).");
                }
            }

            LocalDate currentDate = LocalDate.now(); // take current date
            LocalDate parsedDate = null;

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // create date pattern
            String newEventDate = null;
            while (parsedDate == null || !isValidDate(parsedDate) || parsedDate.isBefore(currentDate)) {
                System.out.print("Ngày tổ chức sự kiện (yyyy-MM-dd): ");
                newEventDate = scanner.nextLine();
                if (newEventDate.isEmpty()) {
                    break;
                }
                try {
                    parsedDate = LocalDate.parse(newEventDate, dateFormat); // parse date input to date pattern
                    if (!newEventDate.isEmpty()) {
                        if (isValidDate(parsedDate) && !parsedDate.isBefore(currentDate)) {
                            foundEvent.setEventDate(newEventDate);
                            break;
                        } else {
                            System.out.println("Ngày không hợp lệ hoặc trong quá khứ. Vui lòng nhập lại!!!");
                            parsedDate = null;
                        }
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Ngày không đúng cú pháp. Vui lòng nhập lại!!!");
                }
            }

            System.out.print("Địa điểm sự kiện: ");
            String newEventLocation = scanner.nextLine();
            if (!newEventLocation.isEmpty()) {
                foundEvent.setEventLocation(newEventLocation);
            }
            int newNumberOfAttendees;
            while (true) {
                System.out.print("Số lượng người tham gia: ");
                String input = scanner.nextLine().trim(); // Read the input as a string
                if (input.isEmpty()) {
                    // If the input is empty, keep the current value stable
                    newNumberOfAttendees = foundEvent.getNumberOfAttendees();
                    break;
                }

                try {
                    newNumberOfAttendees = Integer.parseInt(input);
                    if (newNumberOfAttendees < 1) {
                        System.out.println("Phải có ít nhất một người tham gia");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Số người tham gia không hợp lệ. Vui lòng nhập lại.");
                }
            }

            // Update number of attendees if a valid value is provided and it's different from the current value
            if (newNumberOfAttendees != foundEvent.getNumberOfAttendees()) {
                foundEvent.setNumberOfAttendees(newNumberOfAttendees);
            }

            String newStatus;
            while (true) {
                System.out.print("Trạng thái (Available/Not Available): ");
                newStatus = scanner.nextLine();
                //checking status is same (Available/Not Available) or other
                if (!newStatus.equalsIgnoreCase("Available") && !newStatus.equalsIgnoreCase("Not Available")) {
                    System.out.println("Không hợp lệ. Vui lòng nhập lại");
                } else {
                    break;
                }
            }
            if (!newStatus.isEmpty()) {
                foundEvent.setStatus(newStatus);
            }

            System.out.println("Cập nhật sự kiện thành công!!!");
            printEventFromList();
            saveDataToFile();
            System.out.println("Bạn có muốn cập nhật sự kiện nữa không (Y/N)");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                // Continue adding another event
            } else if (choice.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 'Y' hoặc 'N'.");
            }
        }
        saveDataToFile();
    }

    public void deleteEvent(Scanner scanner) {
        printEventFromList();

        while (true) {
            System.out.print("Enter event id to delete: ");
            String eventId = scanner.nextLine();
            Event foundEvent = getEventById(eventId);

            if (foundEvent == null) {
                System.out.println("Event does not exist.");
                return;
            }

            System.out.println("Bạn có chắc chắn xóa sự kiện không (Y/N)");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                events.remove(foundEvent);
                saveDataToFile();
                System.out.println("Event deleted successfully.");
                printEventFromList();
            } else if (!choice.equalsIgnoreCase("n")) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn 'Y' hoặc 'N'.");
            }

            System.out.println("Bạn có muốn xóa sự kiện nữa không (Y/N)");
            choice = scanner.nextLine();

            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    public void loadDataFromFile() {
        try {
            Set<String> existingEvent = new HashSet<>();
            FileReader fileReader = new FileReader("src/Output/event.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String eventId = parts[0];
                    if (existingEvent.contains(eventId)) {
                        continue;
                    }
                    String eventName = parts[1];
                    String eventDate = parts[2];
                    String eventLocation = parts[3];
                    int numberOfAttendees = Integer.parseInt(parts[4]);
                    String status = parts[5];
                    Event event = new Event(eventId, eventName, eventDate, eventLocation, numberOfAttendees, status);
                    boolean exists = false;
                    for (Event eventCheck : events) {
                        if (eventCheck.getEventId().equalsIgnoreCase(eventId)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        events.add(event);
                        existingEvent.add(eventId);
                    }
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDataToFile() {
        try {
            FileWriter eventFile = new FileWriter("src//Output//event.txt");
            for (Event event : events) {
                eventFile.write(event.toString() + "\n");
            }
            eventFile.close();
            System.out.println("Dữ liệu đã được lưu vào các tệp txt.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidDate(LocalDate date) {
        LocalDate minDate = LocalDate.of(1903, 1, 1);
        LocalDate maxDate = LocalDate.now().plusYears(2000);
        return !date.isBefore(minDate) && !date.isAfter(maxDate);
    }

}
