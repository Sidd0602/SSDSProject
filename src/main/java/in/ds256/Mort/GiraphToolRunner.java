/*
package in.ds256.Mort;
import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.examples.SimpleShortestPathsComputation;
import org.apache.giraph.job.GiraphJob;
import org.apache. hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache. hadoop.util.ToolRunner;


public class GiraphToolRunner implements Tool {
    private Configuration conf;

    public Configuration getConf() {
        return conf;
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public int run(String argsArray[]) throws Exception {
        if (argsArray.length != 3) {
            throw new IllegalArgumentException("Run: must have 3 arguments <input path> <output path> <source id>");
        }
        GiraphConfiguration giraphConf = new GiraphConfiguration( getConf());
        giraphConf.setComputationClass(SingleSourceFastestPath.class ) ;
        GiraphJob giraphJob = new GiraphJob(giraphConf , getClass().getName());
        giraphJob.run( true );
        return 0;
    }

    public static void main(String [] args) throws Exception {
        ToolRunner.run(new GiraphDriverTool(), args);
    }
}
*/
