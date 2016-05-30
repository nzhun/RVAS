package Process;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Basic.Constant;
import Basic.Individual;
import Util.BuildSimilarity;
import Util.Utils;

public class Selection {
	  int size;
	  public int [] randctrs;
	  public  int[] rankctrs;
	  public int[] randN(int[] array,int s){
		    int index;
		    Random random = new Random();
		    int temp;
		    for(int i=0;i<s;i++){
		    	index=random.nextInt(array.length);
		    	temp=array[i];
		    	array[i]=array[index];
		    	array[index]=temp;
		    }
		    int[] sub=new int[s];
		    for(int i=0;i<s;i++){sub[i]=array[i];}
//		    for(int i=1;i<9;i++){sub[i]=i;}
		    return sub;
	  }  
	  

	  
	  public void  Group(int size,int pool_size){
		     int Indexs[]=Utils.produceArray(pool_size,1);
		     randctrs=randN(Indexs, size); 			 
	  }
	  
	  public void Similarity_Matched_Group(String rankfile, Map<String, Boolean> ind_caseIDs,java.util.Map<String,Integer> have){
		  /*read similarity metrics first,  only cases line required*/
		    BuildSimilarity bs=new BuildSimilarity();
		    List<Individual> caseinds=bs.initialize(rankfile,ind_caseIDs); 
		    
		    java.util.Map<String, Integer> usedms=new  HashMap<String,Integer>(0);
		  //  System.out.print("Info: cases are  ");
		    for(int i=0;i<caseinds.size();i++){
		        String cs=caseinds.get(i).getNum();
		//        System.out.print(cs+", ");
		    	usedms.put(cs, 1);
		    }
	//	    System.out.println(" ");
		    int count=0;
		    int ctrsize=Constant.control_size;
		    rankctrs=new int[ctrsize];
		  
		   // StringBuffer bf=new StringBuffer("");
		    while(count<ctrsize){
			    for(int i=0;i<caseinds.size();i++){
			    	if(count==ctrsize){break;}
			    	java.util.List<String> neighbors=caseinds.get(i).getNeighbors();
			    	for(int j=0;j<neighbors.size();j++){
			    	     String near=neighbors.get(j);
			    	     if(usedms.containsKey(near)){continue;}
			    	     if(!have.containsKey(near)){
			    	    	 System.out.println("Warning: "+ near+" does not exist in control vcf file!");
			    	    	 continue;
			    	    }
			    	     rankctrs[count]=have.get(near);
			    	 //    bf.append(near+", ");
			    	     usedms.put(near,1);
			    	     break;
			    	}	
			      	count++;
			    }
		    
		  }
		 //   System.out.println("Info: controls are "+bf.toString());
		 
	  }
      
}
