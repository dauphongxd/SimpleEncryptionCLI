# SimpleEncryptionCLI
This project implements a simple encryption/decryption program with logging capabilities.

## Files Included

1. Driver.java
   - Main class that handles user interaction and coordinates between Encryption and Logger.
   - Manages the program flow, user input, and history.

2. Encryption.java
   - Implements the encryption and decryption logic.
   - Handles setting and using the encryption key.

3. Logger.java
   - Manages logging of all program activities.
   - Writes log entries to a specified file.

4. README.md (this file)
   - Provides information about the project and how to run it.

## How to Compile and Run

1. Ensure you have Java Development Kit (JDK) installed on your system.

2. Open a terminal or command prompt and navigate to the directory containing the Java files.

3. Compile the Java files: javac Driver.java Logger.java Encryption.java

4. Run the program: java Driver logfile.txt (Replace `logfile.txt` with the desired name for your log file.)


## Usage

- The program will display a menu with the following options:
- password: Set encryption password
- encrypt: Encrypt a string
- decrypt: Decrypt a string
- history: Show command history
- quit: Exit the program

- Follow the on-screen prompts to use each feature.
- The program maintains a history of successful operations, which can be viewed or used for subsequent operations.



If you encounter any issues or have questions, please don't hesitate to reach out.
