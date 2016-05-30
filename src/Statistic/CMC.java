package Statistic;



//import jdistlib.ChiSquare;
import jdistlib.F;

import org.ejml.simple.SimpleMatrix;

import Basic.Constant;
public class CMC {	
//	public String mingene="";
//	public double minpvalue=1d;
//	public Map<String, double[]> cmc (byte[][] geno,java.util.Map<String,java.util.List<Integer>> genecontain,int size,int[] caserows,int[] controlrows) {
//		java.util.Map<String,double[]> result=new java.util.HashMap<String, double[]>(0);//
//		java.util.Iterator<String> it=genecontain.keySet().iterator();	
//		double min=1000d;
//		result.put("min",new double[]{1000,1,1});
////		int ct=0;
//		while(it.hasNext()){
//			String gene=it.next();
////			ct++;
//			java.util.List<Integer> snps=genecontain.get(gene);
//			int cols=snps.size();
//			double test[]={0,1};
//			if(cols>1){/**at least two snps**/
//				SimpleMatrix casemat=new SimpleMatrix(size,cols);
//				SimpleMatrix controlmat=new SimpleMatrix(geno.length-size,cols);
//				for(int col=0;col<cols;col++){
//					for(int i=0;i<size;i++){
//						casemat.set(i,col,geno[caserows[i]][snps.get(col)]);
//					}
//					for(int i=0;i<controlrows.length;i++){
//						controlmat.set(i,col,geno[controlrows[i]][snps.get(col)]);
//					}
//				}
//				 test=hotellingT2(casemat,controlmat);
//				 casemat=null;
//				 controlmat=null;
//			}
//			if(test[1]<min&&test[1]>-1){
//				min=test[1];
//				this.minpvalue=min;
//				this.mingene=gene;
//		    }
//			result.put(gene,test);
//			
//		}
//		return result;
//	}
	public double[] hotellingT2(SimpleMatrix X1, SimpleMatrix X2) {
		  
		  int n1 = (int)(X1.numRows());
		  int n2 = (int)(X2.numRows());
	
		  int dim_S = X1.numCols();
		  SimpleMatrix S1 = new SimpleMatrix(dim_S,dim_S);
		  SimpleMatrix S2 =new SimpleMatrix(dim_S,dim_S);
		  SimpleMatrix S =new SimpleMatrix(dim_S,dim_S);
	
		  SimpleMatrix x_bar_1 = new SimpleMatrix(dim_S,1);
		  SimpleMatrix x_bar_2 =new SimpleMatrix(dim_S,1);
		  SimpleMatrix barcs=new SimpleMatrix(n1,dim_S);
		  SimpleMatrix barctr=new SimpleMatrix(n2,dim_S);
		  SimpleMatrix d_x_bar = new SimpleMatrix(dim_S,1);
		  for (int j = 0; j < dim_S; j++) {
			 SimpleMatrix xtj1=X1.extractVector(false,j);
			 double xjbar=xtj1.elementSum()/(double)xtj1.getNumElements();//Meancol(xtj1);
			 x_bar_1.set(j,xjbar);
			 for(int i=0;i<n1;i++){barcs.set(i,j,xjbar);}
			 	 
			 SimpleMatrix xtj2=X2.extractVector(false,j);
			 double xjbar2=xtj2.elementSum()/(double)xtj2.getNumElements();//Meancol(xtj2);
			 x_bar_2.set(j,xjbar2);
			 for(int i=0;i<n2;i++){barctr.set(i,j,xjbar2);}
		  }
		  SimpleMatrix dcs=X1.minus(barcs);
		  SimpleMatrix dctr=X2.minus(barctr);
		  S1=(dcs.transpose()).mult(dcs);
		  S2=(dctr.transpose()).mult(dctr);
		  SimpleMatrix ps=S1.plus(S2);
		  S=(ps).scale(1/((double)(n1+n2-2)));		 
		  d_x_bar=x_bar_1.minus(x_bar_2);
		  SimpleMatrix sinvert=new SimpleMatrix(S.numRows(),S.numCols());
		  int rank= inverse(sinvert,S);
		  SimpleMatrix dt=d_x_bar.transpose();	 
		  SimpleMatrix tem=dt.mult(sinvert);
		  SimpleMatrix TM =null;
		  try{  
		     TM=tem.mult(d_x_bar);
		  }catch(ArrayIndexOutOfBoundsException e){
			  System.out.print("X1\n");
			  X1.print();
			  System.out.print("X2\n");
			  X2.print();
		  }
		  double T2=TM.get(0)*(n1*n2/(double)(n1+n2));
		  double stat=((n1+n2-rank-1)/(double)(rank*(n1+n2-2)));
		  stat=stat*T2;
		  double test=-99;
		  if(rank>0&&n1+n2-rank>1){
		    test=F.cumulative(stat, rank,(n1+n2-rank-1), false,false);
		  }
		  S1=null;
		  S2=null;
		  S=null;
		  sinvert=null;
		  x_bar_1=null;
		  x_bar_2=null;
		  barcs=null;barctr=null;
		  d_x_bar=null;
		  return new double[]{stat,test};

	}

    
	 public static int inverse(SimpleMatrix out,SimpleMatrix s){
	    	int rank=s.numCols();
	    	int colc=s.numCols();
	    	int rowc=s.numRows();
	    	double[][] si=new double[rowc][rank];
	    	for(int i=0;i<rowc;i++){
	    		for(int j=0;j<colc;j++){
	    			si[i][j]=s.get(i, j);
	    		}
	    	}
	    
	    	for(int k=0;k<colc;k++){
	    		double D=si[k][k];
	    		if(si[k][k]<Constant.getInstance().tol){
	    			for(int i=0;i<rowc;i++){
	    				si[k][i]=0;
	    				si[i][k]=0;
	    				
	    				}
	    			rank--;
	    		}
	    		else{
	    			double[] row=si[k];
	    			double[] newrow=new double[colc];
	    			for(int j=0;j<colc;j++){
	    				newrow[j]=row[j]/D;
	    				si[k][j]=row[j]/D;
	    			}
	    			for(int j=0;j<rowc;j++){
	    				double[] rowj=si[j];
	    				if(j!=k){
	    					double B=si[j][k];
	    					for(int m=0;m<colc;m++){
	    						si[j][m]=rowj[m]-newrow[m]*B;
	    					}
	    					si[j][k]=-B/D;
	    				}
	    				rowj=null;
	    			}
	    			si[k][k]=1/D;
	    		    row=null;
	    		    newrow=null;
	    		}
	    	}
	    	for(int i=0;i<rowc;i++){
	    		for(int j=0;j<colc;j++){
	    			out.set(i,j,si[i][j]);
	    		}
	    	}
	    	si=null;
	    	return rank;
	    }
    
    

}

 