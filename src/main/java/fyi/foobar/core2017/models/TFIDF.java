package fyi.foobar.core2017.models;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

/**
 * "textbook" tf*idf.
 * 
 * @author Stuart Mackie (stuart@foobar.fyi).
 * @version May 2020.
 */
public class TFIDF extends SimilarityBase {

    @Override
    public String toString() {
        return "tf*idf";
    }

    @Override
    protected double score(BasicStats stats, double freq, double docLen) {

        // The number of documents in the collection:
        long N = stats.getNumberOfDocuments();

        // The number of documents containing the term t:
        long Nt = stats.getDocFreq();

        // Inverse document frequency:
        double idf = log2(N / Nt);

        // Term frequency;
        double tf = log2(1.00 + freq);

        return tf * idf;
    }
}
