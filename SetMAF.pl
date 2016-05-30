#!/usr/bin/perl

#!/usr/nom/env perl 



	my $in=$ARGV[0];##DBSNP FILE  "../original/dbsnp.Esp5400.union.noxy.txt
	my $in2=$ARGV[1]; ##local FILE "378Sample.vcf"
	my $out2f=$ARGV[2];##out2put file
	if(!(-e $in && -e $in2)){print "error!not all file exists!";exit();}
	open inrare, $in; 
	open inorg,"$in2";
	open out2,"> $out2f";
	print $out2f."\n";
	my $org;
	my $rarein;
	while(($org=<inorg>)=~/^#/){print out2 $org;}
	do{$rare=<inrare>;}while($rarein=~/^#/);
	
        while($org){  ## local file
	    chomp($org);
	    my @orgstrs=split(/\s+/,$org);
	    my $orgchr=$orgstrs[0];    ##original file format: chr pos id ref alt
	    $orgchr=~s/chr//;
	    my $orgpos=$orgstrs[1];
	    my $orgref="";
	    my $orgalt="";
    ##******complicated format 
	#    $orgref=$orgstrs[3];
	    #$orgalt=$orgstrs[4];
	    my @rarestrs;
	    my $rarechr="";
	    my $rarepos="";
	    my $rareref="";
	    my $rarealt="";
	    my $i=0;
	    do{
		@rarestrs=split(/\s+/,$rare);
		$rarechr=$rarestrs[1];			##rare format gmaf, chr, pos, id ,ref,alt.	
		$rarepos=$rarestrs[2];
		#$rareref=$rarestrs[4];
		#$rarealt=$rarestrs[5];
		$gmaf=$rarestrs[0];
	     }while(($rarechr<$orgchr||(($rarechr == $orgchr)&&($rarepos<$orgpos))||(($rarechr == $orgchr)&&($rarepos==$orgpos)&&$rarealt ne $orgalt))&&($rare=<inrare>));
    	    if($rare){
#     	         print $rarechr."\t".$orgchr ."\t".$rarepos."\t".$orgpos."\n";
	          if($rarechr == $orgchr&&$rarepos==$orgpos&& $rarealt eq $orgalt){
			#    if($gmaf<0.01||$gmaf==0.01){
			   
			     $orgstrs[7]=$orgstrs[7].";GENE=".$gmaf;
			     print out2 join("\t",@orgstrs)."\n";
			    
			#    }
			      $org=<inorg>;
	          }
	   if(($orgchr<$rarechr)||($orgchr== $rarechr&&$orgpos<$rarepos)){
# 		$gmaf=0;
# 		print out2 "$org\t$gmaf\n";	
		$org=<inorg>;	
      	    }
            }else {
#                $gmaf=0;
# 		print out2 "$org\t$gmaf\n";	
# 		$org=<inorg>;	
            }
         }

	close out2;
	close inorg;
	close inrare;

