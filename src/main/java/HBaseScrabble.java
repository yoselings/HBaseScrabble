import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.*;


public class HBaseScrabble {
    private Configuration config;
    private HBaseAdmin hBaseAdmin;
    private byte[] table = Bytes.toBytes("ScrabbleGames");
    private HBaseSetUp setUp = new HBaseSetUp();

    /**
     * The Constructor. Establishes the connection with HBase.
     * @param zkHost
     * @throws IOException
     */
    public HBaseScrabble(String zkHost) throws IOException {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zkHost.split(":")[0]);
        config.set("hbase.zookeeper.property.clientPort", zkHost.split(":")[1]);
        HBaseConfiguration.addHbaseResources(config);
        this.hBaseAdmin = new HBaseAdmin(config);
    }

    public void createTable() throws IOException {

        HTableDescriptor hTable = new HTableDescriptor(TableName.valueOf(table));
        for (byte[] cf: setUp.listCF) {
            hTable.addFamily(new HColumnDescriptor(cf).setMaxVersions(10));
        }
        this.hBaseAdmin.createTable(hTable);
    }

    public void loadTable(String folder)throws IOException{
        HTable hTable = new HTable(config,table);
        String csvName = "scrabble_games.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(folder+"/"+csvName))) {
            int lines=0;


            while ((line = br.readLine()) != null) {
               //while (br.readLine()!= null) {
                String[] columns = line.split(cvsSplitBy);
                String[] values ={columns[0],columns[1],columns[3],columns[9]};
                int[] keyTable = {0,1,2,3}; //{0,10,20,26}; //10,10,6,6

                Put p = new Put(getKey(values,keyTable));

 //               System.out.println("KEY - "+new String(getKey(values,keyTable)));
                int i=0;
                for (byte[] columnFamily: setUp.listCF) {
                    Map<String, Integer> currentMap = new HashMap<>();
                    switch (i){
                    //Tournament Game Winnes Loser
                        case 0:
                            currentMap = setUp.tournament;
                            break;
                        case 1:
                            currentMap = setUp.game;
                            break;
                        case 2:
                            currentMap = setUp.winner;
                            break;
                        case 3:
                            currentMap = setUp.loser;
                            break;
                    }

                    for (Map.Entry<String, Integer> entry : currentMap.entrySet())
                    {
//                        System.out.println("KEY " + entry.getKey());
//                        System.out.println("VAL " + columns[entry.getValue()]);
                        p.add(columnFamily, Bytes.toBytes(entry.getKey()), Bytes.toBytes(columns[entry.getValue()]));
//                        System.out.println(entry.getKey() + "/" + entry.getValue());
                    }
                    i++;
                }

                hTable.put(p);

               lines++;
                //hBaseAdmin.split(table);
            }
            System.out.println("Lines "+lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
       /* catch(InterruptedException ex){
            ex.printStackTrace();
        }*/

    }

    /**
     * This method generates the key
     * @param values The value of each column
     * @param keyTable The position of each value that is required to create the key in the array of values.
     * @return The encoded key to be inserted in HBase
     */
    private byte[] getKey(String[] values, int[] keyTable) {
        String keyString = "";
        for (int keyId : keyTable){
            keyString += values[keyId];
        }
        byte[] key = Bytes.toBytes(keyString);

        return key;
    }



    public List<String> query1(String tourneyid, String winnername) throws IOException {
        //TO IMPLEMENT
        System.exit(-1);
        return null;

    }

    public List<String> query2(String firsttourneyid, String lasttourneyid) throws IOException {
        //TO IMPLEMENT
        System.exit(-1);
        return null;
    }

    public List<String> query3(String tourneyid) throws IOException {
        //TO IMPLEMENT
        System.exit(-1);
        return null;
    }


    public static void main(String[] args) throws IOException {
        if(args.length<2){
            System.out.println("Error: \n1)ZK_HOST:ZK_PORT, \n2)action [createTable, loadTable, query1, query2, query3], \n3)Extra parameters for loadTables and queries:\n" +
                    "\ta) If loadTable: csvsFolder.\n " +
                    "\tb) If query1: tourneyid winnername.\n  " +
                    "\tc) If query2: firsttourneyid lasttourneyid.\n  " +
                    "\td) If query3: tourneyid.\n  ");
            System.exit(-1);
        }
        HBaseScrabble hBaseScrabble = new HBaseScrabble(args[0]);
        if(args[1].toUpperCase().equals("CREATETABLE")){
            hBaseScrabble.createTable();
        }
        else if(args[1].toUpperCase().equals("LOADTABLE")){

            if(args.length!=3){
                System.out.println("Error: 1) ZK_HOST:ZK_PORT, 2)action [createTables, loadTables], 3)csvsFolder");
                System.exit(-1);
            }
            else if(!(new File(args[2])).isDirectory()){
                System.out.println("Error: Folder "+args[2]+" does not exist.");
                System.exit(-2);
            }
            hBaseScrabble.loadTable(args[2]);
        }
        else if(args[1].toUpperCase().equals("QUERY1")){
            if(args.length!=4){
                System.out.println("Error: 1) ZK_HOST:ZK_PORT, 2)query1, " +
                        "3) tourneyid 4) winnername");
                System.exit(-1);
            }

            List<String> opponentsName = hBaseScrabble.query1(args[2], args[3]);
            System.out.println("There are "+opponentsName.size()+" opponents of winner "+args[3]+" that play in tourney "+args[2]+".");
            System.out.println("The list of opponents is: "+Arrays.toString(opponentsName.toArray(new String[opponentsName.size()])));
        }
        else if(args[1].toUpperCase().equals("QUERY2")){
            if(args.length!=4){
                System.out.println("Error: 1) ZK_HOST:ZK_PORT, 2)query2, " +
                        "3) firsttourneyid 4) lasttourneyid");
                System.exit(-1);
            }
            List<String> playerNames =hBaseScrabble.query2(args[2], args[3]);
            System.out.println("There are "+playerNames.size()+" players that participates in more than one tourney between tourneyid "+args[2]+" and tourneyid "+args[3]+" .");
            System.out.println("The list of players is: "+Arrays.toString(playerNames.toArray(new String[playerNames.size()])));
        }
        else if(args[1].toUpperCase().equals("QUERY3")){
            if(args.length!=3){
                System.out.println("Error: 1) ZK_HOST:ZK_PORT, 2) query3, " +
                        "3) tourneyid");
                System.exit(-1);
            }
            List<String> games = hBaseScrabble.query3(args[2]);
            System.out.println("There are "+games.size()+" that ends in tie in tourneyid "+args[2]+" .");
            System.out.println("The list of games is: "+Arrays.toString(games.toArray(new String[games.size()])));
        }
        else{
            System.out.println("Error: \n1)ZK_HOST:ZK_PORT, \n2)action [createTable, loadTable, query1, query2, query3], \n3)Extra parameters for loadTables and queries:\n" +
                    "\ta) If loadTable: csvsFolder.\n " +
                    "\tb) If query1: tourneyid winnername.\n  " +
                    "\tc) If query2: firsttourneyid lasttourneyid.\n  " +
                    "\td) If query3: tourneyid.\n  ");
            System.exit(-1);
        }

    }



}
