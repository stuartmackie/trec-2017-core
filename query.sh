#/bin/bash

java -Xmx4g -Dcore2017.textproc=standard                   -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query

java -Xmx4g -Dcore2017.textproc=english                    -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query

java -Xmx4g -Dcore2017.textproc=stop-none-stem-none        -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-none-stem-porter      -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-none-stem-krovetz     -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query

java -Xmx4g -Dcore2017.textproc=stop-fifty-stem-none       -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-fifty-stem-porter     -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-fifty-stem-krovetz    -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query

java -Xmx4g -Dcore2017.textproc=stop-lucene-stem-none      -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-lucene-stem-porter    -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-lucene-stem-krovetz   -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query

java -Xmx4g -Dcore2017.textproc=stop-terrier-stem-none     -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-terrier-stem-porter   -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query
java -Xmx4g -Dcore2017.textproc=stop-terrier-stem-krovetz  -Dcore2017.ranklength=1000 -jar target/trec-2017-core.jar query


## 
