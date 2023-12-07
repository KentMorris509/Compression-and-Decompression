import java.io.*;
import java.util.*;

public class Compress {

    public static void main(String[] args) {
        String file = null;
        FileInputStream inputFile = null;
        Scanner keyboard = new Scanner(System.in);
        boolean run = true;
        // verify user is passing a file as a command line argument and if they are not,
        // get filename from them.
        if (args.length > 0) {
            file = args[0];
        } else {
            System.out.println("Failed to find file. Please enter a valid filename: ");
            file = keyboard.nextLine();
        }

        // process file if it can be found, otherwise prompt user to try entering
        // filename again/give user option to compress another file.
        while (run) {
            String BinaryFileName = file + ".zzz";
            String LogFileName = file + ".zzz.log";

            try (BufferedReader reader = new BufferedReader(new FileReader(file));
                    DataOutputStream output = new DataOutputStream(new FileOutputStream(BinaryFileName));
                    FileWriter logWriter = new FileWriter(LogFileName)) {

                long startTime = System.currentTimeMillis();

                int capacity = 101; // default
                double sizeInKB = getFileSize(file);

                if (sizeInKB / 12 > 101) {
                    capacity = (int) sizeInKB * 12;
                } else {
                    capacity = 101; // default
                }
                HashTableChain<String, Integer> HashTable = new HashTableChain<>(capacity);
                List<Integer> compressedOutput = new ArrayList<>();
                StringBuilder currentSequence = new StringBuilder();

                for (int i = 0; i < 128; i++) {
                    HashTable.put(Character.toString((char) i), i);
                }

                int nextCode = 128;
                int character;
                while ((character = reader.read()) != -1) {
                    char c = (char) character;
                    String currentSequencePlusC = currentSequence.toString() + c;

                    if (HashTable.containsKey(currentSequencePlusC)) {
                        currentSequence = new StringBuilder(currentSequencePlusC);
                    } else {
                        compressedOutput.add(HashTable.get(currentSequence.toString()));

                        HashTable.put(currentSequencePlusC, nextCode++);
                        currentSequence = new StringBuilder(Character.toString(c));
                    }
                }

                // Write the code for the last sequence to the compressed output
                compressedOutput.add(HashTable.get(currentSequence.toString()));

                // Write the compressed output to the file
                for (int code : compressedOutput) {
                    output.writeInt(code);

                }

                long endTime = System.currentTimeMillis();
                double durationInSeconds = (endTime - startTime) / 1000.0;

                // Log compression details to the log file
                logWriter.write("Compression of " + file + "\n");
                logWriter.write("Compressed from " + String.format("%.2f Kilobytes", getFileSize(file)) + " to "
                        + String.format("%.2f Kilobytes", getFileSize(BinaryFileName)) + "\n");
                logWriter.write("Compression took " + durationInSeconds + " seconds\n");
                logWriter.write("The HashTableChain contains " + HashTable.size() + " total entries\n");
                logWriter.write("The table was rehashed " + HashTable.numRehashes() + " times\n");

                System.out.println("Success!");
                System.out.println("Would you like to compress another file? (Enter y/n): ");
                String userResponse = keyboard.nextLine();
                boolean userRunAgain = true;
                while (userRunAgain) {
                    if (userResponse.equalsIgnoreCase("y")) {
                        System.out.println("Enter the name of the file:");
                        file = keyboard.nextLine();
                        userRunAgain = false;
                    } else if (userResponse.equalsIgnoreCase("n")) {
                        System.out.println("File(s) successfully compressed!");
                        System.out.println("Goodbye!");
                        userRunAgain = false;
                        run = false;
                        break;
                    } else {
                        System.out.println("--- Error with your response! Please enter y or n!---");
                        userResponse = keyboard.nextLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to find file. Please enter a valid filename: ");
                file = keyboard.nextLine();
            } finally {
                if (inputFile != null) {
                    try {
                        inputFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        keyboard.close();
    }

    private static double getFileSize(String filePath) {
        File file = new File(filePath);
        long sizeInBytes = file.length();
        double sizeInKB = sizeInBytes / 1024.0;
        return sizeInKB;
    }
}
