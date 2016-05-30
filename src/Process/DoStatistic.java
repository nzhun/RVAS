package Process;

import java.util.Arrays;
import java.util.Iterator;

import org.ejml.simple.SimpleMatrix;

import Basic.Constant;
import Parser.VCF_Parser;
import Statistic.CAT_Test;
import Statistic.CMC;
import Statistic.Permutation_Lib;
import Util.Utils;

public class DoStatistic {
	java.util.List<VCF_Parser> variants;
	public double catt[];
	public double cmc[];
	public StringBuffer ctrcount=new StringBuffer();
	public StringBuffer casecount=new StringBuffer();
	public StringBuffer weights=new StringBuffer();
//	public double permutation[];
	
	
	public DoStatistic(java.util.List<VCF_Parser> variants){
		this.variants=variants;
	}
	
	
	
    public void dostatic(int casesize,int ctrsize){
    	
		java.util.List<float[]> gt_list=new java.util.ArrayList<float[]>(0);
	
		double Alltable[]=new double[6];
		// only one vcf file
		Iterator<VCF_Parser> it=variants.iterator();
		while(it.hasNext()){	
			    VCF_Parser vcf_parser=it.next();
			
				float[] each_pos_gt=new float[vcf_parser.genotypes.length];
				int sumcase=0;
				int sumctr=0;
				int j=0;
				double table[]=new double[6];
				for(int i : vcf_parser.genotypes){
						each_pos_gt[j]=i*vcf_parser.weight;
						if(j<casesize){
							if(i<3){
							  sumcase+=i>0?1:0;
							  table[i]+=1;
							}
						}else{
							if(i<3){
							    sumctr+=i>0?1:0;
								table[i+3]+=1;
							}
						}
						j++;	
				}
				for(int i=0;i<table.length;i++){
					Alltable[i]+=table[i]*vcf_parser.weight;
				}
				weights.append(vcf_parser.weight+",");
				casecount.append(sumcase+",");
				ctrcount.append(sumctr+",");
				gt_list.add(each_pos_gt );	
		}	
	
		/*each bin test*/
		/*CATT test*/
		 catt=new CAT_Test().call_CAT(Alltable,Constant.getInstance().model);
		
		/*CMC test*/
		
		SimpleMatrix casemat=new SimpleMatrix(casesize,gt_list.size());
		SimpleMatrix controlmat=new SimpleMatrix(ctrsize,gt_list.size());
		java.util.Iterator<float[]> gt_it= gt_list.iterator();
		int k=0;
		while(gt_it.hasNext()){
			float[] gts=gt_it.next();
			for(int i=0;i<casesize;i++){
				casemat.set(i,k,gts[i]);
			}
			for(int i=0;i<ctrsize;i++){
				controlmat.set(i,k,gts[i+casesize]);
			}
			k++;
		}
		
		cmc=new CMC().hotellingT2(casemat,controlmat);
		
    }
    
    
       public void dostatic(int[] indexs,int casesize,int ctrsize){
    	
				java.util.List<float[]> gt_list=new java.util.ArrayList<float[]>(0);
				StringBuffer ctrcount=new StringBuffer();
				StringBuffer casecount=new StringBuffer();
				StringBuffer weights=new StringBuffer();
				double Alltable[]=new double[6];
				// only one vcf file
				Iterator<VCF_Parser> it=variants.iterator();
				while(it.hasNext()){	
					    VCF_Parser vcf_parser=it.next();
					    double table[]=new double[6];
						float[] each_pos_gt=new float[vcf_parser.genotypes.length];
						int sumcase=0;
						int sumctr=0;
						int j=0;
						for(int k : indexs){
							    int i=vcf_parser.genotypes[k];
								each_pos_gt[j++]=i*vcf_parser.weight;
								if(j<casesize){
									if(i<3){
										sumcase+=i>0?1:0;
										table[i]+=1;
									}
								}else{
									if(i<3){
										sumctr+=i>0?1:0;
									    table[i+3]+=1;
									}
								}
								
						}
						for(int i=0;i<table.length;i++){
							Alltable[i]+=table[i]*vcf_parser.weight;
						}
						weights.append(vcf_parser.weight+",");
						casecount.append(sumcase+",");
						ctrcount.append(sumctr+",");
						gt_list.add(each_pos_gt );	
				}	
			
				/*each bin test*/
				/*CATT test*/
				 catt=new CAT_Test().call_CAT(Alltable,Constant.getInstance().model);
				
				/*CMC test*/
				
				SimpleMatrix casemat=new SimpleMatrix(casesize,gt_list.size());
				SimpleMatrix controlmat=new SimpleMatrix(ctrsize,gt_list.size());
				java.util.Iterator<float[]> gt_it= gt_list.iterator();
				int k=0;
				while(gt_it.hasNext()){
					float[] gts=gt_it.next();
					for(int i=0;i<casesize;i++){
						casemat.set(i,k,gts[i]);
					}
					for(int i=0;i<ctrsize;i++){
						controlmat.set(i,k,gts[i+casesize]);
					}
					k++;
				}
				
				cmc=new CMC().hotellingT2(casemat,controlmat);
		
    }
    
 
 	/*In Sham method*/
    public double[] permutation(int casesize,int ctrsize,double obv_catt,double obv_cmc){
    	
		    	 int total=Constant.getInstance().pertime;		
		 		/********************Permutation starts*****************************/
		 		 if(total<1){return null;}
		 		 int[] allcases=Utils.produceArray(casesize,0);
				 int[] controls=Utils.produceArray(ctrsize,casesize);
		 		 double[] maxs;
		 		 double[] minx;
		 		 if((ctrsize+casesize)>15){
		 					minx=new double[total];
		 					maxs=new double[total];
		 					/*************produce new permutated case control group: Monto carlo *******************/
		 					for(int i=0;i<total;i++){
		 						int[] neworder=new Permutation_Lib().randomLable(casesize,ctrsize,allcases,controls);
		 						dostatic(neworder,casesize,ctrsize);
		 						maxs[i]=catt[0];
		 						minx[i]=cmc[1];
		 					}
		 		  }else{
		 			       /**List all permutations**/
		 				    java.util.List<int[]> permutations=new Permutation_Lib().allPermutations(allcases,controls);
		 				    total=permutations.size();
		 					minx=new double[total];
		 					maxs=new double[total];
		 					int i=0;
		 				    for(int[] neworder :permutations){
		 					   /* this is an index array*/
		 					   dostatic(neworder,casesize,ctrsize);
		 					   maxs[i]=catt[0];
							   minx[i++]=cmc[1];
		 				    }
		 		  }
		 			
		 		  Arrays.sort(maxs);
		 		  Arrays.sort(minx);
		 		  int cmc_time=0;
				  for(int i=0;i<minx.length;i++){
						cmc_time=i;
						if(obv_cmc<minx[i]){break;} /**/	
				  }
				  double permut_cmc=(cmc_time+1)/(double)(total+1);
				   int cat_time=0;
				   for(int i=0;i<maxs.length;i++){
					cat_time=i;
		//			if(catT<minx[i]){break;}  //with equal
					if(obv_catt<maxs[i]||obv_catt==maxs[i]){break;}  
				   }
				  double permut_catt=(double)(total-cat_time+1)/(double)(total+1);
		 		  return new double[]{permut_catt,permut_cmc};
    }
}
