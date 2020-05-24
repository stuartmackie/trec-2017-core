#/bin/bash

# time ./eval.sh | column -t > eval.res

# for key in "4" "5" "6" "7"; do head -n1 eval.res ; sort -n -k${key},${key} eval.res | tail -n 5 ; done

echo "Model Param TextProc MAP P@10 nDCG Recall"

for textproc in "standard" "english" "stop-none-stem-none" "stop-none-stem-porter" "stop-none-stem-krovetz" "stop-fifty-stem-none" "stop-fifty-stem-porter" "stop-fifty-stem-krovetz" "stop-lucene-stem-none" "stop-lucene-stem-porter" "stop-lucene-stem-krovetz" "stop-terrier-stem-none" "stop-terrier-stem-porter" "stop-terrier-stem-krovetz" ; do

    #
    # Lucene:
    #

    # Classic:
    declare -A hashmap
    while read metric qid score ; do
        hashmap["$metric"]="$score"
    done < <($HOME/tools/trec_eval/trec_eval -m map -m P.10 -m ndcg -m set_recall /data/nytcorpus/TREC-2017/qrels.txt /data/nytcorpus/treceval/Classic-${textproc})
    echo "Classic n/a ${textproc} ${hashmap["map"]} ${hashmap["P_10"]} ${hashmap["ndcg"]} ${hashmap["set_recall"]}"

    # BM25:
    for param1 in "0.00" "0.10" "0.20" "0.40" "0.60" "0.80" "1.00" "1.20" "1.40" "1.60" "1.80" "2.00" ; do
    for param2 in "0.00" "0.05" "0.10" "0.15" "0.20" "0.25" "0.30" "0.35" "0.40" "0.45" "0.50" "0.55" "0.60" "0.65" "0.70" "0.75" "0.80" "0.85" "0.90" "0.95" "1.00" ; do
        declare -A hashmap
        while read metric qid score ; do
            hashmap["$metric"]="$score"
        done < <($HOME/tools/trec_eval/trec_eval -m map -m P.10 -m ndcg -m set_recall /data/nytcorpus/TREC-2017/qrels.txt /data/nytcorpus/treceval/BM25-${param1}-${param2}-${textproc})
        echo "BM25 ${param1}-${param2} ${textproc} ${hashmap["map"]} ${hashmap["P_10"]} ${hashmap["ndcg"]} ${hashmap["set_recall"]}"
    done
    done

    # LM-D:
    for param in "0" "500" "1000" "1500" "2000" "2500" "3000" "3500" "4000" "4500" "5000" "10000" ; do
        declare -A hashmap
        while read metric qid score ; do
            hashmap["$metric"]="$score"
        done < <($HOME/tools/trec_eval/trec_eval -m map -m P.10 -m ndcg -m set_recall /data/nytcorpus/TREC-2017/qrels.txt /data/nytcorpus/treceval/LM-D-${param}-${textproc})
        echo "LM-D ${param} ${textproc} ${hashmap["map"]} ${hashmap["P_10"]} ${hashmap["ndcg"]} ${hashmap["set_recall"]}"
    done

    # LM-JM:
    for param in "0.01" "0.05" "0.10" "0.15" "0.20" "0.25" "0.30" "0.35" "0.40" "0.45" "0.50" "0.55" "0.60" "0.65" "0.70" "0.75" "0.80" "0.85" "0.90" "0.95" "1.00" ; do
        declare -A hashmap
        while read metric qid score ; do
            hashmap["$metric"]="$score"
        done < <($HOME/tools/trec_eval/trec_eval -m map -m P.10 -m ndcg -m set_recall /data/nytcorpus/TREC-2017/qrels.txt /data/nytcorpus/treceval/LM-JM-${param}-${textproc})
        echo "LM-JM ${param} ${textproc} ${hashmap["map"]} ${hashmap["P_10"]} ${hashmap["ndcg"]} ${hashmap["set_recall"]}"
    done

    # DFI:
    for param in "ChiSquared" "Saturated" "Standardized" ; do
        declare -A hashmap
        while read metric qid score ; do
            hashmap["$metric"]="$score"
        done < <($HOME/tools/trec_eval/trec_eval -m map -m P.10 -m ndcg -m set_recall /data/nytcorpus/TREC-2017/qrels.txt /data/nytcorpus/treceval/DFI-${param}-${textproc})
        echo "DFI ${param} ${textproc} ${hashmap["map"]} ${hashmap["P_10"]} ${hashmap["ndcg"]} ${hashmap["set_recall"]}"
    done

    #
    # Custom:
    #

    # TFIDF:
    declare -A hashmap
    while read metric qid score ; do
        hashmap["$metric"]="$score"
    done < <($HOME/tools/trec_eval/trec_eval -m map -m P.10 -m ndcg -m set_recall /data/nytcorpus/TREC-2017/qrels.txt /data/nytcorpus/treceval/TFIDF-${textproc})
    echo "TFIDF n/a ${textproc} ${hashmap["map"]} ${hashmap["P_10"]} ${hashmap["ndcg"]} ${hashmap["set_recall"]}"

done


## 