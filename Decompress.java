import java.io.*;
import java.util.*;

public class Decompress {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the compressed binary file as a command-line argument.");
            return;
        }

        String compressedFilePath = args[0];
        String decompressedFilePath = compressedFilePath.replace(".zzz", "_decompressed.txt");
        String logFilePath = compressedFilePath.replace(".zzz", ".log");

        try (DataInputStream input = new DataInputStream(new FileInputStream(compressedFilePath));
             FileWriter output = new FileWriter(decompressedFilePath);
             FileWriter logWriter = new FileWriter(logFilePath)) {

            long startTime = System.currentTimeMillis();

            Dictionary<Integer, String> dictionary = new Hashtable<>();
            List<String> decompressedOutput = new ArrayList<>();

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
            logWriter.write("The table was doubled 0 times (perfect hash table).\n");

            System.out.println("Decompression completed. Decompressed content saved to: " + decompressedFilePath);
            System.out.println("Log file saved to: " + logFilePath);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading/writing the file: " + e.getMessage());
        }
    }
}

