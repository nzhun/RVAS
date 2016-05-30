package Statistic;

import java.math.BigDecimal;

import jdistlib.ChiSquare;
import Basic.Constant;

public class CAT_Test {	
	int[] casetab;
	int[] controltab;
	public double[] tests;
	double max=0;
	int index=0;
	
	
	public  double[] call_CAT(double []tab,int model){      
	        int row=2;
	        int col=3;
	        int[] weights=getWeight(model);
		    for(int k=0;k<tab.length;k++){tab[k]+=Constant.getInstance().tol;}
	        double sumrow[]=new double[]{0D,0D};
	        double sumcol[]=new double[]{0D,0D,0D};
	        double sum=0D;
	        for(int i=0;i<row;i++){
	        	for(int j=0;j<col;j++){
	        		sumrow[i]+=tab[i*col+j];
	        		sumcol[j]+=tab[i*col+j];
	        		sum+=tab[i*col+j];
	        	}	
	        }
	       
	        double xhat=0D;
	        double p[]=new double[3];
	        for(int j=0;j<col;j++){
	        	xhat+=sumcol[j]*weights[j];
	        	p[j]=tab[j]/sumcol[j];
	        }
	        xhat=xhat/sum;
	        double prop=sumrow[0]/sum;
	        double bp1=0D,bp2=0D;
	        for(int j=0;j<col;j++){
	        	bp1+=sumcol[j]*(p[j]-prop)*(weights[j]-xhat);
	        	bp2+=sumcol[j]*(weights[j]-xhat)*(weights[j]-xhat);
	        }
	        double b=bp1/bp2;
	        if(prop==1||prop==0){
	        	 System.out.println("unaccessible\n");
	        	 return null;
	         }
	        double T=((b*b)/(prop*(1-prop)))*bp2;
	        Double Ts=new BigDecimal(T).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
	        return new double[]{Ts,ChiSquare.cumulative(Ts, 1, false, false)};
	 }
	 
	 


//	public  void burden_CAT( int genesize,int model, List<Map<Integer, Double>> maptest) {
//			tests=new double[genesize];
//			for(int i=0;i<genesize;i=i+1){
//				double[] tab=new double[6];
//				double test=-1d;
//				java.util.Map<Integer,Double> map=new java.util.HashMap<Integer,Double>(0);
//				int com=casetab[i*3]*10000+casetab[i*3+1]*100+casetab[i*3+2];
//				if(maptest.size()>i){
//				  map=maptest.get(i);
//				  if(map.containsKey(com)){
//					  test=map.get(com);
//				  }
//				}
//				if(test<0){
//					for(int j=0;j<3;j++){
//					   tab[j]=casetab[i*3+j];
//					}
//					for(int j=0;j<3;j++){
//					   tab[j+3]=controltab[i*3+j];
//				   }
//				   test=call_CAT(tab,model);
//				   map.put(com, test);
//				   if(maptest.size()>i){
//				        maptest.set(i,map);
//				   }else{
//					   maptest.add(map);
//				  }
//				}
//			  if(test>max){max=test;index=i;}
//			  tests[i]=test;
//			}
//		
//		}
//	 
//		public  void group(int[][] arr,int[] cases,int[] controls,int genesize){
//			casetab=new int[genesize*3];
//			controltab=new int[genesize*3];
//			for(int i=0;i<genesize*3;i++){
//				for(int j=0;j<cases.length;j++){
//					casetab[i]+=arr[cases[j]][i];
//				}
//				for(int j=0;j<controls.length;j++){
//					controltab[i]+=arr[controls[j]][i];
//				}
//			}
//			
//		}
		
		private int[] getWeight(int model) {
			 int[] weights=new int[3];
			 switch(model)
		        {
		        case 0: // 'dominant'
		            weights[0] = 0;
		            weights[1] = 1;
		            weights[2] = 1;
		            break;

		        case 1: // 'additive'
		            weights[0] = 0;
		            weights[1] = 1;
		            weights[2] = 2;
		            break;

		        case 2: // 'recessive'
		            weights[0] = 0;
		            weights[1] = 0;
		            weights[2] = 1;
		            break;
		        }
			return weights;
		}
}
