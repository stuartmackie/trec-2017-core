package fyi.foobar.core2017;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import fyi.foobar.core2017.index.Lucene;
import fyi.foobar.core2017.runs.BM25;
import fyi.foobar.core2017.runs.Classic;
import fyi.foobar.core2017.runs.DFI;
import fyi.foobar.core2017.runs.LM_D;
import fyi.foobar.core2017.runs.LM_JM;
import fyi.foobar.core2017.runs.TFIDF;
import fyi.foobar.core2017.trec.Topic;
import fyi.foobar.core2017.trec.Topics;

/**
 * TREC 2017 Core Track.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {

        /*
         * Initialisation:
         */

        logger.info("TREC 2017 Core Track");

        CompositeConfiguration config = new CompositeConfiguration();

        try {

            // System properties (-Dfoo=bar):
            config.addConfiguration(new SystemConfiguration());

            // Application properties:
            Configurations configs = new Configurations();
            config.addConfiguration(configs.properties("application.properties"));

        } catch (ConfigurationException e) {
            logger.error("Configuration error.", e);
            System.exit(-1);
        }

        /*
         * TREC topics:
         */

        if (args[0].equals("topics")) {
            Topics topics = new Topics(config);
            for (Topic topic : topics)
                System.out.println(topic);
        }

        /*
         * Lucene:
         */

        if (args[0].equals("index"))
            Lucene.index(config);

        /*
         * Query:
         */

        if (args[0].equals("query")) {

            IndexReader ir = null;
            try {
                ir = DirectoryReader
                        .open(FSDirectory.open(Paths.get(config.getString("core2017.lucene") + "/"
                                + config.getString("core2017.textproc"))));
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }

            ExecutorService executor = Executors.newFixedThreadPool(4);

            /*
             * Runs:
             */

            // Lucene:
            executor.submit(new Classic(config, ir));
            executor.submit(new BM25(config, ir));
            executor.submit(new LM_D(config, ir));
            executor.submit(new LM_JM(config, ir));
            executor.submit(new DFI(config, ir));

            // Custom:
            executor.submit(new TFIDF(config, ir));

            // Tidy up:
            executor.shutdown();
        }
    }

}
