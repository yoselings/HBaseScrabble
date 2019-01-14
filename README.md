# Running hbase with docker

    git clone https://github.com/big-data-europe/docker-hbase
    docker-compose -f docker-compose-distributed-local.yml up

add hosts mapping:

    127.0.0.1 hbase-master hbase-region localhost

add ports mapping in docker compose file for master

    16000:16000

add ports mapping in docker compose file for master

    16020:16020

Master at http://localhost:16010/master-status
Zookeeper at 2181

# Running app

    .\bin\HBaseScrabble localhost:2181 createTable
    .\bin\HBaseScrabble localhost:2181 loadTable C:\Users\Filip\Desktop\HBaseProjectScrabble\HBaseProject\CSVS
    .\bin\HBaseScrabble localhost:2181 query1 3 “Paul Avrin”
    .\bin\HBaseScrabble localhost:2181 query2 100 150
    .\bin\HBaseScrabble localhost:2181 query3 235

# hbase shell

    docker container exec -it hbase-master hbase shell
    count 'ScrabbleGames', INTERVAL=>100000, CACHE=>1000