package Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

import Util.Utils;

public class PedParser {
      String pedfile;
	  public java.util.Map<String,Integer> caseIDs=new java.util.HashMap<String,Integer>(0);
      public java.util.Map<String,Integer> controlIDs=new java.util.HashMap<String,Integer>(0);
      public PedParser(String file){
    	  this.pedfile=file;
      }
     
	  public void readPed(){
		  java.util.Map<String,java.util.List<PedIndividuals>> families=new java.util.HashMap<String, java.util.List<PedIndividuals>>(0);
	      java.util.Map<String,PedIndividuals> peds=new java.util.HashMap<String, PedIndividuals>(0);
	      int[] sexs=new int[6];
	      java.util.Map<String,Integer> ind_sex=new java.util.HashMap<String,Integer>(0);
		  BufferedReader br =Utils.openstream(pedfile);
		  try {
			  String line;
			  Pattern pattern_all=Pattern.compile("\\t");
			  while ((line = br.readLine()) != null) {
				 if(line.charAt(0) =='#'){continue;}
				 if(line.length()<6){continue;}
				 String[] sets=pattern_all.split(line,0); //line.split("\\s+");
				 if(sets[4].toLowerCase().equals("sex")){continue;}
				 int sex=0;
				 int phenotype=0;
				 if(!sets[4].equals("other")){
					 if(!sets[4].equals("1")&&!sets[4].equals("2")){
						 System.out.println("Error: sex should be coded as 1=male; 2=female; other=unknown!");
						 System.exit(0);
					 }else{
						 sex=Integer.parseInt(sets[4]); 
					 }
				 }
				 if(ind_sex.containsKey(sets[1])){
					 int lastsex=ind_sex.get(sets[1]);
					 if(lastsex!=sex){ 
						 if(lastsex+sex!=3){
							 sex=sex>lastsex?sex:lastsex;
							 System.out.println("Warning: overwrite "+sets[1]+"'s gender as "+sex+"!");
						 }
						 else{
					  	    System.out.println("Warning: "+sets[1]+" has two genders!");
						    System.out.println();
					     }
					}
				 }
				 if(sets[5].equals("0")||sets[5].equals("1")||sets[5].equals("2")){
					 phenotype=Integer.parseInt(sets[5]);
				 }else{
					 System.out.println("Error: phenotype should be coded as 0=missing; 1=unaffected; 2=affected!");
					 System.exit(0);
				 }
				 
				 PedIndividuals pedindividual=new PedIndividuals();
				 pedindividual.family=sets[0];
				 pedindividual.self=sets[1];
				 pedindividual.father=sets[2];
				 pedindividual.mother=sets[3];				
				 pedindividual.sex=sex;
				 pedindividual.phenotype=phenotype;
				 peds.put(sets[1],pedindividual);
				 if(phenotype==2){
					 caseIDs.put(sets[1],0);
					 sexs[sex]+=1;
				 }else if(phenotype==1){
					 controlIDs.put(sets[1], 0);
					 sexs[sex+3]+=1;
				 }
				 
				 if(ind_sex.containsKey(sets[2])){
					 int father_sex=ind_sex.get(sets[2]);
				     if(father_sex<1){
				    	 ind_sex.put(sets[2],1);
				    	 if(peds.containsKey(sets[2])){peds.get(sets[2]).sex=1;}
				     }else if(father_sex>1){
				    	 System.out.println("Error:"+ sets[2]+" has two genders! Please check Ped file!");
				    	 System.exit(0);
				   }
				  }
				 

				 if(ind_sex.containsKey(sets[3])){
					 int mother_sex=ind_sex.get(sets[3]);
				     if(mother_sex<1){
				    	 ind_sex.put(sets[3],2);
				    	 if(peds.containsKey(sets[3])){peds.get(sets[3]).sex=1;}
				     }else if(mother_sex<2){
				    	 System.out.println("Error: "+sets[3]+" has two genders! Please check Ped file!");
				    	 System.exit(0);
				     }
				  }
				 
				 java.util.List<PedIndividuals> list=new java.util.ArrayList<PedIndividuals>();
				 if(families.containsKey(sets[0])){
				     list=families.get(sets[0]);
				 }
				 list.add(pedindividual);
				 families.put(sets[0], list);
				 
			}
			java.util.Iterator<String> family_iterator=families.keySet().iterator();
			while(family_iterator.hasNext()){
				String spec_fam=family_iterator.next();
				java.util.List<PedIndividuals> pedlist=families.get(spec_fam);
				if(pedlist.size()>2){
					System.out.println("\033[31m RED"+" Warning: "+spec_fam+" has "+ pedlist.size()+" individuals in the ped files."
							+ " But Association test required unrelated case and control individuals!");
					for(PedIndividuals ped : pedlist){
						System.out.println(ped.family+"\t"+ped.self+"\t"+ped.father+"\t"+ped.mother);
					}
				}
			}
			
			System.out.println("Notice: it contains "+ caseIDs.size()+" cases and "+controlIDs.size()+" controls!");
			System.out.println("Notice: cases: "+ sexs[0]+ " unknown gender; " +sexs[1]+" male and "+sexs[2]+" female!");
			System.out.println("Notice: controls: "+ sexs[3]+ " unknown gender; " +sexs[4]+" male and "+sexs[5]+" female!");  
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		  
	  }
	  
	  class PedIndividuals{
		  String family="";
		  String father="";
		  String mother="";
		  String self="";
		  int sex=0;
		  int phenotype=0;
	  }
}
