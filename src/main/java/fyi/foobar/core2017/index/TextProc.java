package fyi.foobar.core2017.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

/**
 * Text processing: custom analyzers.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class TextProc {

    private static final Logger logger = LogManager.getLogger(TextProc.class);

    public static Analyzer analyzer(Configuration config) {

        String textproc = config.getString("core2017.textproc");

        Map<String, Analyzer> perfield = new HashMap<String, Analyzer>();

        /* Standard analyzer. */
        if (textproc.equals("standard")) {
            perfield.put("dateline", new StandardAnalyzer());
            perfield.put("headline", new StandardAnalyzer());
            perfield.put("leadpara", new StandardAnalyzer());
            perfield.put("summary", new StandardAnalyzer());
            perfield.put("fulltext", new StandardAnalyzer());
        }

        /* English analyzer. */
        if (textproc.equals("english")) {
            perfield.put("dateline", new EnglishAnalyzer());
            perfield.put("headline", new EnglishAnalyzer());
            perfield.put("leadpara", new EnglishAnalyzer());
            perfield.put("summary", new EnglishAnalyzer());
            perfield.put("fulltext", new EnglishAnalyzer());
        }

        /* Custom analyzer: stop-none-stem-none */
        if (textproc.equals("stop-none-stem-none")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-none-stem-porter */
        if (textproc.equals("stop-none-stem-porter")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Snowball stemmer:
                        .addTokenFilter(SnowballPorterFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-none-stem-krovetz */
        if (textproc.equals("stop-none-stem-krovetz")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Krovetz stemmer:
                        .addTokenFilter(KStemFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-fifty-stem-none */
        if (textproc.equals("stop-fifty-stem-none")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-fifty", "format", "wordset")
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-fifty-stem-porter */
        if (textproc.equals("stop-fifty-stem-porter")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-fifty", "format", "wordset")
                        // Snowball stemmer:
                        .addTokenFilter(SnowballPorterFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-fifty-stem-krovetz */
        if (textproc.equals("stop-fifty-stem-krovetz")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-fifty", "format", "wordset")
                        // Krovetz stemmer:
                        .addTokenFilter(KStemFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-lucene-stem-none */
        if (textproc.equals("stop-lucene-stem-none")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-lucene", "format", "wordset")
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-lucene-stem-porter */
        if (textproc.equals("stop-lucene-stem-porter")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-lucene", "format", "wordset")
                        // Snowball stemmer:
                        .addTokenFilter(SnowballPorterFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-lucene-stem-krovetz */
        if (textproc.equals("stop-lucene-stem-krovetz")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-lucene", "format", "wordset")
                        // Krovetz stemmer:
                        .addTokenFilter(KStemFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-terrier-stem-none */
        if (textproc.equals("stop-terrier-stem-none")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-terrier", "format", "wordset")
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-terrier-stem-porter */
        if (textproc.equals("stop-terrier-stem-porter")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-terrier", "format", "wordset")
                        // Snowball stemmer:
                        .addTokenFilter(SnowballPorterFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        /* Custom analyzer: stop-terrier-stem-krovetz */
        if (textproc.equals("stop-terrier-stem-krovetz")) {
            try {
                Analyzer analyzer = CustomAnalyzer.builder()
                        // Tokenizer:
                        .withTokenizer(StandardTokenizerFactory.class)
                        // Lower case:
                        .addTokenFilter(LowerCaseFilterFactory.class)
                        // Stopwords:
                        .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words",
                                "stop-terrier", "format", "wordset")
                        // Krovetz stemmer:
                        .addTokenFilter(KStemFilterFactory.class)
                        // CustomAnalyzer:
                        .build();
                perfield.put("dateline", analyzer);
                perfield.put("headline", analyzer);
                perfield.put("leadpara", analyzer);
                perfield.put("summary", analyzer);
                perfield.put("fulltext", analyzer);
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(-1);
            }
        }

        PerFieldAnalyzerWrapper analyzer =
                new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), perfield);

        return analyzer;
    }
}
