# trec-2017-core

Tools for indexing the NYT Corpus, and retrieval runs for the TREC 2017 Core Track.

## TREC 2017 Core Track

* [trec-core.github.io/2017/](https://trec-core.github.io/2017/)
* [trec.nist.gov/data/core2017.html](https://trec.nist.gov/data/core2017.html)
* [trec.nist.gov/pubs/trec26/papers/Overview-CC.pdf](https://trec.nist.gov/pubs/trec26/papers/Overview-CC.pdf)

## New York Times Annotated Corpus

* [catalog.ldc.upenn.edu/LDC2008T19](https://catalog.ldc.upenn.edu/LDC2008T19)

## Build/config

~~~~
$ mvn clean package
~~~~

* application.properties

~~~~
# NYT Corpus "data" directory:
core2017.corpus=/data/nytcorpus/data

# Lucene index directory:
core2017.lucene=/data/nytcorpus/lucene

# Lucene textproc config:
core2017.textproc=standard

# TREC 2017 Core Track:
core2017.topics=/data/nytcorpus/TREC-2017/core_nist.txt
core2017.evaldir=/data/nytcorpus/treceval
core2017.ranklength=1000
~~~~

## TREC topics

Print out the TREC 2017 Core Track topics:

~~~~
$ java -jar target/trec-2017-core.jar topics
~~~~

## TREC runs (Lucene)

Indexing, retrieval, and evaluation:

~~~~
$ time ./index.sh
$ time ./query.sh
$ time ./eval.sh | column -t > eval.res
~~~~

~~~~
Model    Param         TextProc                   MAP*    P@10    nDCG    Recall
BM25     1.80-0.40     stop-none-stem-krovetz     0.2238  0.5300  0.4584  0.5556
BM25     1.80-0.45     stop-fifty-stem-krovetz    0.2240  0.5260  0.4575  0.5550
BM25     1.80-0.45     stop-none-stem-krovetz     0.2240  0.5300  0.4570  0.5547
BM25     1.60-0.45     stop-lucene-stem-krovetz   0.2242  0.5200  0.4578  0.5563
BM25     1.80-0.45     stop-lucene-stem-krovetz   0.2243  0.5300  0.4582  0.5560
~~~~

