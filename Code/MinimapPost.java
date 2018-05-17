
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MinimapPost {

	public static int recover(String str, int index){
		int num=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)!='N'){
				num++;
			}
			if(num==index+1){
				return i;
			}
		}
//		if(num==index){
//			return str.length();
//		}
//		System.out.println("something wrong!");
		return -1;
	}
	public static int reverseRecover(String str, int index){
		int num=0;
		for(int i=str.length()-1;i>=0;i--){
			if(str.charAt(i)!='N'){
				num++;
			}
			if(num==index+1){
				return i;
			}
		}
//		System.out.println("something wrong in reverse case!");
		return -1;
	}
	public static int gapLength(String str){
		int length=0; int start=0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)=='M'){
				start=i+1;
			}
			else if(str.charAt(i)=='I'||str.charAt(i)=='D'){
				length=length+Integer.parseInt(str.substring(start, i)); start=i+1;
			}
		}
		return length;
	}
	public static int gapNumber(String str){
		int number=0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)=='I'||str.charAt(i)=='D'){
				number=number+1;
			}
		}
		return number;
	}
	public static boolean wrongGap(String str){
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)!='M'&&str.charAt(i)!='D'&&str.charAt(i)!='I'&&str.charAt(i)!='1'&&str.charAt(i)!='2'&&str.charAt(i)!='3'&&str.charAt(i)!='4'&&str.charAt(i)!='5'&&str.charAt(i)!='6'&&str.charAt(i)!='7'&&str.charAt(i)!='8'&&str.charAt(i)!='9'&&str.charAt(i)!='0'){
//				System.out.println(str.charAt(i));
				return true;
			}
		}
		return false;
	}
	public static String revertCigar(String cigar) {
		String newCigar = "";
		String operNum = "";
		int start = 0;
		for (int i = 0; i < cigar.length(); i++) {
			if (cigar.charAt(i) == 'M') {
				operNum = cigar.substring(start, i); start = i + 1;
				newCigar = operNum + 'M' + newCigar;
			}
			if (cigar.charAt(i) == 'D') {
				operNum = cigar.substring(start, i); start = i + 1;
				newCigar = operNum + 'I' + newCigar;
			}
			if (cigar.charAt(i) == 'I') {
				operNum = cigar.substring(start, i); start = i + 1;
				newCigar = operNum + 'D' + newCigar;
			}
		}
		return newCigar;
	}

	public static void main (String args[]){
		try{
			long startTime=System.currentTimeMillis();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));	//"SCN_MinimapResult.txt"
			writer.write("index  cigar seg1  seg2  chr1  start1  end1  chr2  start2  end2  Match  Mismatch  nGap  sGap  strand1  strand2"); writer.newLine();

			ArrayList<String> Segs=new ArrayList<String>();   String[] onepair;  int num=0;
			int start1; int end1; int start2; int end2; String cigar; String strand; int mismatches;
			ArrayList<int[]> SegsIndex=new ArrayList<int[]>();  int index=0; int seg1, seg2; int length=0;
			Scanner inSeg=new Scanner(new File(args[0]));  // "AllSegsOfSCN_CRMasked.fasta"
			while(inSeg.hasNextLine()){
				onepair=inSeg.nextLine().trim().split("[\\p{Space}]+");
				int[] temp={Integer.parseInt(onepair[1]), Integer.parseInt(onepair[2]), Integer.parseInt(onepair[3])};
			    SegsIndex.add(temp);
			    Segs.add(inSeg.nextLine());
			}
//			System.out.println("end reading segs!  "+Segs.size());

			Scanner in = new Scanner(new File(args[1]));  in.nextLine(); int p=1; // "AllSegsOfSCN_CRRemoved.info"
			while(in.hasNextLine()){
				//0			1		 2		3		4		5		6		7		8		9		10		11		12				13		14				15		16		17				18		19
				//#cigar  score   name1   strand1 size1   zstart1 end1    name2   strand2 size2   zstart2 end2    identity        idPct   coverage        covPct  nmatch  nmismatch       ngap    cgap
				//7M4D    404624  16908   +       559922  227076  233095  7529    +       448182  44219   50283   5133/5520       93.0%   6064/448182     1.4%    5133    387     58      1043
				//   0		1		2		3		4		5		6		7		8		9	   10     11		12				13				14				15
 				// name1   len1    start1  e1     str      name2   len2    s2      e2     matches all    mq
				//15808   1862    409     1414    -       17551   1928    410     1428    864     1033    0       NM:i:169        ms:i:1058       AS:i:1058       nn:i:0  tp:A:S  cm:i:12 s1:i:118        dv:f:0.1633     cg:Z:41M2I9M2I14M1D6M6D2M6D21M5D20M2D77M4I66M1D63M1D68M2D62M1D161M1D147M1I62M1I5M1D83M1I32M1D26M3I16M1I9M
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); p++;//i++;
				if(!onepair[0].equalsIgnoreCase("#cigar")){
					cigar = onepair[20].replace("cg:Z:", "");
					if(wrongGap(cigar)){
//						System.out.println("wrong cigar line:    "+p);
						num++;
					}
					else{
						seg1=Integer.parseInt(onepair[5]); seg2=Integer.parseInt(onepair[0]);
						start1=Integer.parseInt(onepair[7]); end1=Integer.parseInt(onepair[8]); start2=Integer.parseInt(onepair[2]); end2=Integer.parseInt(onepair[3]);
						strand=onepair[4];
                        if(seg1 != seg2 || start2 > end1 || start1 > end2){
							writer.write(index+"  "+cigar+"  "+seg1+"  "+seg2+"  ");
							if(strand.equalsIgnoreCase("+")){
								System.out.println(SegsIndex.get(seg1)[0]+"  "+(SegsIndex.get(seg1)[1]+recover(Segs.get(seg1), start1))+"  "+(SegsIndex.get(seg1)[1]+recover(Segs.get(seg1),(end1-1))+1)+"  ");
							}
							else{
								length=Integer.parseInt(onepair[6]);
								int rev_start1=length-end1;
								int rev_end1=length-start1;
								writer.write(SegsIndex.get(seg1)[0]+"  "+(SegsIndex.get(seg1)[1]+reverseRecover(Segs.get(seg1),(rev_end1-1)))+"  "+(SegsIndex.get(seg1)[1]+reverseRecover(Segs.get(seg1),rev_start1)+1)+"  ");
							}
							System.out.println(SegsIndex.get(seg2)[0]+"  "+(SegsIndex.get(seg2)[1]+recover(Segs.get(seg2), start2))+"  "+(SegsIndex.get(seg2)[1]+recover(Segs.get(seg2),(end2-1))+1)+"  ");
							mismatches = Integer.parseInt(onepair[12].replace("NM:i:", ""));
							writer.write(onepair[9]+"  "+ (mismatches - gapLength(cigar)) +"  "+gapNumber(cigar)+"  "+gapLength(cigar)+"  +  " + strand);
							writer.newLine();  index++;
//							System.out.println(index);
//							if(index%10000000==0){
//								System.out.println(index+"  "+onepair[2]+"  "+onepair[7]);
//							}
						}
					}

				}
			}
			writer.close(); in.close(); long endTime=System.currentTimeMillis();
//			System.out.println("end!  "+index+"   wrongGap: "+num+"  runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){

		}

	}
}
