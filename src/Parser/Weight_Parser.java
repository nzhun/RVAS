package Parser;

import java.util.regex.Pattern;

public class Weight_Parser {
   public float score=0;
   public String chr="";
   public int pos=0;
   public void weight_line_parser(String line){
	    Pattern pattern_all=Pattern.compile("\\t");
	    String[] sets=pattern_all.split(line,0);
	    chr=sets[0];
		try{
	        pos=Integer.parseInt(sets[1]);
	        score=Float.parseFloat(sets[2]);
		} catch (NumberFormatException e) {
			System.out.println("Either " +sets[1]+" or "+sets[2] +" is not numberic");
			e.printStackTrace();
		}
   }
}
