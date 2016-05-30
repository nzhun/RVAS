package Basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Parser.VCF_Parser;
import Parser.Weight_Parser;
import Process.DoStatistic;
import Reader.TabixReader;
import Util.Utils;



public class Flow {

	public void workflow(int [] case_columns, int[] ctrl_cols){		
		    String group_file=Constant.getInstance().groupfile;
		    String weight_file=Constant.getInstance().weightfile;
		    String vcf_file=Constant.getInstance().vcf;
		    String ctr_file=Constant.getInstance().controlvcf;
		    BufferedReader br=null;
			TabixReader vtr=null;
			TabixReader ctr_vtr=null;
			TabixReader wtr =null;
			StringBuffer results=new StringBuffer();
			String outf=Constant.getInstance().outfolder+Constant.getInstance().suffix+".pvalues.txt";
//			Utils.write(results.toString(), outf,false);
//			results.append();
			Utils.write("#group_name\tgroup_region"+"\tCATT\tCMC\tPermutation_CATT\tPermutation_CMC\t"
			          +"Weights\tCase_counts\tControl_counts\n", outf,false);
			int pointer=0;
			try {
				vtr = new TabixReader(vcf_file);
				if(!weight_file.equals("")){
				    wtr = new TabixReader(weight_file);
				}
				br = new BufferedReader(new FileReader(group_file));
				int[] columns=case_columns;
				if(ctr_file.equals("")){
					columns=new int[case_columns.length+ctrl_cols.length];
					int i=0;
					for(int col : case_columns){
						columns[i++]=col;
					}
					for(int col : ctrl_cols){
						columns[i++]=col;
					}
				}else{
					ctr_vtr= new TabixReader(ctr_file);
				}
			    String line;
				line = br.readLine();
				while (line != null &&!line.isEmpty()) {
				       //group format, name id1;id2 
					    line=line.replace("\n", "");
					    String[] groupinfo=line.split("\t");
					    String[] groupmarkers=groupinfo[1].split(";");
					    java.util.List<VCF_Parser> variants=new java.util.ArrayList<VCF_Parser>(0);
					    for (String region:groupmarkers){
							TabixReader.Iterator viter = vtr.query(region); // get the iterator
							if(viter==null){continue;}
							TabixReader.Iterator witer = null; // get the iterator
							if(wtr!=null){witer=wtr.query(region);}
							/**How to deal with control and case vcf file**/
							String variant="";
							// only one vcf file
							if(ctr_file.equals("")){
								while (viter != null && (variant = viter.next()) != null){
									VCF_Parser vcf_parser=new VCF_Parser();
									vcf_parser.vcf_line_parser(variant, columns);
									if(vcf_parser.pos==0){
										System.out.println("\tError: "+vcf_file+" is not in VCF format!");
										System.exit(0);
									}
									float weight=findWeight(vcf_parser.pos,witer);
									vcf_parser.weight=weight;	
									variants.add(vcf_parser);
								}
							}else{
								// if one case vcf file one control vcf file
								String ctr_variant="";
								TabixReader.Iterator ctr_viter=ctr_vtr.query(region);
								if(ctr_viter!=null){
								    ctr_variant=ctr_viter.next();
								}
								if(viter!=null){
								    variant=viter.next();
								}
								VCF_Parser ctrvcf_parser=new VCF_Parser();
								VCF_Parser vcf_parser=new VCF_Parser();
								while(ctr_variant!=null && !ctr_variant.equals("") 
										 && variant!=null&& !variant.equals("")){ 
									byte[] each_pos_gt=new byte[case_columns.length+ctrl_cols.length];
									ctrvcf_parser.vcf_line_parser(ctr_variant, ctrl_cols);
									vcf_parser.vcf_line_parser(variant, case_columns);
									
									int pos=ctrvcf_parser.pos<vcf_parser.pos?ctrvcf_parser.pos:vcf_parser.pos;
									float weight=findWeight(pos,witer);
									if(ctrvcf_parser.pos<vcf_parser.pos){
										ctrvcf_parser.weight=weight;
										for(int i=case_columns.length;i<case_columns.length+ctrl_cols.length;i++){
										     each_pos_gt[i]=ctrvcf_parser.genotypes[i-case_columns.length];
										}
										creatVariant(ctrvcf_parser,variants,each_pos_gt);
										ctr_variant=ctr_viter.next();
									}else if(ctrvcf_parser.pos>vcf_parser.pos){
										vcf_parser.weight=weight;
										for(int i=0;i<case_columns.length;i++){
											each_pos_gt[i]=vcf_parser.genotypes[i];
										}
										creatVariant(vcf_parser,variants,each_pos_gt);
										variant=viter.next();
									}else{
										ctrvcf_parser.weight=weight;
										vcf_parser.weight=weight;
										for(int i=0;i<case_columns.length;i++){
											each_pos_gt[i]=vcf_parser.genotypes[i];
										}
										for(int i=case_columns.length;i<case_columns.length+ctrl_cols.length;i++){
											each_pos_gt[i]=ctrvcf_parser.genotypes[i-case_columns.length];
										}
										creatVariant(vcf_parser,variants,each_pos_gt);
										variant=viter.next();
										ctr_variant=ctr_viter.next();
									}
								}
								while(variant!=null &&!variant.equals("")){
									vcf_parser.vcf_line_parser(variant, case_columns);
									vcf_parser.weight=findWeight(vcf_parser.pos,witer);
									byte[] each_pos_gt=new byte[case_columns.length+ctrl_cols.length];
									for(int i=0;i<case_columns.length;i++){
										each_pos_gt[i]=vcf_parser.genotypes[i];
								    }
									creatVariant(vcf_parser,variants,each_pos_gt);
									variant=viter.next();
								}
								
								while(ctr_variant!=null &&!ctr_variant.equals("")){
									ctrvcf_parser.vcf_line_parser(ctr_variant, ctrl_cols);
									ctrvcf_parser.weight=findWeight(ctrvcf_parser.pos,witer);
									byte[] each_pos_gt=new byte[case_columns.length+ctrl_cols.length];
									for(int i=0;i<ctrl_cols.length;i++){
										each_pos_gt[i+case_columns.length]=ctrvcf_parser.genotypes[i];
								    }
									creatVariant(ctrvcf_parser,variants,each_pos_gt);
									ctr_variant=ctr_viter.next();
								}
							}	
							
						
					    }
						/*do statics*/
//					    if(line.contains("TGDS")){
//					    	System.out.print(line);
//					    }
					    if(variants.size()<1){ line = br.readLine();continue;}
						DoStatistic dotests=new DoStatistic(variants);
						dotests.dostatic(case_columns.length, ctrl_cols.length);
						double obv_catt=0;
						double obv_cmc=1;
						double[] permuts=new double[2];
						if(dotests.catt!=null)obv_catt=dotests.catt[0];
						if(dotests.cmc!=null) obv_cmc=dotests.cmc[1];
						if(Constant.getInstance().pertime>1){
							permuts= dotests.permutation(case_columns.length, ctrl_cols.length, obv_catt, obv_cmc);	
						}
						String a=line+"\t"+obv_catt+"\t"+obv_cmc+"\t"+permuts[0]+"\t"+permuts[1]+"\t"
						          +dotests.weights+"\t"+dotests.casecount.toString()+"\t"+dotests.ctrcount.toString()+"\n";
				       results.append(a);
				       pointer++;
//				       System.out.println(a);
//				       String outf=Constant.getInstance().outfolder+Constant.getInstance().suffix+".pvalues.txt";
						if(pointer>100){
				             Utils.write(results.toString(), outf,true);
				             pointer=0;
				             results.setLength(0);
				       }
				        line = br.readLine();
				    }	
				    Utils.write(results.toString(), outf,true);
	               
					br.close();
			} catch (IOException e) {
					e.printStackTrace();
			}
			
		    return ;
	  }
	
	
		private float findWeight(int pos,TabixReader.Iterator witer) {
				float weight=1;
				Weight_Parser weight_parser=new Weight_Parser();
				String weight_info;
				if(witer!=null)
				try {					
					do{
						weight_info = witer.next();
						weight_parser.weight_line_parser(weight_info);
//						wpos=weight_parser.pos;
					}while (weight_parser.pos<pos&&witer!=null);
					if(weight_parser.pos==pos){
						weight=weight_parser.score;
//					   	System.out.println(weight);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		      return weight;
	   }


		private void creatVariant(VCF_Parser vcf_parser,java.util.List<VCF_Parser> variants,byte[] each_pos_gt){
				
				VCF_Parser var=new VCF_Parser();
				var.chr=vcf_parser.chr;
				var.pos=vcf_parser.pos;
				var.alt=vcf_parser.alt;
				var.ref=vcf_parser.ref;
				var.sum=vcf_parser.sum;
				var.weight=vcf_parser.weight;
				var.genotypes=each_pos_gt;
				variants.add(var);
				
	   }
	
}
	
