package Basic;

//import java.io.File;

public final class Constant {
	public String outfolder;//="src/OUTPUT";    
	public String rank="";
	public String vcf="";
	public String controlvcf="data/1kg.controls.vcf.gz";
    public static int indscount=0;;
    static java.util.List<Individual> inds;
	public int pertime=1000;
	public double tol=0.0000001;
    public int model=1;
    public int randomtype=1;// as randomly not pair
	public static boolean verbose=false;
	private static Constant instance = null;
	public static int size=0;
	public static int control_size=100;
	public String suffix="Permutation";
	String[] models={"dominant","addtive","recessive"};
	public String pedFile="";/*ped file*/
	public int[] caseIndexs=null;
	public int[] controlIndexs=null;
	public String groupfile="";
	public String weightfile="";
	public static Constant getInstance() {
	      if(instance == null) {
	         instance = new Constant();
	      }
	      return instance;
	}

	public void setFolder(String f,String vcf_prefix,String control_vcf,String rank,String ped,String groupf,String weightf){
		this.outfolder=f+"/";
		this.rank=rank;
		this.vcf=vcf_prefix;
		if(!control_vcf.equals(""))this.controlvcf=control_vcf;
		if(!ped.equals(""))this.pedFile=ped;
		if(!weightf.equals(""))this.weightfile=weightf;
		if(!groupf.equals("")) this.groupfile=groupf;
		if(groupfile.equals("")){
			
			System.out.println("\t Group file: "+this.groupfile+"\n");
		}
		if(weightfile.equals("")){
			
			System.out.println("\t Weight file: "+this.weightfile+"\n");
		}
//		if((new File(rank)).exists()){
//			Constant.inds=Individual.initialize(rank);
//		}
		
	}

	
	public void print(){
		System.out.print(
				"\nInput parameters are: \n\n"+
		  	         	   "\t case vcf file: "+this.vcf+"\n"+
		  	         	   "\t similarity matrix: "+this.rank+"\n"+
		  	               "\t output folder: "+this.outfolder+"\n"+
		  	               "\t output prefix: "+this.suffix+"\n"+
				           "\t permutation times: "+this.pertime+"\n"+
				           
				           "\t genetic model in CATT: "+models[this.model]+"\n"
	  );
	if(pedFile.equals("")&&caseIndexs==null&&controlIndexs==null){	
		     System.out.println(
		       	"\t control vcf file: "+this.controlvcf+"\n"+
		        "\t control group size: "+control_size+"\n");
	}else{
		if(pedFile.equals("")){
			System.out.println("\t Case and Control columns has been specified,\n");
		}else{
			System.out.println("\t Ped file: "+this.pedFile+"\n");
		}
		
	}
	if(groupfile.equals("")){
		
		System.out.println("\t Group file: "+this.groupfile+"\n");
	}
	if(weightfile.equals("")){
		
		System.out.println("\t Weight file: "+this.weightfile+"\n");
	}
   }
	
	
}
