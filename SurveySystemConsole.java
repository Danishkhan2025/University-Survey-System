import java.io.*;
import java.util.*;

public class SurveySystemConsole {
    static final String ADMIN_PASSWORD = "danish";
    static final String RESPONSE_FILE = "responses.txt";

    static Scanner scanner = new Scanner(System.in);

    static Map<String, String[]> surveyCategories = new LinkedHashMap<>();
    static {
        surveyCategories.put("University Cafeteria", new String[]{
            "1. Are you satisfied with the cleanliness of the cafeteria?",
            "2. Are the food prices reasonable?",
            "3. Do you find food variety adequate?",
            "4. Is the staff courteous and helpful?",
            "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Transport", new String[]{
            "1. How punctual are the buses?",
            "2. Are the buses clean and well-maintained?",
            "3. Do you feel safe while using transport?",
            "4. Is the bus route convenient?",
            "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Library", new String[]{
            "1. Are the library resources adequate?",
            "2. Is the environment suitable for study?",
            "3. Is the staff helpful?",
            "4. Are you satisfied with the library timings?",
            "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Masjid", new String[]{
            "1. Is the Masjid clean and well-maintained?",
            "2. Are prayer times announced clearly?",
            "3. Is there enough space during Jummah prayers?",
            "4. Are you satisfied with the arrangements for Wudu?",
            "5. Suggestions for improvement:"
        });
        surveyCategories.put("University Faculty", new String[]{
            "1. Are the teachers punctual?",
            "2. Do teachers deliver lectures effectively?",
            "3. Is the faculty cooperative outside class?",
            "4. Are you satisfied with teacher evaluations?",
            "5. Suggestions for improvement:"
        });
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== COMSATS University Survey System ===");
            System.out.println("1. Admin Login");
            System.out.println("2. Participant - Take Survey");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    adminLogin();
                    break;
                case "2":
                    participantMenu();
                    break;
                case "3":
                    System.out.println("Thank you for using the system.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void adminLogin() {
        System.out.print("Enter Admin Password: ");
        String pass = scanner.nextLine();
        if (pass.equals(ADMIN_PASSWORD)) {
            adminPanel();
        } else {
            System.out.println("Incorrect password!");
        }
    }

    static void adminPanel() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View All Responses");
            System.out.println("2. Search by Name");
            System.out.println("3. Count Responses");
            System.out.println("4. Delete All Responses");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewResponses();
                    break;
                case "2":
                    searchByName();
                    break;
                case "3":
                    countResponses();
                    break;
                case "4":
                    deleteResponses();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void participantMenu() {
        System.out.println("\n*** Your opinion matters! ***");
        System.out.println("Help improve COMSATS by answering a short survey.");
        System.out.println("We value your time and feedback!\n");

        System.out.println("Select a survey category:");
        List<String> keys = new ArrayList<>(surveyCategories.keySet());
        for (int i = 0; i < keys.size(); i++) {
            System.out.println((i + 1) + ". " + keys.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > keys.size()) {
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {}
        }

        String category = keys.get(choice - 1);
        String[] questions = surveyCategories.get(category);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        String[] answers = new String[questions.length];
        for (int i = 0; i < questions.length; i++) {
            System.out.println(questions[i]);
            answers[i] = scanner.nextLine();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(RESPONSE_FILE, true))) {
            pw.println("Name: " + name);
            pw.println("Survey Category: " + category);
            for (int i = 0; i < questions.length; i++) {
                pw.println(questions[i] + " " + answers[i]);
            }
            pw.println("------------------------------------------------");
            System.out.println("\nThank you for participating in the survey, " + name + "!");
        } catch (IOException e) {
            System.out.println("Error saving responses.");
        }
    }

    static void viewResponses() {
        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSE_FILE))) {
            String line;
            boolean empty = true;
            System.out.println("\n--- All Responses ---");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                empty = false;
            }
            if (empty) System.out.println("No responses found.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    static void searchByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().toLowerCase();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("name: " + name)) {
                    found = true;
                    System.out.println(line);
                    for (int i = 0; i < 7; i++) {
                        if ((line = br.readLine()) != null)
                            System.out.println(line);
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("Error searching file.");
        }

        if (!found) {
            System.out.println("No responses found for that name.");
        }
    }

    static void countResponses() {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(RESPONSE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Name:"))
                    count++;
            }
        } catch (IOException e) {
            System.out.println("Error counting responses.");
            return;
        }
        System.out.println("Total Responses: " + count);
    }

    static void deleteResponses() {
        System.out.print("Are you sure you want to delete all responses? (yes/no): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            try {
                new PrintWriter(RESPONSE_FILE).close();
                System.out.println("All responses deleted.");
            } catch (IOException e) {
                System.out.println("Error deleting responses.");
            }
        } else {
            System.out.println("Delete canceled.");
        }
    }
}
