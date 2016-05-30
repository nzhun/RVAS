package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import net.sf.samtools.util.BlockCompressedInputStream;

public class Utils {
	
	public static BufferedReader openstream(String file) { /**  open stream **/
 	      BufferedReader br =null;
	      try {
 	           if(file.endsWith(".gz")){
	 	           	System.out.println("Notice: please use 'bgzip' to compress vcf file if \""+file+"\" is not!");
//	 	       	 if(OsUtils.isUnix()){}else if(OsUtils.isWindows()){}
	 			    br= new BufferedReader(new InputStreamReader(new BlockCompressedInputStream(new File(file))));
		      }else if(file.endsWith(".vcf")||file.endsWith(".ped")){
				    br = new BufferedReader(new FileReader(file));
	 		 } else{
	 		       System.out.println("Error: The input file "+file+" is not .vcf or .vcf.gz file!");
	 		 }
	    } catch (IOException e) {
			 e.printStackTrace();
	   }
	   return  br;
   }
	
	public static java.util.Map<String , java.util.List<Integer>>  deepcopy( java.util.Map<String , java.util.List<Integer>> map3){
		java.util.Map<String , java.util.List<Integer>> map4=new java.util.HashMap<String, java.util.List<Integer>>();
		Iterator<String> it=map3.keySet().iterator();
		  while(it.hasNext()){
			 String k= it.next();
			 java.util.List<Integer> d=map3.get(k);
			 java.util.List<Integer> c=new java.util.ArrayList<Integer>(0);
			 for(int i=0;i<d.size();i++){c.add(d.get(i));}
			 map4.put(k,c);
		  }
	    return map4;
	}
	
	public static void write(int[] cases, String out) {
		 try {
		      File file = new File(out);
		      BufferedWriter output = new BufferedWriter(new FileWriter(file));
		      output.write("Control\n");
		      for(int i=0;i<cases.length;i++){
		    	  output.write(cases[i]+"\t");
		    	//  System.out.print(cases[i]+"\t");
		      } 
		      output.write("\nRank1\n");	      
		      output.close();
		    } catch ( IOException e ) {
		       e.printStackTrace();
		    }
			
		
	}	
	
	
	public static void write(double d[], String j) {
			
	       try {
	          File file = new File(j);
	          BufferedWriter output = new BufferedWriter(new FileWriter(file));
	          for(int i=0;i<d.length;i++){
	        	  output.write(d[i]+"\n");
	          }
	          output.close();
	        } catch ( IOException e ) {
	           e.printStackTrace();
	        }
			
		}


	 public static void write(String d, String j,boolean a) {
				
		    try {
			      File file = new File(j);
			      BufferedWriter output = new BufferedWriter(new FileWriter(file,a));
			      output.write(d); 
			      output.close();
		    } catch ( IOException e ) {
		       e.printStackTrace();
		    }
				
	  }

  	  public static int[] produceArray(int s,int base){
		
  		  int arr[]=new int[s];
		  for(int i=0;i<s;i++){
			  arr[i]=i+base;
		  }
		  return arr;
	 }
	
	  public static int[] ZeroArray(int s){
		  int arr[]=new int[s];
		  for(int i=0;i<s;i++){
		    	arr[i]=0;
		 }
		 return arr;
	 }


}




