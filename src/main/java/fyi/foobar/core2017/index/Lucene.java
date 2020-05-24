package fyi.foobar.core2017.index;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import com.nytlabs.corpus.NYTCorpusDocument;
import com.nytlabs.corpus.NYTCorpusDocumentParser;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Index NYT Corpus documents.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class Lucene {

    private static final Logger logger = LogManager.getLogger(Lucene.class);

    public static void index(Configuration config) {

        String corpus = config.getString("core2017.corpus");
        String lucene = config.getString("core2017.lucene");
        String textproc = config.getString("core2017.textproc");

        try {
            FileUtils.deleteDirectory(new File(lucene + "/" + textproc));
            FileUtils.forceMkdir(new File(lucene + "/" + textproc));
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

        // Lucene indexer:
        IndexWriter iw = null;

        // Lucene configuration:
        try {
            Directory dir = FSDirectory.open(Paths.get(lucene + "/" + textproc));
            IndexWriterConfig iwc = new IndexWriterConfig(TextProc.analyzer(config));
            iwc.setOpenMode(OpenMode.CREATE);
            iwc.setSimilarity(new BM25Similarity());
            iw = new IndexWriter(dir, iwc);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        /*
         * NYT Corpus:
         */

        try {

            NYTCorpusDocumentParser nytcorpus = new NYTCorpusDocumentParser();

            DateTimeFormatter dtf_yyyymmdd =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));

            for (File file : FileUtils.listFiles(new File(corpus), new String[] {"tgz"}, true)) {

                try {

                    TarArchiveInputStream tgz =
                            new TarArchiveInputStream(new GzipCompressorInputStream(
                                    new BufferedInputStream(new FileInputStream(file))));

                    TarArchiveEntry tar = null;

                    while ((tar = tgz.getNextTarEntry()) != null) {

                        if (tar.isFile()) {

                            File tmp = new File(
                                    System.getProperty("java.io.tmpdir") + "/" + "nytcorpus-ntif");
                            IOUtils.copy(tgz, new FileOutputStream(tmp));

                            NYTCorpusDocument nytdoc =
                                    nytcorpus.parseNYTCorpusDocumentFromFile(tmp, false);

                            if (nytdoc != null) {

                                String docno = String.valueOf(nytdoc.getGuid());
                                String yyyymmdd = dtf_yyyymmdd
                                        .format(nytdoc.getPublicationDate().toInstant());

                                String headline = nytdoc.getHeadline() == null ? ""
                                        : nytdoc.getHeadline().replaceAll("\n", " ");
                                String body = nytdoc.getBody() == null ? ""
                                        : nytdoc.getBody().replaceAll("\n", " ");

                                // Lucene document:
                                Document doc = new Document();
                                doc.add(new StringField("docno", docno, Field.Store.YES));
                                doc.add(new StringField("yyyymmdd", yyyymmdd, Field.Store.YES));
                                doc.add(new TextField("fulltext", headline + " " + body,
                                        Field.Store.NO));

                                // Index document:
                                iw.addDocument(doc);
                            }

                            FileUtils.deleteQuietly(tmp);
                        }
                    }

                    tgz.close();

                } catch (IOException e) {
                    logger.error(e.getMessage());
                    System.exit(-1);
                }

                logger.info(textproc + " " + "[" + file.toString() + "]" + " "
                        + iw.getDocStats().numDocs);
            }

            iw.close();

        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
    }

}
