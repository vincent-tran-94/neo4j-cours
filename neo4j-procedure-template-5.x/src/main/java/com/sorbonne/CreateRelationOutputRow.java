package com.sorbonne;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;
import java.util.*;

import java.util.stream.Stream;

public class CreateRelationOutputRow {

    @Context
    public Log log;

    @Context
    public GraphDatabaseService db;

    @Procedure(name = "nn.createrelationoutputrow", mode = Mode.WRITE)
    @Description("Creates a CONTAINS relationship between a Row node (outputsRow) and a Neuron (output)")
    public Stream<CreateResult> createRelationOutputRow(
            @Name("from_id") String from_id,
            @Name("to_id") String to_id,
            @Name("outputfeatureid") String outputfeatureid,
            @Name("value") double value)
    {
        try (Transaction tx = db.beginTx()) {
            // Exécuter la requête Cypher pour créer la relation
//            tx.execute("MATCH (n1:Row {id:"+ from_id +",type:'outputsRow'})" +
//                    "MATCH (n2:Neuron {id:"+ to_id + ",type:'output'})" +
//                    "CREATE (n1)-[:CONTAINS {{output:"+ value +",id:"+ outputfeatureid +"}]->(n2)");

            String query = "MATCH (n1:Row {id: $from_id ,type:'outputsRow'}})" +
                    "MATCH (n2:Neuron {id: $to_id ,type:'output'})" +
                    "CREATE (n1)-[:CONTAINS {{output: $value, id: $outputfeatureid}]->(n2));";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("from_id", from_id);
            parameters.put("to_id", to_id);
            parameters.put("value", value);
            parameters.put("outputfeatureid", outputfeatureid);
            tx.execute(query, parameters);
            tx.commit();

            return Stream.of(new CreateResult("ok"));
        } catch (Exception e) {
            log.error("Failed to create relationOutputrow: " + e.getMessage());
            return Stream.of(new CreateResult("ko"));
        }
    }

    public static class CreateResult {
        public final String result;

        public CreateResult(String result) {
            this.result = result;
        }
    }
}