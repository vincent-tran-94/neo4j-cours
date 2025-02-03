package com.sorbonne;

import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.stream.Stream;
import java.util.*;


public class CreateNeuron {
    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;
    @Context
    public GraphDatabaseService db;

    // Exemple de base à lire completer corriger
    // Pour utiliser call nn.createNeuron("123","0","input","sotfmax")
    @Procedure(name = "nn.createNeuron",mode = Mode.WRITE)
    @Description("Create a Neuron to specified id, layer and type and activation function")
    public Stream<CreateResult> createNeuron(@Name("id") String id,
                                       @Name("layer") String layer,
                                       @Name("type") String type,
                                       @Name("activation_function") String activation_function
    ) {
        try (Transaction tx = db.beginTx()) {
            String query = "CREATE (n:Neuron { " +
                    "id: $id, " +
                    "layer: $layer, " +
                    "type: $type, " +
                    "bias: 0.0, " +
                    "output: null, " +
                    "m_bias: 0.0, " +
                    "v_bias: 0.0, " +
                    "activation_function: $activation_function " +
                    "})";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", id);
            parameters.put("layer", layer);
            parameters.put("type", type);
            parameters.put("activation_function", activation_function);

            tx.execute(query, parameters);
            tx.commit();
            return Stream.of(new CreateResult("ok"));

        } catch (Exception e) {
            log.error("Failed to create Neuron: " + e.getMessage());
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