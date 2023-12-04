import java.io.*;

public class BinaryFileReader {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the binary file as a command-line argument.");
            return;
        }

        String binaryFilePath = args[0];

        try (DataInputStream input = new DataInputStream(new FileInputStream(binaryFilePath))) {

            while (input.available() > 0) {
                int code = input.readInt();
                System.out.print(code);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
