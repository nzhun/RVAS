package Process;

import java.io.File;
import java.io.IOException;

import Basic.Constant;
import Parser.PedParser;
import Parser.VCF_Header_Parser;
import Reader.TabixReader;
import Util.Utils;
public class Case_Control_Groups {
	   public int[] cols;
	   public int[] ctrcols;
	   public StringBuffer header=new StringBuffer();
	   public StringBuffer casenames=new StringBuffer();
	   public StringBuffer controlnames=new StringBuffer();	   

	   public void ID_Index(){
		   String variantfile=Constant.getInstance().vcf;
		   String controlvcf=Constant.getInstance().controlvcf;
		   String[] IDs=Read_IDs(variantfile);
		   
		   if(Constant.getInstance().caseIndexs!=null||!Constant.getInstance().pedFile.equals("")){			  
			   if(!Constant.getInstance().pedFile.equals("")){
				      PedParser pedReader=new PedParser(Constant.getInstance().pedFile);
				      pedReader.readPed();
				      java.util.Iterator<String> itcs=pedReader.caseIDs.keySet().iterator();
				      System.out.print("Notice: cases: ");
				      while(itcs.hasNext()){
				    	  String temp=itcs.next();
				    	  casenames.append(temp+"\t");
				    	  System.out.print(temp+", ");
				      }
				      System.out.println();
				      itcs=pedReader.controlIDs.keySet().iterator();
				      System.out.print("Notice: controls: ");
				      while(itcs.hasNext()){
				    	  String temp=itcs.next();
				    	  controlnames.append(temp+"\t");
				    	  System.out.print(temp+", ");
				      }
				      System.out.println();
				      ID2Index(IDs,pedReader.caseIDs,pedReader.controlIDs);
				     
				      
			   }else{
				      
					   if(Constant.getInstance().caseIndexs==null||Constant.getInstance().controlIndexs==null){
						   
						   System.out.println("Error: "+"please specify cases columns and controls columns!");
						   System.exit(0);
					   }
					   Constant.size=Constant.getInstance().caseIndexs.length;
					   Constant.control_size=Constant.control_size<Constant.getInstance().controlIndexs.length?Constant.control_size:Constant.getInstance().controlIndexs.length;
					   Constant.getInstance().controlvcf="";
					   cols=new int[Constant.size];
					   int i=0;
					   for(int col:Constant.getInstance().caseIndexs){
						   cols[i++]=col;
						   casenames.append(IDs[col-1]+"\t");
					   }
					   ctrcols=new int[Constant.control_size];
					   java.util.List<Integer> list=new java.util.ArrayList<Integer>();
					   for( int j: Constant.getInstance().controlIndexs){
						   if(j<IDs.length+1){list.add(j);}
					   }
					   Constant.getInstance().controlIndexs=new int[list.size()];
					   for(int k=0;k<list.size();k++){Constant.getInstance().controlIndexs[k]=list.get(k);}
					   Selection select=new Selection();
					   /** if rank file is empty**/
					   if(Constant.getInstance().rank.equals("")){
						   int [] randctrs=select.randN(Constant.getInstance().controlIndexs, Constant.control_size); 
//						      select.Group(Constant.control_size,Constant.getInstance().controlIndexs.length);
							   i=0;
							   System.out.print("Random controls: ");
							   for(int col:randctrs){
								   ctrcols[i++]=col;
								   controlnames.append(IDs[col-1]+"\t");
								   System.out.print(IDs[col-1]+", ");
							   }
							   System.out.println();
					   }
					   /** if rank file is not empty**/
					   else{
						   
						   java.util.Map<String,Integer> have=new java.util.HashMap<String, Integer>(0);
//						   int k=1;	
						   for(int p:Constant.getInstance().controlIndexs){
								 have.put(IDs[p-1],p);
//								 k++;
							 }
							 // -rank /media/na/data/projects/RVAS/similarity/similarity_matrix_rare.txt
							 java.util.Map<String,Boolean> Map_IDs=new java.util.HashMap<String, Boolean>(); 
							 System.out.print("Cases: ");
							 for(int id:Constant.getInstance().caseIndexs){
								 Map_IDs.put(IDs[id-1], true);
								 System.out.print(IDs[id-1]+", ");
							 }
							 System.out.println();
							 select.Similarity_Matched_Group(Constant.getInstance().rank, Map_IDs,have);
							 ctrcols=select.rankctrs;
							 System.out.print("Notice: Most Similar controls are ");
							 for(int col:ctrcols){
								 controlnames.append(IDs[col-1]+"\t");
								 System.out.print(IDs[col-1]+", ");
							 }
							 System.out.println();
					   }					  
					   
			  }
			   
		   }else{
			   if(variantfile.equals(controlvcf)){
				   System.out.println("Error: Case and controls are compeletely the same!");
				   System.exit(0);
			   }
			   if(new File(variantfile).exists() &&new File(controlvcf).exists()){
				   Constant.size=IDs.length;
				   cols=Utils.produceArray(Constant.size, 1);
				   String ctrl_IDs[]=Read_IDs(controlvcf);
				   int inds=ctrl_IDs.length;
				   if(Constant.control_size<inds){
						 Selection select=new Selection();
						 if(IDs.length<1||Constant.getInstance().rank.equals("")){
							 System.out.println("Notice: No rank file  was specified, "+Constant.control_size+" controls were randomly choose from "+ controlvcf+"!");
		  				     select.Group(Constant.control_size,inds);
							 ctrcols=select.randctrs;
							 System.out.print("Notice: Random controls are ");
							 for(int col:ctrcols){
								 controlnames.append(ctrl_IDs[col-1]+"\t");
								 System.out.print(ctrl_IDs[col-1]+", ");
							 }
							 System.out.println();
							if(Constant.verbose){
							   Utils.write(ctrcols,Constant.getInstance().outfolder+Constant.control_size+".case-ctr-index.txt");
							}
						 }else{
							 java.util.Map<String,Integer> have=new java.util.HashMap<String, Integer>(0);
							 for(int p=0;p<inds;p++){
								 have.put(ctrl_IDs[p],p+1);
							 }
							 // -rank /media/na/data/projects/RVAS/similarity/similarity_matrix_rare.txt
							 java.util.Map<String,Boolean> Map_IDs=new java.util.HashMap<String, Boolean>(); 
							 for(String id:IDs){
								 Map_IDs.put(id, true);
							 }
							 select.Similarity_Matched_Group(Constant.getInstance().rank, Map_IDs,have);
							 ctrcols=select.rankctrs;
							 System.out.print("Notice: Most Similar controls are ");
							 for(int col:ctrcols){
								 controlnames.append(ctrl_IDs[col-1]+"\t");
								 System.out.print(ctrl_IDs[col-1]+", ");
							 }
							 System.out.println();
						 }
				     }else{
				    	 if(Constant.control_size!=inds){
				    		 System.out.println("Warning: Only "+inds+" controls were involved in this association test");
				    	 }
				    	 Constant.control_size=inds;
				    	 System.out.print("Notice: controls are ");
				    	 for(int i=0;i<ctrl_IDs.length;i++){
				    	   controlnames.append(ctrl_IDs[i]+"\t");
				    	   System.out.print(ctrl_IDs[i]+", ");
				    	 }
				    	
				    	 ctrcols=Utils.produceArray(inds, 1);
				     }
			   }else{
				   System.out.println("Error: Check whether file "+variantfile+" and "+controlvcf+" exist ");
				   System.exit(0);
			   }
		   }
		  
	   }
	  
	  
	   public String[] Read_IDs(String vcf){
		   String[] ids=null;
		   try {
			
			TabixReader vtr = new TabixReader(vcf);
		    /**read header**/
			String line="";
			while (vtr!=null&&(line = vtr.readLine()) != null &&line.startsWith("#")){
			    if(line.startsWith("##")){
					header.append(line);
			    }else{
			    	VCF_Header_Parser vh=new VCF_Header_Parser();
			    	vh.vcf_line_parser(line);
			    	ids=vh.IDs;
			    }
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return ids;
	   }
	  
		 private void ID2Index(String[] IDs, java.util.Map<String,Integer> caseIDs,java.util.Map<String,Integer> controlIDs) {
	           java.util.List<Integer> cases=new java.util.ArrayList<Integer>();
	           java.util.List<Integer> controls=new java.util.ArrayList<Integer>();
			   for(int i=0;i<IDs.length;i++){
					if(caseIDs.containsKey(IDs[i])){
					   cases.add(i);
					}
					if(controlIDs.containsKey(IDs[i])){
					   controls.add(i);
					}
				}
			    cols=new int[cases.size()];
			    ctrcols=new int[controls.size()];
			    int i=0;
			    for(int caseid: cases){
			    	cols[i++]=caseid;
			    }
			    i=0;
			    for(int controlid: controls){
			    	ctrcols[i++]=controlid;
			    }
		}
	  
	  
	 

	 
}
