package com.example.ifamily.message;

import java.util.ArrayList;
import java.util.List;

public class Relations {
private List<String> relation=new ArrayList<String>();
private String malerelation1="“Ø“Ø";
private String malerelation2="∞÷∞÷";
private String malerelation3="æÀæÀ";
private String malerelation4=" Â Â";
private String malerelation5="∏Á∏Á";
private String malerelation6="µ‹µ‹";
private String malerelation7="π√“Ø";
private String femalerelation1="ƒÃƒÃ";
private String femalerelation2="¬Ë¬Ë";
private String femalerelation3="æÀ¬Ë";
private String femalerelation4="…Ù…Ù";
private String femalerelation5="Ω„Ω„";
private String femalerelation6="√√√√";
private String femalerelation7="π√π√";
public Relations(int type)
{
	super();
	if(type==1)////////////////////1ƒ–2≈Æ
	{
	relation.clear();
	relation.add(femalerelation1);
	relation.add(femalerelation2);
	relation.add(femalerelation3);
	relation.add(femalerelation7);
	relation.add(femalerelation4);
	relation.add(femalerelation5);
	relation.add(femalerelation6);
	}
	else{
		relation.clear();
		relation.add(malerelation1);
		relation.add(malerelation2);
		relation.add(malerelation3);
		relation.add(malerelation7);
		relation.add(malerelation4);
		relation.add(malerelation5);
		relation.add(malerelation6);
		
	}
}

public List<String> getrelations()
{
	return relation;
}
}
