package com.sorbonne;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;
import java.util.*;

import java.util.stream.Stream;

public class CreateConnection {

    @Context
    public Log log;

    @Context
    public GraphDatabaseService db;

    @Procedure(name = "nn.createConnection", mode = Mode.WRITE)
    @Description("Creates a CONNECTED_TO relationship between two neurons with a specified weight.")
    public Stream<CreateResult> createConnection(
            @Name("from_id") String from_id,
            @Name("to_id") String to_id,
            @Name("weight") double weight) {

        try (Transaction tx = db.beginTx()) {
            // Exécuter la requête Cypher pour créer la relation
//            tx.execute("MATCH (n1:Neuron {id:"+ from_id +"})" +
//                            "MATCH (n2:Neuron {id:"+ to_id + "})" +
//                            "CREATE (n1)-[:CONNECTED_TO {weight:"+ weight +"}]->(n2)");

            String query = "MATCH (n1:Neuron {id: $from_id })" +
                    "MATCH (n2:Neuron {id: $to_id })" +
                    "CREATE (n1)-[:CONNECTED_TO {weight: $weight }]->(n2)";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("from_id", from_id);
            parameters.put("to_id", to_id);
            parameters.put("weight", weight);
            tx.execute(query, parameters);
            tx.commit();

            return Stream.of(new CreateResult("ok"));
        } catch (Exception e) {
            log.error("Failed to create connection: " + e.getMessage());
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