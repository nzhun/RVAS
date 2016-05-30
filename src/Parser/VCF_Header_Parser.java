package Parser;


import java.util.regex.Pattern;


public class VCF_Header_Parser {
	public String chr;
	public int pos;
	public String ref;
	public String alt;
	public String[] IDs;
	public void vcf_line_parser(String line) {
		    Pattern pattern_all=Pattern.compile("\\t");
		    String[] sets=pattern_all.split(line,0);
		    int FIX_H=9;
		    IDs=new String[sets.length-FIX_H];
		    for(int i=FIX_H;i<sets.length;i++){
				IDs[i-FIX_H]=sets[i];
		    }
	}
}
