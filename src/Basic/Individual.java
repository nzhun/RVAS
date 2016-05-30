package Basic;

public class Individual {
 String Num="";
 String FamilyNum="";
 int flag=0;
 java.util.List<String> Neighbors=new java.util.ArrayList<String>(0);
 public void setNum(String i){
	 this.Num=i;
 }
 public void setFamNum(String i){
	 this.FamilyNum=i;
 }
 public void setFlag(int i){
	 this.flag=i;
 }
 public void addneighbor(String i){
	 Neighbors.add(i);
 }
  public String getNum(){
	  return this.Num;
  }
  public String getFamilyNum(){
    return this.FamilyNum;
  }
  public int getFlag(){
     return this.flag;
  }
  public java.util.List<String> getNeighbors(){
      return this.Neighbors;
  }
  
 

}
