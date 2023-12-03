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

            HashTableChain<String, Integer> charTable = new HashTableChain<>();

            for (int asciiValue = 0; asciiValue <= 127; asciiValue++) {
                String ch = Character.toString((char) asciiValue);
                charTable.put(ch, asciiValue);
            }

            int nextCode = 128;
            int character;
            String temp = "";
            while ((character = reader.read()) != -1) {
                char c = (char) character;
                String tempPlusC = temp + c;

                if (charTable.containsKey(tempPlusC)) {
                    temp = tempPlusC;
                } else {
                    Integer code = charTable.get(temp);
                    if (code != null) {
                        output.writeInt(code.intValue());
                        charTable.put(tempPlusC, nextCode++);
                        System.out.println(tempPlusC + " " + charTable.get(tempPlusC));
                        temp = Character.toString(c);
                    }else{
                        continue;
                    }
                }
            }

            // Handle the last character(s)
            if (!temp.isEmpty()) {
                Integer code = charTable.get(temp);
                if (code != null) {
                    output.writeInt(code);
                }
            }

            long endTime = System.currentTimeMillis();
            double durationInSeconds = (endTime - startTime) / 1000.0;

            // Log compression details to the log file
            logWriter.write("Compression of " + file + "\n");
            logWriter.write("Compressed from " + getFileSize(file) + " to " + getFileSize(BinaryFileName) + "\n");
            logWriter.write("Compression took " + durationInSeconds + " seconds\n");
            logWriter.write("The dictionary contains " + charTable.size() + " total entries\n");
            logWriter.write("The table was rehashed " + charTable.numRehashes() + " times\n");

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
