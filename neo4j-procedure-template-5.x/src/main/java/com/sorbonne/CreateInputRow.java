package com.sorbonne;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.stream.Stream;

public class CreateInputRow {

    @Context
    public Log log;

    @Context
    public GraphDatabaseService db;

    @Procedure(name = "nn.createInputRow", mode = Mode.WRITE)
    @Description("Creates a new Row node with the given ID and type 'inputsRow'.")
    public Stream<CreateResult> createInputRow(@Name("id") String id) {
        try (Transaction tx = db.beginTx()) {
            // Exécuter la requête Cypher pour créer le nœud Row
            tx.execute("CREATE (n:Row { id: "+ id +", type: 'inputsRow' })");
            return Stream.of(new CreateResult("ok"));
        } catch (Exception e) {
            log.error("Failed to create input Row: " + e.getMessage());
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