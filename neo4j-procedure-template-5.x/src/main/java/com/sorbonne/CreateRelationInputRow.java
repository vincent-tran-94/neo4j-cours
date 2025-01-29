package com.sorbonne;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.stream.Stream;

public class CreateRelationInputRow {

    @Context
    public Log log;

    @Context
    public GraphDatabaseService db;

    @Procedure(name = "nn.createrelationinputrow", mode = Mode.WRITE)
    @Description("Creates a CONTAINS relationship between a Row node (inputsRow) and a Neuron (input)")
    public Stream<CreateResult> createRelationInputRow(
            @Name("from_id") String from_id,
            @Name("to_id") String to_id,
            @Name("inputfeatureid") String inputfeatureid,
            @Name("value") double value)
    {
        try (Transaction tx = db.beginTx()) {
            // Exécuter la requête Cypher pour créer la relation
            tx.execute("MATCH (n1:Row {id:"+ from_id +",type:'inputsRow'})" +
                    "MATCH (n2:Neuron {id:"+ to_id + ",type:'input'})" +
                    "CREATE (n1)-[:CONTAINS {{output:"+ value +",id:"+ inputfeatureid +"}]->(n2)");

            return Stream.of(new CreateResult("ok"));
        } catch (Exception e) {
            log.error("Failed to create relationinputrow: " + e.getMessage());
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