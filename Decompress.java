import java.io.*;
import java.util.*;

public class Decompress {

    public static void main(String[] args) {
        String compressedFilePath = null;
        FileInputStream inputFile = null;
        Scanner keyboard = new Scanner(System.in);
        boolean run = true;
        // verify user is passing a file as a command line argument and if they are not,
        // get filename from them.
        if (args.length > 0) {
            compressedFilePath = args[0];
        } else {
            System.out.println("Failed to find file. Please enter a valid filename: ");
            compressedFilePath = keyboard.nextLine();
        }

        // process file if it can be found, otherwise prompt user to try entering
        // filename again/give user option to compress another file.
        while (run) {
            String decompressedFilePath = compressedFilePath.replace(".zzz", "");
            String logFilePath = compressedFilePath.replace(".zzz", ".log");

            try (DataInputStream input = new DataInputStream(new FileInputStream(compressedFilePath));
                    FileWriter output = new FileWriter(decompressedFilePath);
                    FileWriter logWriter = new FileWriter(logFilePath)) {

                long startTime = System.currentTimeMillis();

                Dictionary<Integer, String> dictionary = new Hashtable<>();
                List<String> decompressedOutput = new ArrayList<>();
                int listLength = dictionary.size();
                int doubled = 0;

                // Initialize the dictionary with single-character sequences
                for (int i = 0; i < 128; i++) {
                    dictionary.put(i, Character.toString((char) i));
                }

                int nextCode = 128;

                // Read the initial code and retrieve the corresponding sequence
                int currentCode = input.readInt();
                String currentSequence = dictionary.get(currentCode);
                decompressedOutput.add(currentSequence);

                // Continue reading codes and decoding the content
                while (input.available() > 0) {
                    int newCode = input.readInt();
                    String newSequence;

                    String sequence = dictionary.get(newCode);
                    if (sequence != null) {
                        newSequence = sequence;
                    } else {
                        newSequence = currentSequence + currentSequence.charAt(0);
                    }

                    decompressedOutput.add(newSequence);

                    // Add the new sequence to the dictionary
                    dictionary.put(nextCode++, currentSequence + newSequence.charAt(0));

                    if (dictionary.size() == listLength * 2) {
                        listLength = decompressedOutput.size();
                        doubled++;
                    }

                    // Update current sequence for the next iteration
                    currentSequence = newSequence;
                }

                // Write the decompressed output to the text file
                for (String sequence : decompressedOutput) {
                    output.write(sequence);
                }

                long endTime = System.currentTimeMillis();
                double durationInSeconds = (endTime - startTime) / 1000.0;

                // Log decompression details to the log file
                logWriter.write("Decompression for file " + compressedFilePath + "\n");
                logWriter.write("Decompression took " + durationInSeconds + " seconds.\n");
                logWriter.write("The table was doubled " + doubled + " times (perfect hash table).\n");

                System.out.println("Success! Log file saved to: " + logFilePath);
                System.out.println("Would you like to decompress another file? (Enter y/n): ");
                String userResponse = keyboard.nextLine();
                boolean userRunAgain = true;
                while (userRunAgain) {
                    if (userResponse.equalsIgnoreCase("y")) {
                        System.out.println("Enter the name of the file:");
                        compressedFilePath = keyboard.nextLine();
                        userRunAgain = false;
                    } else if (userResponse.equalsIgnoreCase("n")) {
                        System.out.println("File(s) succesfully decompressed!");
                        System.out.println("Goodbye!");
                        userRunAgain = false;
                        run = false;
                    } else {
                        System.out.println("--- Error with your response! Please enter y or n!---");
                        userResponse = keyboard.nextLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to find file. Please enter a valid filename: ");
                compressedFilePath = keyboard.nextLine();
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

}
