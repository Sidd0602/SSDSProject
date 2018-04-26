package in.ds256.Mort;

//import jdk.internal.jline.internal.Log;
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


public class RouteRetrieval extends BasicComputation<LongWritable, Text, Text, Text> {
    /** The shortest paths id */
    public static final LongConfOption SOURCE_ID = new LongConfOption("RouteRetrieval.sourceId",5, "The shortest paths id");       //386896 is the sink to traverse back from.
    private static final Logger LOG = Logger.getLogger(RouteRetrieval.class);

    private boolean isDestination(Vertex<LongWritable, ?, ?> vertex) {
        return vertex.getId().get() == SOURCE_ID.get(getConf());
    }

    @Override
    public void compute(Vertex<LongWritable, Text, Text> vertex,Iterable<Text> messages) throws IOException {
        int tmin = 10;
        long srcId = 1;
        if (getSuperstep() == 0) {
            if (isDestination(vertex)) {
                String vValues[] = vertex.getValue().toString().split("!");
                String cValues[] = vValues[3].split("#");
                String eachC[] = cValues[0].split("@");
                String leastRecord = cValues[0];
                long leastCost = Long.parseLong(eachC[0]);
                for (int i = 1; i < cValues.length; i++) {
                    String c[] = cValues[i].split("@");
                    if (Long.parseLong(c[0]) < leastCost) {
                        leastCost = Long.parseLong(c[0]);
                        leastRecord = cValues[i];
                    }
                }
                String parseLeastRec[] = leastRecord.split("@");
                LongWritable destId = new LongWritable (Long.parseLong(parseLeastRec[2]));
                sendMessage(destId, new Text (leastRecord));
            }
            LOG.info("Vertex " + vertex.getId() + " is visited now." + "at SS# " + getSuperstep());
        } else {
            String vValues[] = vertex.getValue().toString().split("!");
            String cValues[] = vValues[3].split("#");
            String eachC[] = cValues[0].split("@");
            String leastRecord = cValues[0];
            long leastCost = Long.parseLong(eachC[0]);
            for (Text message : messages) {
                String msgData = message.toString();
                String msgD[] = msgData.split("@");
                int time = Integer.parseInt(msgD[1]);
                if (vValues[2].equals("0")) {
                    for (int i = 1; i < cValues.length ; i++) {
                        String c[] = cValues[i].split("@");
                        if (Integer.parseInt(c[1]) <= time) {
                            if (Long.parseLong(c[0]) < leastCost) {
                                leastCost = Long.parseLong(c[0]);
                                leastRecord = cValues[i];
                            }
                        }
                    }
                } else {
                    for (int i = 1; i < cValues.length ; i++) {
                        String c[] = cValues[i].split("@");
                        if (Integer.parseInt(c[1]) <= (time-tmin)) {
                            if (Long.parseLong(c[0]) < leastCost) {
                                leastCost = Long.parseLong(c[0]);
                                leastRecord = cValues[i];
                            }
                        }
                    }
                }

            }
            String parseLeastRec[] = leastRecord.split("@");
            LongWritable destId = new LongWritable (Long.parseLong(parseLeastRec[2]));
            LOG.info("Vertex " + vertex.getId() + " is visited now." + + "at SS# " + getSuperstep());
            if(destId != srcId) {
                sendMessage(destId,new Text(leastRecord));
            }

        }

        vertex.voteToHalt();
    }
}

