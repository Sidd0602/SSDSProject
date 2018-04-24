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
import java.lang.*;
import java.util.*;
import java.io.IOException;

/**
 * 
"Finds all fastest paths from a selected vertex also taking care of parking vertices!"
*/

public class SingleSourceFastestPath extends BasicComputation<
    LongWritable, Text, Text, DoubleWritable> {
  /** The shortest paths id */
  public static final LongConfOption SOURCE_ID =
      new LongConfOption("SingleSourceFastestPath.sourceId", 1, "The shortest paths id");
  /** Class logger */
  private static final Logger LOG = Logger.getLogger(SingleSourceFastestPath.class);

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
  public void compute(Vertex<LongWritable, Text, Text> vertex,Iterable<DoubleWritable> messages) throws IOException {
    if (getSuperstep() == 0) {
      String vValues[] = vertex.getValue().toString().split("!");	//Convert the text input of form "dist!parkingvertex" to string array
      vVal[0]=Long.toString(Long.MAX_VALUE);				//set maximum distance to largest value in double
      String newValue = vVal[0]+"!"+vVal[1];				//Set the computed value back to some string		
      vertex.setValue(new Text(newValue));				//Set the values into a new Text Object
    }

    minDist = isSource(vertex) ? :0 : Long.MAX_VALUE; //only for source vertex, distance is 0, all other remain inf
    for (DoubleWritable message : messages) {
      minDist = Math.min(minDist, message.get());		//everytime you receive msgs, ensure that only minimum is assigned as to dist for current vertex
    }
      LOG.info("Vertex " + vertex.getId() + " got minDist = " + minDist + " vertex value = " + vertex.getValue());

    //   vertex.getValue() will be used for parking vertex

    if (minDist < vertex.getValue().get()) {
      vertex.setValue(new DoubleWritable(minDist));
      for (Edge<LongWritable, Text> edge : vertex.getEdges()) {
	String e[] = edge.getValue().toString().split("!");
        System.out.println("length of vertex "+vertex.getId()+" is "+e.length);
        // adding
              Double temp = new Double(minDist);
              int tim = ((temp.intValue())/6)%3;
              double d = Integer.parseInt(e[tim]);
       //done 
	
        double distance = minDist + d; //Double.parseDouble(edge.getValue().get().toString());
        LOG.info("Vertex " + vertex.getId() + " sent to " + edge.getTargetVertexId() + " = " + distance);
        sendMessage(edge.getTargetVertexId(), new LongWritable(distance));
      }
    }
    vertex.voteToHalt();
  }
}
