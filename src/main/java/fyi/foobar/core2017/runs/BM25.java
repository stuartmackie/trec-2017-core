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
import org.apache.lucene.search.similarities.BM25Similarity;
import fyi.foobar.core2017.index.TextProc;
import fyi.foobar.core2017.trec.Topic;
import fyi.foobar.core2017.trec.Topics;

/**
 * BM25.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class BM25 implements Runnable {

    private static final Logger logger = LogManager.getLogger(BM25.class);

    private Configuration config;
    private IndexReader ir;
    private Topics topics;
    private String evaldir;
    private String textproc;
    private int ranklength;

    private static final String[] k1 = new String[] {"0.00", "0.10", "0.20", "0.40", "0.60", "0.80",
            "1.00", "1.20", "1.40", "1.60", "1.80", "2.00"};
    private static final String[] b = new String[] {"0.00", "0.05", "0.10", "0.15", "0.20", "0.25",
            "0.30", "0.35", "0.40", "0.45", "0.50", "0.55", "0.60", "0.65", "0.70", "0.75", "0.80",
            "0.85", "0.90", "0.95", "1.00"};

    private DecimalFormat df_rank = new DecimalFormat("0");
    private DecimalFormat df_score = new DecimalFormat("00.0000");

    public BM25(Configuration config, IndexReader ir) {
        this.config = config;
        this.ir = ir;
        topics = new Topics(config);
        evaldir = config.getString("core2017.evaldir");
        textproc = config.getString("core2017.textproc");
        ranklength = config.getInt("core2017.ranklength");
    }

    public void run() {

        for (String param1 : k1) {
            for (String param2 : b) {

                StringBuilder run = new StringBuilder();

                logger.info("[" + textproc + "]" + " " + "BM25" + "-" + param1 + "-" + param2);

                IndexSearcher is = new IndexSearcher(ir);
                is.setSimilarity(new BM25Similarity(Float.valueOf(param1), Float.valueOf(param2)));

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
                                    + score + " " + "BM25" + "-" + param1 + "-" + "-" + param2 + "-"
                                    + textproc + "\n");
                        }
                    }
                }

                try {
                    FileUtils.writeStringToFile(new File(
                            evaldir + "/" + "BM25" + "-" + param1 + "-" + param2 + "-" + textproc),
                            run.toString(), StandardCharsets.UTF_8, true);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    System.exit(-1);
                }
            }
        }
    }
}
