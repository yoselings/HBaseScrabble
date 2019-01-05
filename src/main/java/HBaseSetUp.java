import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  HBaseSetUp{
    Map<String,Integer> tournament = new HashMap<>();
    Map<String,Integer> game = new HashMap<>();
    Map<String,Integer> winner = new HashMap<>();
    Map<String,Integer> loser = new HashMap<>();
    List<byte[]> listCF = new ArrayList<>();
    public HBaseSetUp(){

        tournament.put("tourneyid",1);
        tournament.put("round",15);
        tournament.put("division",16);

        game.put("gameid",0);
        game.put("tie",2);
        game.put("date",17);
        game.put("lexicon",18);

        winner.put("winnerid",3);
        winner.put("winnername",4);
        winner.put("winnerscore",5);
        winner.put("winneroldrating",6);
        winner.put("winnernewrating",7);
        winner.put("winnerpos",8);

        loser.put("loserid",9);
        loser.put("losername",10);
        loser.put("loserscore",11);
        loser.put("loseroldrating",12);
        loser.put("losernewrating",13);
        loser.put("loserpos",14);

        listCF.add(Bytes.toBytes("Tournament"));
        listCF.add(Bytes.toBytes("Game"));
        listCF.add(Bytes.toBytes("Winner"));
        listCF.add(Bytes.toBytes("Loser"));

    }

}