import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {

       /* byte[] key = new byte[38];
        System.out.println("NUM BYTES "+Bytes.toBytes("1111"));
        System.arraycopy(Bytes.toBytes("1111"),0,key,
                0,"1111".length());

        System.out.println(key);
        System.out.println(new String(key));

        for (int i = 7; i < 38; i++){
            key[i] = (byte) - 255;
           // System.out.println("i :"+key[i]);
        }
        System.out.println(key);
        System.out.println(new String(key));

        System.out.println("---------------------------------------");

        byte[] key2 = new byte[38];

        System.arraycopy(Bytes.toBytes("1111"),0,key,
                7,"1111".length());

        for (int i = 7; i < 38; i++){
            key[i] = (byte)255;
        }
        System.out.println(key2);
        System.out.println(new String(key2));
*/


        String csvName = "scrabble_games.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader("/home/osboxes/Documents/HBaseProject/CSVS"+"/"+csvName))) {
            int lines = 1;
            br.readLine();
            while ((line = br.readLine()) != null && lines <= 275) {
                String[] columns = line.split(cvsSplitBy);
                System.out.println(lines+" : " + columns[1]);
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
