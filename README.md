# Run hbase with docker

1. Download docker image and run the distributed version

    git clone https://github.com/big-data-europe/docker-hbase
    docker-compose -f docker-compose-distributed-local.yml up

2. Add hosts mapping in hosts file

    127.0.0.1 hbase-master hbase-region localhost

3. Add ports mapping in docker compose file for master node

    16000:16000
    16020:16020

4. Chec if hbase is running

Master at http://localhost:16010/master-status
Zookeeper avaialble at port 2181

# Run HBaseScrabble app

    .\bin\HBaseScrabble localhost:2181 createTable
    .\bin\HBaseScrabble localhost:2181 loadTable C:\Users\Filip\Desktop\HBaseProjectScrabble\HBaseProject\CSVS
    .\bin\HBaseScrabble localhost:2181 query1 3 “Paul Avrin”
    .\bin\HBaseScrabble localhost:2181 query2 100 150
    .\bin\HBaseScrabble localhost:2181 query3 235

# Use hbase shell

    docker container exec -it hbase-master hbase shell
    count 'ScrabbleGames', INTERVAL=>100000, CACHE=>1000
