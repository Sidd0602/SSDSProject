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
        LongWritable, Text, Text, LongWritable> {
    /** The shortest paths id */
    public static final LongConfOption SOURCE_ID =
            new LongConfOption("SingleSourceReverseFastest.sourceId", 12 , "The shortest paths id");       //386896 is new Destination Vertex
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
    public void compute(Vertex<LongWritable, Text, Text> vertex,Iterable<LongWritable> messages) throws IOException {
    }
}