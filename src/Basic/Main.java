package Basic;

import java.io.File;

import Process.Case_Control_Groups;
public class Main {
	public static String help="Usage:"+
                "java -jar rvat.jar [OPTIONS]\n\n"+
	  	       "required parameters:\n"+	  	               
	  	               "\t-vcf cases vcf file (.vcf or.gz by bgzip)\n"+
	  	               "\t-o output folder\n\n"+
	  	       "optional parameters\n"+
			           "\t-controlvcf controls vcf(.gz by bgzip), default data/controls.vcf.gz from 1000 Genomes project\n "+
	  	                "\t-ped ped file, foramt follows: http://pngu.mgh.harvard.edu/~purcell/plink/data.shtml\n "+
			           "\t-casecols case columns(delimited by comma, e.g. 1,2,3) in multiple vcf file\n"+
	  	                "\t-controlcols control columns(delimited by comma, e.g. 1,2,3) in multiple vcf file\n"+
	  	               "\t-rank similarity matrix, default randomly choose controls\n" +
			           "\t-prefix prefix for output files, default Permutation.\n"+
	  	               "\t-model 1\n"+
			           "\t-permutation, permutation times default 1000\n"+
	  	               "\t-verbose [0,1], whether print other detail\n"+
	  	               "\t-size control group size, default 10\n"+
			           "\t-model set Cochran-Armitage trend test model,\n"+
					   "\t\t model 0: dominant model, weights=(0,1,1)\n"+
			           "\t\t model 1: additive model, weights=(0,1,2)\n"+
					   "\t\t model 2: recessive model, weights=(0,0,1)\n"+
			           "\n";
  public static int input(String[] args){
  	  if(args.length<2){
  		  return 0;
  	 }  
	  if(args[0].equals("-H")||args[0].equals("-h")||args[0].equals("-help")){
		   return 0;
		}else{
			String output="";
			String vcf="";
			String controlvcf="";
			String rank="";
			String ped="";
			String groupf="";
			String weightf="";
			java.util.Set<Integer> colsets=new java.util.HashSet<Integer>();
			for(int i=0;i<args.length;i++){
				 if(args[i].equals("-vcf")){
					vcf=args[i+1];
					if(!new File(vcf).exists()){System.out.println("Error: vcf file:  "+vcf+" can not find !\n");return 0;}
					i++;
				}else if(args[i].equals("-controlvcf")){
						controlvcf=args[i+1];
						if(!new File(controlvcf).exists()){System.out.println("Error: vcf file:  "+controlvcf+" can not find !\n");return 0;}
						i++;
			   }else if(args[i].equals("-ped")){
					ped=args[i+1];
					if(!new File(ped).exists()){System.out.println("Error: ped file:  "+ped+" can not find !\n");return 0;}
					i++;
		      }else if(args[i].equals("-group")){
					groupf=args[i+1];
					if(!new File(groupf).exists()){System.out.println("Error: group file:  "+groupf+" can not find !\n");return 0;}
					i++;
		      }else if(args[i].equals("-weight")){
					weightf=args[i+1];
					if(!new File(weightf).exists()){System.out.println("Error: weight file:  "+weightf+" can not find !\n");return 0;}
					i++;
		      }else if(args[i].equals("-casecols")){
		    	  if(!args[i+1].matches("(\\d+([\\,-])?)+")){System.out.println("Error: case columns are positive integer and  delimited by \",\" \n");return 0;}
					String[] sizestr= args[i+1].split(",");
					java.util.List<Integer> casecols=new java.util.ArrayList<Integer>(0);
					//Constant.getInstance().controlIndexs=new int[sizestr.length];
					for(int j=0;j<sizestr.length;j++){
						if(sizestr[j].matches("\\d+(\\s)?-(\\s)?\\d+")){
							 String[] seq= sizestr[j].split("-");
					         int beg=Integer.parseInt(seq[0]);
					         int end=Integer.parseInt(seq[1]);
					         if(beg>end){
					        	 System.out.println("Error: begin position is behind of end position! please check the case columns input!");
					        	 System.exit(0);
					         }
					         for(int s=beg;s<end+1;s++){
					        	// casecols.add(s);
					        	 int sizep=colsets.size();
								 colsets.add(s);
								 int sizeb=colsets.size();
								 if(sizeb==sizep){
										System.out.println("Warning: in casecols "+s+" has been used before!");
								 }else{casecols.add(s);}
					         }
						}
						else{
							int s=Integer.parseInt(sizestr[j]);
							
							int sizep=colsets.size();
							colsets.add(s);
							int sizeb=colsets.size();
							if(sizeb==sizep){
								System.out.println("Warning: in casecols "+s+" has been used before!");
							}else{casecols.add(s);}
						}
					}	
					Constant.getInstance().caseIndexs=new int[casecols.size()];
					int pointer=0;
				//	System.out.print("Info; cases  ");
					for(int s:casecols){
						Constant.getInstance().caseIndexs[pointer++]=s;
					//	System.out.print(s+", ");
					}
				//	System.out.println();
				
					i++;
		        }else if(args[i].equals("-controlcols")){
		    	  if(!args[i+1].matches("(\\d+([\\,-])?)+")){System.out.println("Error: control columns are positive integer and  delimited by \",\" \n");return 0;}
					String[] sizestr= args[i+1].split(",");
					java.util.List<Integer> controlcols=new java.util.ArrayList<Integer>(0);
					//Constant.getInstance().controlIndexs=new int[sizestr.length];
					for(int j=0;j<sizestr.length;j++){
						if(sizestr[j].matches("\\d+(\\s)?-(\\s)?\\d+")){
							 String[] seq= sizestr[j].split("-");
					         int beg=Integer.parseInt(seq[0]);
					         int end=Integer.parseInt(seq[1]);
					         if(beg>end){
					        	 System.out.println("Error: begin position is behind of end position! please check the control columns input!");
					        	 System.exit(0);
					         }
					        
					         for(int s=beg;s<end+1;s++){
					        	// controlcols.add(s);
					        	 int sizep=colsets.size();
								 colsets.add(s);
								 int sizeb=colsets.size();
								if(sizeb==sizep){
									System.out.println("Warning: in controlcols "+s+" has been used before!");
								}else{
									controlcols.add(s);
								}
					        	
					         }
						}
						else{
							int s=Integer.parseInt(sizestr[j]);
						   	 int sizep=colsets.size();
							 colsets.add(s);
							 int sizeb=colsets.size();
						  	 if(sizeb==sizep){
								System.out.println("Warning: in controlcols "+s+" has been used before!");
							 }else{
								controlcols.add(s);
							 }
						}
					}	
					Constant.getInstance().controlIndexs=new int[controlcols.size()];
					int pointer=0;;
					for(int s:controlcols){
						Constant.getInstance().controlIndexs[pointer++]=s;
					}
					i++;
		        }else if(args[i].equals("-rank")){
			   		rank=args[i+1];
					i++;
				}else if(args[i].equals("-o")){
					output=args[i+1];
					if(!new File(output).exists()){System.out.println("Error:  folder "+output+" can not find !\n");return 0;}
					i++;
				}else if(args[i].equals("-prefix")){
					 Constant.getInstance().suffix=args[i+1];
					 i++;
				}else if(args[i].equals("-permutation")){
					if(!args[i+1].matches("\\d+")){System.out.println("Error:  permutation times must be a positive integer!\n");return 0;}
					Constant.getInstance().pertime=Integer.parseInt(args[i+1]);
//					System.out.println(args[i+1]+" permutation times,");
					i++;
				}else if(args[i].equals("-size")){
					if(!args[i+1].matches("\\d+")){System.out.println(" Error:  Control group size must be a positive integer!\n");return 0;}
					Constant.control_size=Integer.parseInt(args[i+1]);
//					System.out.println(args[i+1]+" permutation times,");
					i++;
				}else if(args[i].equals("-model")){
					if(!args[i+1].matches("\\d+")){System.out.println(" Error:  model must be a 0,1 or 2!\n");return 0;}
					Constant.getInstance().model=Integer.parseInt(args[i+1]);
					i++;
				}else if(args[i].equals("-verbose")){
					if(!args[i+1].matches("\\d+")){System.out.println(" Error: it must be 0 or 1!\n");return 0;}
					Constant.getInstance();
					Constant.verbose=Integer.parseInt(args[i+1])>0?true:false;
					i++;
				}else{
					System.out.println("Error: "+args[i] +" is unrecognized!\n");
					System.out.println(help);
					System.exit(0);
					return 0;
			    }
			}
			if(vcf.equals("")||output.equals("")){System.out.println("Error:  "+vcf+" or "+output+" can be empty!");return 0;}
			Constant.getInstance().setFolder( output,vcf,controlvcf,rank,ped,groupf,weightf);
		}
		  return 1; 
	   }  
	  
	
	
	public static void main(String args[]){
		    long startTime = System.currentTimeMillis();
		    if(input(args)==0){
		    	System.out.println(help);
		    	System.exit(0);
		    }
		    else{
		      Constant.getInstance().print();
		    }
		    Case_Control_Groups case_control_groups=new Case_Control_Groups();
		    case_control_groups.ID_Index();
		   	new Flow().workflow(case_control_groups.cols,case_control_groups.ctrcols);
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Notice: elapsed time "+(totalTime/1000)/60 +" mins "+(totalTime/1000)%60+" secs.");
	 }

}


