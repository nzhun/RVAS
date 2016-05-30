package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import Basic.Individual;

public class BuildSimilarity {
	  java.util.List<String> colIDs=new java.util.ArrayList<String>();
	  java.util.List<String> controlIDs=new java.util.ArrayList<String>();
	  public  java.util.List<Individual> initialize(String input, java.util.Map<String,Boolean> caseids){
		  File f=new File(input);
		  if(!f.exists()){
			  System.out.println("Error: "+input +" does not exist!");
		      System.exit(0);
		  }
		  java.util.List<Individual> inds=new java.util.ArrayList<Individual>(0);
//		  java.util.List<Individual2> inds2=new java.util.ArrayList<Individual2>(0);
		  try {
			    Pattern pattern=Pattern.compile("\\t");
			  //  Pattern pat2=Pattern.compile("^[0-9].*");
				BufferedReader br = null; 
				String line;
				br = new BufferedReader(new FileReader(input));
				int linec=0;
//				int pointer=0;
				int[] cols=new int[caseids.size()];
//				java.util.List<java.util.List<Float>> metric1=new java.util.ArrayList<java.util.List<Float>>();
				float metric[][]=new float[cols.length][25350];
				while ((line = br.readLine()) != null) {
//					java.util.List<Float> mf=new java.util.ArrayList<Float>();
					String[] sets=pattern.split(line,0);
					if(linec==0){
						int k=0;
					    for(int j=0;j<sets.length;j++){
					    	if(caseids.containsKey(sets[j])){
					    		cols[k++]=j;
					    		colIDs.add(sets[j]);
					    	}
					    	
					    }
					    if(k==0){
					    	  System.out.println("Error: No cases ID was found in Similarity metrics file!"); 
					          System.exit(0);
					     }
					    linec++;
						continue;
						
					}
					controlIDs.add(sets[0]);
					for(int i=0;i<cols.length;i++){
						if(isnumber(sets[cols[i]])){
						   metric[i][linec-1]=Float.parseFloat(sets[cols[i]]);
						}else{
							System.out.println("Error: in similarity matrix file "+input+" (line, row) ("+(linec)+", "+i+") is not number.");
							System.exit(0);
						}
					}
					linec++;		
			}
			br.close();
			
			 for(int i=0;i<cols.length;i++){
				  float curdistance[]=new float[linec-1];
				  for(int k=0;k<linec-1;k++){
					  curdistance[k]=metric[i][k];
				  }
				  int[] ranks=convertRank(curdistance);
				  Individual ind=new Individual();
				  ind.setNum(colIDs.get(i));
				  for(int j=1;j<ranks.length;j++){
						ind.addneighbor(controlIDs.get(ranks[j]));
				  }
				  inds.add(ind);
			  }
	         
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		  return inds;
	  }		
	    
	   private static boolean isnumber(String str){
	      try {
		           Float.parseFloat(str);
		           return true;
		  } catch(NumberFormatException e) {
		           return false;
		  }
	   }
	   
	  
	  	private static int[] convertRank(float[] distances) {
		 
		    int[] ranks=new int[distances.length];
		    int[] orders=new int[ranks.length];
		    for(int i=0;i<distances.length;i++){
		    	int k=orders[i];
		    	for(int j=i+1;j<distances.length;j++){
		    		if(distances[i]>distances[j]){k++;}
		    		else{orders[j]++;}
		    	}
		    	ranks[k]=i;
		    }
	  		return ranks;
	  	}
	  

}
