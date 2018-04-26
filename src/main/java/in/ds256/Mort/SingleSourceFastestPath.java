package in.ds256.Mort;


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
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.Path;
import java.lang.*;
import java.util.*;
import java.io.IOException;

/**
"Finds all fastest paths from a selected vertex also taking care of parking vertices!"
*/

public class SingleSourceFastestPath extends BasicComputation<
    LongWritable, Text, Text, Text> {
  /** The shortest paths id */
  public static final LongConfOption SOURCE_ID = new LongConfOption("SingleSourceFastestPath.sourceId", 1, "The shortest paths id");       //386896 is new Source Vertex
  /** Class logger */
  private static final Logger LOG = Logger.getLogger(SingleSourceFastestPath.class);

  /**
   * Is this vertex the source id?
   *
   * @param vertex Vertex
   * @return True if the source id
   */
  /*private boolean isSource() {
      return (getVertexId().get() == getContext().getConfiguration().getLong(SOURCE_ID, SOURCE_ID_DEFAULT));
  }
*/
  private boolean isSource(Vertex<LongWritable, ?, ?> vertex) {
        return vertex.getId().get() == SOURCE_ID.get(getConf());
  }

  @Override
  public void compute(Vertex<LongWritable, Text, Text> vertex,Iterable<Text> messages) throws IOException {
    if (getSuperstep() == 0) {
      String vValues[] = vertex.getValue().toString().split("!");	//Convert the text input of form "dist!parkingvertex" to string array
      vValues[0]=Long.toString(Long.MAX_VALUE);				//set maximum distance to largest value in double
      String newValue = vValues[0]+"!"+vValues[1]+"!"+vValues[2];                //Set the computed value back to some string
      vertex.setValue(new Text(newValue));				//Set the values into a new Text Object
    }
    long minWait = 10;                                       //The minimum waiting time is used only for parking vertices
    long minDist = isSource(vertex) ? 0 : Long.MAX_VALUE; //only for source vertex, distance is 0, all other remain inf
    for (Text message : messages) {
        String msgStr = message.toString();
        long msgVal = Long.parseLong(msgStr);
        minDist = Math.min(minDist, msgVal);		//everytime you receive msgs, ensure that only minimum is assigned as dist for current vertex
    }

    //LOG.info("Vertex " + vertex.getId() + " got minDist = " + minDist + " vertex value = " + vertex.getValue());

    //   vertex.getValue() will be used for parking vertex
    String vertexVal[] = vertex.getValue().toString().split("!");
    long compDist = Long.parseLong(vertexVal[0]);
    boolean parkingVertex = vertexVal[2].equals("1") ? true : false;    //check if current vertex is parking vertex
    if (minDist < compDist) {
        vertexVal[0] = Long.toString(minDist);
        String newVertexVal = vertexVal[0] + "!" + vertexVal[1]+ "!" + vertexVal[2];
        vertex.setValue(new Text(newVertexVal));
        for (Edge<LongWritable, Text> edge : vertex.getEdges()) {
	        String e[] = edge.getValue().toString().split("!");
            //System.out.println("length of vertex "+vertex.getId()+" is "+e.length);
            // adding
            long temp = minDist;
            int timeSlot = (((int)temp)/60)%24;
            long d = Long.parseLong(e[timeSlot]);
            //done
            long distance = minDist + d; //Double.parseDouble(edge.getValue().get().toString());
            if (parkingVertex) {
                distance = distance + minWait;
            }
            String dist = Long.toString(distance);
            //LOG.info("Vertex " + vertex.getId() + " sent to " + edge.getTargetVertexId() + " = " + distance);
            sendMessage(edge.getTargetVertexId(), new Text(dist));
        }
    }
    vertex.voteToHalt();
  }

}
