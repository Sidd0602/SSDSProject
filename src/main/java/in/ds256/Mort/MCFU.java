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

/*minimum cost function, denoted as Ci(t), monitors
the minimum on-road cost of traveling from vs to vi that
arrives on time t*/
/*
public class MCFU extends BasicComputation <LongWritable, Text, Text, LongWritable> {
    @Override
    public void Compute (Vertex<LongWritable, Text, Text>, Iterable<LongWritable> messages) throws IOException {
        if (getSuperstep == 0) {
            String vValues[] = vertex.getValue().toString().split("!");
            if (received vertex is parking vertex) {
                int gFuncTTT = cFuncTT + wtFromInNeighbourAtTT;          //cFunc and wtFromInNeighbour received in msg from in neighbour thus put in loop and iterate over it for all inNeighbours
                int newTT = oldT + minWaitTimeRecv;
                int newTTT = newTT + wtFromInNeighbourTT;
            } else {
                int gFunctTT = cFunc + wtFromInNeighbourAtT;
                int newTTT = oldT + wtFromInNeighbourAtT;
            }

            if (vValues[2].equals("1")) {
                int gNew = NonIncrease(gFuncT);
            }

            t1init;
            t1final;
            t2init;
            t2final;
        }
    }
}*/
