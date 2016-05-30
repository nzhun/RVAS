package Statistic;

import java.util.Random;

public class Permutation_Lib {
	public java.util.List<int[]> allPermutations(int cases[], int controls[]){ /*int[] cases=Utils.produceArray(15, 0);int[] controls=Utils.produceArray(15, 15);*/
		java.util.List<int[]> cbs=new java.util.ArrayList<int[]>();
		int len=cases.length<controls.length?cases.length:controls.length;
		for(int k=1;k<len+1;k++){
			java.util.List<int[]> cbst= permut(cases,controls,k);	
		   cbs.addAll(cbst);
		}
		return cbs;	
	}
	
	static java.util.List<java.util.Set<Integer>> sum_up_recursive(java.util.List<Integer> numbers,java.util.Set<Integer> partial , int sizeOfset) {
		java.util.List<java.util.Set<Integer>> result = new java.util.ArrayList<java.util.Set<Integer>>();
	    if(partial.size() == sizeOfset){result.add(partial);}
	    for (int i = 0; i < numbers.size(); i++) {
	    	java.util.ArrayList<Integer> remaining = new java.util.ArrayList<Integer>();
	        int n = numbers.get(i);
	        for (int j = i + 1; j < numbers.size(); j++){remaining.add(numbers.get(j));}
	        java.util.Set<Integer> partial_rec = new java.util.HashSet<Integer>(partial);
	        partial_rec.add(n);
	        result.addAll(sum_up_recursive(remaining, partial_rec, sizeOfset));
	    }
	    return result;
	}

	static java.util.List<int[]> permut(int a[],int b[],int c){
		java.util.List<int[]>  cb=new java.util.ArrayList<int[]>();
		  Integer[] tempa=new Integer[a.length];
		  Integer[] tempb=new Integer[b.length];
		  int ct=0;
		  for(int i:a){tempa[ct++]=i;}
		  ct=0;
		  for(int i:b){tempb[ct++]=i;}
		  java.util.List<java.util.Set<Integer>> allCombinations = sum_up_recursive(java.util.Arrays.asList(tempa), new java.util.HashSet<Integer>(), c);		 
		  java.util.List<java.util.Set<Integer>> b_allCombinations = sum_up_recursive(java.util.Arrays.asList(tempb), new java.util.HashSet<Integer>(), c);
		  for (java.util.Set<Integer> set : allCombinations) {
		    	int [] tema=a.clone();
		    	int cba[]=new int[a.length+b.length];
		    	int pt=0;
			    for (Integer num : set){
			       	cba[a.length+pt++]=num;
			       	for(int k=0;k<tema.length;k++){if(tema[k]==num){tema[k]=-1;}}
			    } 
			    for (java.util.Set<Integer> set2 : b_allCombinations) {
			    	int[] cpcba=cba.clone();
			       	int pt1=0;
			       	int pt2=pt;
			       	for(int k=0;k<tema.length;k++){if(tema[k]!=-1){cpcba[pt1++]=tema[k];}}
			       	int[] temb=b.clone();
		  	        for (Integer num2 : set2){
		  	        	cpcba[pt1++]=num2;
		  	            for(int k=0;k<temb.length;k++){if(temb[k]==num2){temb[k]=-1;}}
		  		    }
		  		    for(int k=0;k<temb.length;k++){
		  		        if(temb[k]!=-1){
		  		        	cpcba[a.length+pt2++]=temb[k];
		  		    	}
		  		    }
		  		    cb.add(cpcba);
		  	  }	        
		 } 
		 return cb;
	}
	
	
	public  int[] randomLable(int size,int ctrsize,int[] initcases,int[] initcontrols){
		int[] all=new int[size+ctrsize];
		for(int i=0;i<size;i++){ all[i]=initcases[i];  }
		for(int i=0;i<ctrsize;i++){	 all[i]=initcontrols[i];}
		int total=size+ctrsize;
		for(int i=0;i<size;i++){
			  int rand=new Random().nextInt(total);
			  if(rand>size-1){
			     int temp=all[i];
			     all[i]=all[rand];
			     all[rand]=temp;
			  }  
		}
		return all;
	}
}
