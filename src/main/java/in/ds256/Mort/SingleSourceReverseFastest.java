package in.ds256.Mort;

import java.io.IOException;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.conf.LongConfOption;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import java.lang.*;
import java.util.*;
import java.io.IOException;


public class SingleSourceReverseFastest extends BasicComputation<
        LongWritable, Text, Text, Text> {
    /** The shortest paths id */
    public static final LongConfOption SOURCE_ID =
            new LongConfOption("SingleSourceReverseFastest.sourceId", 5 , "The shortest paths id");       //386896 is new Destination Vertex
    /** Class logger */
    private static final Logger LOG = Logger.getLogger(SingleSourceReverseFastest.class);

    /**
     * Is this vertex the source id?
     *
     * @param vertex Vertex
     * @return True if the source id
     */
    private boolean isSource(Vertex<LongWritable, ?, ?> vertex) {
        return vertex.getId().get() == SOURCE_ID.get(getConf());
    }

    @Override
    public void compute(Vertex<LongWritable, Text, Text> vertex,Iterable<Text> messages) throws IOException {
        if (getSuperstep() == 0) {
            String vValues[] = vertex.getValue().toString().split("!");	//Convert the text input of form "dist!parkingvertex" to string array
            vValues[1]=Long.toString(0);				//set maximum distance to largest value in double
            String newValue = vValues[0]+"!"+vValues[1]+"!"+vValues[2];                //Set the computed value back to some string
            vertex.setValue(new Text(newValue));				//Set the values into a new Text Object
        }
        long minWait = 10;                                       //The minimum waiting time is used only for parking vertices
        long tD= 300;
        long maxDist = isSource(vertex) ? tD : 0; //only for source vertex, distance is 0, all other remain inf
        for (Text message : messages) {
            String msgStr = message.toString();
            long msgVal = Long.parseLong(msgStr);
            maxDist = Math.max(maxDist, msgVal);		//everytime you receive msgs, ensure that only minimum is assigned as dist for current vertex
        }
        //LOG.info("Vertex " + vertex.getId() + " got minDist = " + minDist + " vertex value = " + vertex.getValue());

        //   vertex.getValue() will be used for parking vertex
        String vertexVal[] = vertex.getValue().toString().split("!");
        long compDist = Long.parseLong(vertexVal[1]);
        boolean parkingVertex = vertexVal[2].equals("1") ? true : false;    //check if current vertex is parking vertex
        if (maxDist > compDist) {
            vertexVal[1] = Long.toString(maxDist);
            String newVertexVal = vertexVal[0] + "!" + vertexVal[1]+ "!" + vertexVal[2];
            vertex.setValue(new Text(newVertexVal));
            for (Edge<LongWritable, Text> edge : vertex.getEdges()) {
                String e[] = edge.getValue().toString().split("!");
                //System.out.println("length of vertex "+vertex.getId()+" is "+e.length);
                // adding
                long temp = maxDist;
                int timeSlot = (((int)temp)/60)%24;
                long d = Long.parseLong(e[timeSlot]);
                //done
                long distance = maxDist - d; //Double.parseDouble(edge.getValue().get().toString());
                if (parkingVertex) {
                    distance = distance - minWait;
                }
                String dist = Long.toString(distance);
                //      LOG.info("Vertex " + vertex.getId() + " sent to " + edge.getTargetVertexId() + " = " + distance);
                sendMessage(edge.getTargetVertexId(), new Text(dist));
            }
        }
        vertex.voteToHalt();
    }
}