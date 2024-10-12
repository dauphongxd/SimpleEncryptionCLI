import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Driver {
    private Process loggerProcess;
    private Process encryptionProcess;
    private PrintStream loggerInput;
    private Scanner loggerOutput;
    private PrintStream encryptionInput;
    private Scanner encryptionOutput;
    private Set<String> history = new LinkedHashSet<>();
    private Scanner userInput = new Scanner(System.in);
    private String logFileName;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Driver <log_file_name>");
            return;
        }
        new Driver(args[0]).run();
    }

    public Driver(String logFileName) {
        this.logFileName = logFileName;
    }


    public void run() {
        try {
            startProcesses();
            log("START Logging Started.");

            while (true) {
                printMenu();
                String command = userInput.nextLine().trim().toLowerCase();

                switch (command) {
                    case "password":
                        handlePassword();
                        break;
                    case "encrypt":
                        handleEncryptDecrypt("ENCRYPT");
                        break;
                    case "decrypt":
                        handleEncryptDecrypt("DECRYPT");
                        break;
                    case "history":
                        showHistory();
                        break;
                    case "quit":
                        quit();
                        return;
                    default:
                        System.out.println("Invalid command. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startProcesses() throws IOException {
        loggerProcess = Runtime.getRuntime().exec("java Logger " + logFileName);
        encryptionProcess = Runtime.getRuntime().exec("java Encryption");

        loggerInput = new PrintStream(loggerProcess.getOutputStream());
        loggerOutput = new Scanner(loggerProcess.getInputStream());
        encryptionInput = new PrintStream(encryptionProcess.getOutputStream());
        encryptionOutput = new Scanner(encryptionProcess.getInputStream());
    }

    private void printMenu() {
        System.out.println("----------------------------------------------------------");
        System.out.println("\t\t\t   Menu");
        System.out.println("----------------------------------------------------------");
        System.out.println("   password - set the password for encryption/decryption");
        System.out.println("   encrypt  - encrypt a string");
        System.out.println("   decrypt  - decrypt a string");
        System.out.println("   history  - show history");
        System.out.println("   quit     - quit program");
        System.out.println("\n----------------------------------------------------------");
        System.out.print("Enter Command: ");
    }

    private void handlePassword() throws IOException {
        System.out.print("Would you like to use the history? (Y/N) ");
        String useHistory = userInput.nextLine().trim().toLowerCase();
        String password;

        if (useHistory.equals("y")) {
            password = getStringFromHistory();
            if (password == null) return;
        } else {
            System.out.print("Enter Password: ");
            password = userInput.nextLine().trim().toUpperCase();
            history.add(password); // Add the new password to history
        }

        log("SET_PASSWORD Setting passkey.");
        sendToEncryption("PASSKEY " + password);
        String response = encryptionOutput.nextLine();
        if (response.equals("RESULT Passkey set")) {
            log("SET_PASSWORD Success.");
            System.out.println("Password set successfully.");
        } else {
            log("SET_PASSWORD Error: " + response);
            System.out.println("Error setting password: " + response);
        }
        System.out.println();
    }

    private void handleEncryptDecrypt(String operation) throws IOException {
        System.out.print("Would you like to use the history? (Y/N) ");
        String useHistory = userInput.nextLine().trim().toLowerCase();
        String input;

        if (useHistory.equals("y")) {
            input = getStringFromHistory();
            if (input == null) return;
        } else {
            System.out.print("Enter String to " + operation + ": ");
            input = userInput.nextLine().trim();
        }

        log(operation + " " + input + ".");
        sendToEncryption(operation + " " + input);
        String response = encryptionOutput.nextLine();

        if (response.startsWith("RESULT")) {
            String result = response.substring(7);
            System.out.println("\nResult: " + result);
            history.add(input.toUpperCase()); // Convert to uppercase before adding
            history.add(result.toUpperCase()); // Convert to uppercase before adding
            log(operation + " Success: " + result + ".");
        } else {
            System.out.println("\n!!ERROR: " + response.substring(6));
            log(operation + " Error: " + response.substring(6));
        }

        System.out.println("\nHit Enter to continue.");
        userInput.nextLine();

    }

    private String getStringFromHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty.");
            return null;
        }

        System.out.println("\n----------------------------------------------------------");
        System.out.println("\t\t\t   History");
        System.out.println("----------------------------------------------------------\n");
        List<String> historyList = new ArrayList<>(history);
        for (int i = 0; i < historyList.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + historyList.get(i));
        }
        System.out.println("   " + (historyList.size() + 1) + ". (Go Back)");
        System.out.println("\n----------------------------------------------------------");
        System.out.print("Select String: ");
        int choice = Integer.parseInt(userInput.nextLine().trim());

        if (choice == historyList.size() + 1) {
            return null;
        }
        if (choice < 1 || choice > historyList.size()) {
            System.out.println("Invalid choice.");
            return null;
        }
        return historyList.get(choice - 1);
    }

    private void showHistory() throws IOException {
        System.out.println("\n----------------------------------------------------------");
        System.out.println("\t\t\t   History");
        System.out.println("----------------------------------------------------------\n");
        int i = 1;
        for (String item : history) {
            System.out.println("   " + i + ". " + item);
            i++;
        }
        System.out.println("\n----------------------------------------------------------");
        System.out.println("Hit enter to continue.");
        userInput.nextLine();
        log("HISTORY History Checked.");
    }

    private void quit() throws IOException {
        log("STOPPED Logging Stopped.");
        sendToEncryption("QUIT");
        sendToLogger("QUIT");
        System.out.println("Exiting program.");

        // Wait a moment to ensure the log message is processed
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loggerProcess.destroy();
        encryptionProcess.destroy();
    }

    private void sendToEncryption(String message) {
        encryptionInput.println(message);
        encryptionInput.flush();
    }

    private void sendToLogger(String message) {
        loggerInput.println(message);
        loggerInput.flush();
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String[] parts = message.split(" ", 2);
        String logType = parts[0];
        String logMessage = parts.length > 1 ? parts[1] : "";
        sendToLogger(timestamp + " [" + logType + "] " + logMessage);
    }
}
