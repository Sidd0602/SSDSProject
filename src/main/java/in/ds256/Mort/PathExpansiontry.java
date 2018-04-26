/*
package in.ds256.Mort;

import java.util.HashMap;
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


public class PathExpansiontry extends BasicComputation <LongWritable, Text, Text, Text>  {
*/
/*
    public static final LongConfOption SOURCE_ID =
            new LongConfOption("PathExpansiontry.sourceId", 1, "The shortest paths id");

    private boolean isSource(Vertex<LongWritable, ?, ?> vertex) {
        return vertex.getId().get() == SOURCE_ID.get(getConf());
    }

    static long vs1 = 5;
    static long minWait = 10;
    static long source =1;
    @Override
    public void compute(Vertex<LongWritable, Text, Text> vertex,Iterable<Text> messages) throws IOException {
        //long ts1 = 5;
        long vEA,vTD;
        String vValues[] = vertex.getValue().toString().split("!");
        vEA = Long.parseLong(vValues[0]);
        vTD = Long.parseLong(vValues[1]);
        boolean parking = vValues[0].equals("1") ? true : false;

        if (getSuperstep() == 0) {
            long cost = vEA-vs1;
            String newValue = vertex.getValue().toString() + "!" + Long.toString(Long.MAX_VALUE) + "@" +Long.toString(Long.MAX_VALUE)+"@"+vertex.getId();
            vertex.setValue(new Text(newValue));				//Set the values into a new Text Object
        }
        Map<Long, String> mapold = new HashMap<Long,String>();
        Map<Long, String> map = new HashMap<Long,String>();
        boolean tracker = false;
        //done
        if(getSuperstep() == 0 && isSource(vertex))
        {
            long windowStart = vEA/60;
            long windowEnd = vTD/60;
            String fnl ="";
            for (Edge<LongWritable, Text> edge : vertex.getEdges())
            {   String e[] = edge.getValue().toString().split("!");
                //String fnl ="";
                long nextTime=vEA;
                for(long i=windowStart;i<=windowEnd;i++)
                {
                    int timeSlot = (((int)nextTime)/60)%24;
                    long d = Long.parseLong(e[timeSlot]);
                    if(i==windowStart)
                    {
                        fnl = Long.toString(d)+"@"+Long.toString(vEA+d)+"@"+edge.getTargetVertexId();
                        nextTime = vEA-(vEA%60)+60;
                    }
                    else
                    {
                        fnl = fnl+"#"+Long.toString(d)+"@"+Long.toString(nextTime+d)+"@"+edge.getTargetVertexId();
                        nextTime = nextTime+60;
                    }
                }
                //changed	sendMessage(edge.getTargetVertexId(), new Text(fnl));
                sendMessage(edge.getTargetVertexId(), new Text(fnl));
                vertex.setValue(new Text(fnl));
            }
            //vertex.setValue(new Text(fnl+"@"+edge.getTargetVertexId()));
        }
// working fine
        if(getSuperstep() == 1)
            for (Text message : messages) {

                long windowStart = vEA/60;
                //long windowEnd = Math.min(vTD/60,windowStart+23);      //everytime you receive msgs
                //String can change to array
                long windowEnd = vTD/60;
                //long end = windowEnd/24 ;
                //long st = vEA/24;


                String ownCostsTime[] = vValues[3].split("#");

                for(String ownCost : ownCostsTime)
                {    long cost = Long.parseLong(ownCost.split("@")[0]);
                    String timStr =  ownCost.split("@")[1];
                    long timee = (Long.parseLong(timStr))/60;
                    if(timee<=windowEnd)
                        map.put(timee,ownCost);
                }

                String costsTime[] = message.toString().split("#");
                for(String costTime : costsTime)
                {
                    long costnew = Long.parseLong(costTime.split("@")[0]);
                    String timStrnew =  costTime.split("@")[1];
                    long timeenew = (Long.parseLong(timStrnew))/60;
                    String timeeValue = map.get(timeenew);
                    if(timeenew<=windowEnd)
                    {if(timeeValue==null)
                    {	map.put(timeenew,costTime);
                        tracker =true;
                    }
                    else
                    {	long storedcost = Long.parseLong(timeeValue.split("@")[0]);
                        String storedtimStr =  timeeValue.split("@")[1];
                        if(costnew<storedcost)   //should not send message to source run it in loop , add for parking vertices and use window end also // add cost also and correct time too set own valuye also
                        {	map.put(timeenew,costTime);
                            tracker = true;
                        }
                    }}
                }
                //String fnl = ;
		 *//*

*/
/*for (Map.Entry<Long,String> entry : map.entrySet())
		   {	long key = entry.getKey();
			    String value = entry.getValue();

				fnl = fnl + "#"+value;
				//fnl = fnl + "#" + value[0] + "@" + Long.toString(Long.parseLong(value[1]));
			}

		 //vertex.setValue(new Text(fnl));*//*
*/
/*


            }

        if(tracker == true)
        {    String fnl = "";
            for (Edge<LongWritable, Text> edge : vertex.getEdges())
            {      if((edge.getTargetVertexId()).get()!=source)
            {
                String e[] = edge.getValue().toString().split("!");
                //String fnl = "";
                String fnlVertex = vValues[0]+"!"+vValues[1]+"!"+vValues[2]+"!";
                for (Map.Entry<Long,String> entry : map.entrySet())
                {	long key = entry.getKey();
                    String value[] = entry.getValue().split("@");
                    int timeSlot = (((int)key))%24;
                    long d = Long.parseLong(e[timeSlot]);
                    fnlVertex = fnlVertex + "#"+entry.getValue();
                    fnl = fnl + "#" + Long.toString(Long.parseLong(value[0])+d) + "@" + Long.toString(Long.parseLong(value[1])+d);//+"%"+entry.getValue();
                    //fnl = fnl + "#" + value[0] + "@" + Long.toString(Long.parseLong(value[1]));
                }
                //vertex.setValue(new Text(fnlVertex.substring(0,fnlVertex.indexOf("#"))+fnlVertex.substring(fnlVertex.indexOf("#")+1)+" sending message "+fnl.substring(1)+" ID is "+(edge.getTargetVertexId().toString())));
                vertex.setValue( new Text ( fnlVertex.substring (0,fnlVertex.indexOf ("#"))+fnlVertex.substring(fnlVertex.indexOf("#")+1)));
                sendMessage(edge.getTargetVertexId(), new Text(fnl.substring(1)));
            }
            }
        }


        vertex.voteToHalt();
    }


*//*


}

*/
