#! /bin/bash
number="$1"
outdir="$2"
cd minimap2
make
for ((i=1; i<=number; i++)); do
        ./minimap2 $outdir/AllSegsOfSCN_CRRemoved.fasta $outdir/AllSegsOfSCN_CRRemoved$i.fasta -c -X -E1,0 -O2,32 -w1 -t 2 --min-occ-floor 200 > $outdir/AllSegsOfSCN_CRRemoved$i.info &
done
wait
cd ..
echo end minimap alignments of putative SDs
