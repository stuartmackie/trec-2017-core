#/bin/bash

java -Xmx4g -Dcore2017.textproc=standard                   -jar target/trec-2017-core.jar index

java -Xmx4g -Dcore2017.textproc=english                    -jar target/trec-2017-core.jar index

java -Xmx4g -Dcore2017.textproc=stop-none-stem-none        -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-none-stem-porter      -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-none-stem-krovetz     -jar target/trec-2017-core.jar index

java -Xmx4g -Dcore2017.textproc=stop-fifty-stem-none       -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-fifty-stem-porter     -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-fifty-stem-krovetz    -jar target/trec-2017-core.jar index

java -Xmx4g -Dcore2017.textproc=stop-lucene-stem-none      -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-lucene-stem-porter    -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-lucene-stem-krovetz   -jar target/trec-2017-core.jar index

java -Xmx4g -Dcore2017.textproc=stop-terrier-stem-none     -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-terrier-stem-porter   -jar target/trec-2017-core.jar index
java -Xmx4g -Dcore2017.textproc=stop-terrier-stem-krovetz  -jar target/trec-2017-core.jar index


## 
