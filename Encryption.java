import java.util.Scanner;

public class Encryption {
    private String passkey = null;

    public static void main(String[] args) {
        Encryption program = new Encryption();
        program.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toUpperCase();
            String argument = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "PASSKEY":
                    setPasskey(argument);
                    break;
                case "ENCRYPT":
                    encrypt(argument);
                    break;
                case "DECRYPT":
                    decrypt(argument);
                    break;
                case "QUIT":
                    System.out.println("RESULT Exiting program");
                    return;
                default:
                    System.out.println("ERROR Unknown command");
            }
            System.out.flush();
        }
    }

    private void setPasskey(String key) {
        this.passkey = key.toUpperCase();
        System.out.println("RESULT");
    }

    private void encrypt(String text) {
        if (passkey == null) {
            System.out.println("ERROR Passkey not set");
            return;
        }
        System.out.println("RESULT " + cipherText(text.toUpperCase(), generateKey(text, passkey)));
    }

    private void decrypt(String text) {
        if (passkey == null) {
            System.out.println("ERROR Password not set");
            return;
        }
        System.out.println("RESULT " + originalText(text.toUpperCase(), generateKey(text, passkey)));
    }

    private String generateKey(String str, String key) {
        StringBuilder generatedKey = new StringBuilder(key);
        while (generatedKey.length() < str.length()) {
            generatedKey.append(key);
        }
        return generatedKey.substring(0, str.length());
    }

    private String cipherText(String str, String key) {
        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                int x = (str.charAt(i) + key.charAt(i) - 2 * 'A') % 26;
                x += 'A';
                cipherText.append((char) x);
            } else {
                cipherText.append(str.charAt(i));
            }
        }
        return cipherText.toString();
    }

    private String originalText(String cipherText, String key) {
        StringBuilder origText = new StringBuilder();
        for (int i = 0; i < cipherText.length(); i++) {
            if (Character.isLetter(cipherText.charAt(i))) {
                int x = (cipherText.charAt(i) - key.charAt(i) + 26) % 26;
                x += 'A';
                origText.append((char) x);
            } else {
                origText.append(cipherText.charAt(i));
            }
        }
        return origText.toString();
    }
}
