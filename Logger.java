import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Logger {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Logger <log_file_name>");
            return;
        }

        String logFileName = args[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter log messages (type 'QUIT' to exit):");

            while (true) {
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("QUIT")) {
                    break;
                }

                int firstSpaceIndex = input.indexOf(' ');
                if (firstSpaceIndex == -1) {
                    System.out.println("Invalid input format. Please use 'ACTION MESSAGE'");
                    continue;
                }

                String action = input.substring(0, firstSpaceIndex);
                String message = input.substring(firstSpaceIndex + 1);

                LocalDateTime now = LocalDateTime.now();
                String timestamp = now.format(formatter);

                String logEntry = String.format("%s [%s] %s", timestamp, action, message);
                writer.write(logEntry);
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
