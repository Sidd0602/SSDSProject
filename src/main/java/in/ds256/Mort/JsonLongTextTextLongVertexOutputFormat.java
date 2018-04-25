package in.ds256.Mort;

import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import java.io.IOException;

public class JsonLongTextTextLongVertexOutputFormat extends TextVertexOutputFormat<LongWritable, Text, Text> {

    @Override
    public TextVertexWriter createVertexWriter(TaskAttemptContext context) {
        return new JsonLongTextTextLongVertexWriter();
    }

    /**
     * VertexWriter that supports vertices with <code>double</code>
     * values and <code>float</code> out-edge weights.
     */
    private class JsonLongTextTextLongVertexWriter extends TextVertexWriterToEachLine {
        @Override
        public Text convertVertexToLine(Vertex<LongWritable, Text, Text> vertex
        ) throws IOException {
            JSONArray jsonVertex = new JSONArray();
            try {
                jsonVertex.put(vertex.getId().get());
                String vertexValue = vertex.getValue().toString();
                Text vVal = new Text(vertexValue);
                jsonVertex.put(vVal);
                JSONArray jsonEdgeArray = new JSONArray();
                for (Edge<LongWritable, Text> edge : vertex.getEdges()) {
                    JSONArray jsonEdge = new JSONArray();
                    jsonEdge.put(edge.getTargetVertexId().get());
                    String edgeValue = edge.getValue().toString();
                    Text eVal = new Text(edgeValue);
                    jsonEdge.put(eVal);
                    jsonEdgeArray.put(jsonEdge);
                }
                jsonVertex.put(jsonEdgeArray);
            } catch (JSONException e) {
                throw new IllegalArgumentException(
                        "writeVertex: Couldn't write vertex " + vertex);
            }
            return new Text(jsonVertex.toString());
        }
    }
}
