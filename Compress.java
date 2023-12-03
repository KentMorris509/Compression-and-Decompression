import java.util.*;
import java.io.*;

public class Compress{

    public static void main(String[] args){
        String file;
        String BinaryFileName;
        boolean validFile = false;
        while(!validFile){
            System.out.print("Enter a file to compress: ");
            file = System.console().readLine();
            BinaryFileName = file + ".zzz";

            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                DataOutputStream output = new DataOutputStream(new FileOutputStream(BinaryFileName));

                HashTableChain<String, Integer> charTable = new HashTableChain<>();
                    
                for (int asciiValue = 0; asciiValue <= 127; asciiValue++){
                    String ch = Character.toString((char) asciiValue);
                    charTable.put(ch,asciiValue);
                }

                int nextCode = 127;
                int character;
                String temp = "";
                while((character = reader.read()) != -1){
                    char c = (char) character;
                    temp += c;
                    if (!charTable.containsKey(temp.toString())){
                        output.writeInt(charTable.get(temp.substring(0,temp.length() - 1)));
                        charTable.put(temp.toString(),nextCode++);
                        temp = "";
                        temp += c;
                    }
                }

                if (temp.length() > 0){
                    output.writeInt(charTable.get(temp.toString()));
                }
                validFile = true;
            }catch(FileNotFoundException e){
                System.out.println("File was not found please try again!");

            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}