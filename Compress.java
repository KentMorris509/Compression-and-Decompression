import java.io.*;
import java.util.*;

public class Compress {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a file path as a command-line argument.");
            return;
        }

        String file = args[0];
        String BinaryFileName = file + ".zzz";
        String LogFileName = file + ".zzz.log";

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
                DataOutputStream output = new DataOutputStream(new FileOutputStream(BinaryFileName));
                FileWriter logWriter = new FileWriter(LogFileName)) {

            long startTime = System.currentTimeMillis();

            HashTableChain<String, Integer> HashTable = new HashTableChain<>();
            List<Integer> compressedOutput = new ArrayList<>();
            StringBuilder currentSequence = new StringBuilder();

            // Initialize the HashTableChain with single-character sequences
            for (int i = 0; i <128; i++) {
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
            logWriter.write("Compressed from " + getFileSize(file) + " to " + getFileSize(BinaryFileName) + "\n");
            logWriter.write("Compression took " + durationInSeconds + " seconds\n");
            logWriter.write("The HashTableChain contains " + HashTable.size() + " total entries\n");  
            logWriter.write("The table was rehashed " + HashTable.numRehashes() + " times\n");  

        } catch (FileNotFoundException e) {
            System.out.println("File was not found. Please try again!");
        } catch (IOException e) {
            System.out.println("An IOException occurred: " + e.getMessage());
        }
    }

    private static String getFileSize(String filePath) {
        File file = new File(filePath);
        long sizeInBytes = file.length();
        double sizeInKB = sizeInBytes / 1024.0;
        return String.format("%.2f Kilobytes", sizeInKB);
    }
}
