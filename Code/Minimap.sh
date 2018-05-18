#! /bin/bash
number="$1"
outdir="$2"
cd minimap2
make
cd ..
./minimap2/minimap2 $outdir/AllSegsOfSCN_CRRemoved.fasta $outdir/AllSegsOfSCN_CRRemoved.fasta -c -X -w1 -t16 --min-occ-floor 200 > $outdir/AllSegsOfSCN.info
wait
echo end minimap alignments of putative SDs
