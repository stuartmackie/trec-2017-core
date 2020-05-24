package fyi.foobar.core2017.runs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DFISimilarity;
import org.apache.lucene.search.similarities.Independence;
import org.apache.lucene.search.similarities.IndependenceChiSquared;
import org.apache.lucene.search.similarities.IndependenceSaturated;
import org.apache.lucene.search.similarities.IndependenceStandardized;
import fyi.foobar.core2017.index.TextProc;
import fyi.foobar.core2017.trec.Topic;
import fyi.foobar.core2017.trec.Topics;

/**
 * DFI.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class DFI implements Runnable {

    private static final Logger logger = LogManager.getLogger(DFI.class);

    private Configuration config;
    private IndexReader ir;
    private Topics topics;
    private String evaldir;
    private String textproc;
    private int ranklength;

    private static final Independence[] independence =
            new Independence[] {new IndependenceChiSquared(), new IndependenceSaturated(),
                    new IndependenceStandardized()};

    private DecimalFormat df_rank = new DecimalFormat("0");
    private DecimalFormat df_score = new DecimalFormat("00.0000");

    public DFI(Configuration config, IndexReader ir) {
        this.config = config;
        this.ir = ir;
        topics = new Topics(config);
        evaldir = config.getString("core2017.evaldir");
        textproc = config.getString("core2017.textproc");
        ranklength = config.getInt("core2017.ranklength");
    }

    public void run() {

        for (Independence param : independence) {

            StringBuilder run = new StringBuilder();

            logger.info("[" + textproc + "]" + " " + "DFI" + "-" + param);

            IndexSearcher is = new IndexSearcher(ir);
            is.setSimilarity(new DFISimilarity(param));

            for (Topic topic : topics) {

                Query query = null;
                TopDocs results = null;

                try {
                    StandardQueryParser qp = new StandardQueryParser(TextProc.analyzer(config));
                    query = qp.parse(topic.title, "fulltext");
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                    continue;
                }

                try {
                    results = is.search(query, ranklength);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

                if (results != null && results.totalHits.value > 0) {

                    ScoreDoc[] ranking = results.scoreDocs;

                    for (int i = 0; i < Math.min(ranklength, results.totalHits.value); i++) {

                        Document document = null;
                        try {
                            document = is.doc(ranking[i].doc);
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }

                        String docid = document.get("docno");
                        String yyyymmdd = document.get("yyyymmdd");
                        String rank = df_rank.format(i);
                        String score = df_score.format(ranking[i].score);

                        run.append(topic.num + " " + yyyymmdd + " " + docid + " " + rank + " "
                                + score + " " + "DFI" + "-" + param + "-" + textproc + "\n");
                    }
                }
            }

            try {
                FileUtils.writeStringToFile(
                        new File(evaldir + "/" + "DFI" + "-" + param + "-" + textproc),
                        run.toString(), StandardCharsets.UTF_8, true);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }
    }
}
