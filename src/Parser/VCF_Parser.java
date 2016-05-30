package Parser;


import java.util.regex.Pattern;


public class VCF_Parser {
	public String chr="";
	public int pos=0;
	public String ref;
	public String alt;
	public int sum;
	public float  weight;
	public byte[] genotypes;
	
	public void vcf_line_parser(String line,int [] cols) {
	    Pattern pattern_all=Pattern.compile("\\t");
	    String[] sets=pattern_all.split(line,0);
	    int FIX_H=9;
	    if(sets.length<FIX_H){
//	    	System.out.println("\tError:");
	        return ;
	    }
		Pattern pattern_gt=Pattern.compile("[|/]");
		Pattern pattern_gtcol=Pattern.compile(":");
		chr=sets[0];
		try{
	        pos=Integer.parseInt(sets[1]);
		} catch (NumberFormatException e) {
			System.out.println(sets[1] +" is not numberic");
			e.printStackTrace();
		}
		ref=sets[3];
	    alt=sets[4];	    
		int fieldcol=0;
		String gts[]=pattern_gtcol.split(sets[FIX_H-1]);
		for(String part_gts:gts){
		   if(part_gts.equals("GT")){break;}
		   fieldcol++;
		}
		int index=0;
		genotypes=new byte[cols.length];
		for(int col:cols){/* cols start from 1 **/
			int i=col-1+FIX_H;
			if(i>sets.length){System.out.println("Error: there are not enough controls!");break;}
			if(i>sets.length-1){
				System.out.println( sets[i]);
			}
			String infos[]=pattern_gtcol.split(sets[i]);
		  	if(!infos[fieldcol].equals("-")&&!infos[fieldcol].equals(".")&&
		  			  !infos[fieldcol].equals("./.")&&!infos[fieldcol].equals(".|.")){
		  		   String alleles[]=pattern_gt.split(infos[fieldcol]);
		  		   genotypes[index]=(byte)0;
		  		   if(alleles.length<2){
		  			   System.out.println("Warning: Genotype at "+sets[0]+"\t"+sets[1]+ " for individual "+col+" is not in right format! it is ignored");
//		  			   genotypes[index]=9;
		  			   genotypes[index]=0;
		  			   continue;
		  		   }
		  		   if(alleles[0].equals(alleles[1])){
		  		        if(!alleles[0].equals("0")){
		  		          	 genotypes[index]=2;
		  		          	 sum=sum+1;
		  		         }
		  		   }else{
		  			    genotypes[index]=1;
		  			   	sum=sum+1;		  			    
		  		  }			   
			  }else{
//				   	genotypes[index]=9;
					genotypes[index]=0;
			  }
		  	  index++;
		}
}
	
	
//	public void vcf_line_parser(String line,int [] cols) {
//		    Pattern pattern_all=Pattern.compile("\\t");
//		    String[] sets=pattern_all.split(line,0);
//		    Pattern pattern_alt=Pattern.compile(",");
//		    int FIX_H=9;
//			Pattern pattern_gt=Pattern.compile("[|/]");
//			Pattern pattern_gtcol=Pattern.compile(":");
//			chr=sets[0];
//			try{
//		        pos=Integer.parseInt(sets[1]);
//			} catch (NumberFormatException e) {
//				System.out.println(sets[1] +" is not numberic");
//				e.printStackTrace();
//			}
//			ref=sets[3];
//		    alt=sets[4];
//		    alts=pattern_alt.split(alt);		    
//			int fieldcol=0;
//			String gts[]=pattern_gtcol.split(sets[FIX_H-1]);
//			for(String part_gts:gts){
//			   if(part_gts.equals("GT")){break;}
//			   fieldcol++;
//			}
//			int index=0;
//			for(int col:cols){/* cols start from 1 **/
//				int i=col-1+FIX_H;
//				if(i>sets.length){System.out.println("Error: there are not enough controls!");break;}
//				if(i>sets.length-1){
//					System.out.println( sets[i]);
//				}
//				String infos[]=pattern_gtcol.split(sets[i]);
////				gtstr.append(sets[i]+"\t");
//			  	if(!infos[fieldcol].equals("-")&&!infos[fieldcol].equals(".")&&
//			  			  !infos[fieldcol].equals("./.")&&!infos[fieldcol].equals(".|.")){
//			  		   String alleles[]=pattern_gt.split(infos[fieldcol]);
//			  		   for(int j=0;j<alts.length;j++){
//			  			   genotypes[j][index]=(byte)0;
//			  		   }
//			  		   if(alleles.length<2){
//			  			   System.out.println("Warning: Genotype at "+sets[0]+"\t"+sets[1]+ " for individual "+col+" is not in right format! it is ignored");
//			  			   for(int j=0;j<alts.length;j++)
//			  				 genotypes[j][index]=9;
//			  			   continue;
//			  		   }
//			  		   if(alleles[0].equals(alleles[1])){
//			  		        if(!alleles[0].equals("0")){
//			  		       	     int row=Integer.parseInt(alleles[0]);
//			  		          	 genotypes[row-1][index]=2;
//			  		         }
//			  		   }else{
//			  			    int row1=Integer.parseInt(alleles[0]);
//			  			    int row2=Integer.parseInt(alleles[1]);
//			  			    if(row1==0){ genotypes[row2-1][index]=1;}
//			  			    else if(row2==0){genotypes[row1-1][index]=1;}
//			  			    else {genotypes[row1-1][index]=1;genotypes[row2-1][index]=1;}/**not right**/
//			  		  }			   
//				  }else{
//					      for(int j=0;j<alts.length;j++)
//					    	  genotypes[j][index]=9;
//				  }
//			  	index++;
//			}
////			return genotypes;
//	}
}
