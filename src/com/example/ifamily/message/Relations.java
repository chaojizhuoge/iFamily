package com.example.ifamily.message;

import java.util.ArrayList;
import java.util.List;

public class Relations {
private List<String> relation=new ArrayList<String>();
private String malerelation1="үү";
private String malerelation2="�ְ�";
private String malerelation3="�˾�";
private String malerelation4="����";
private String malerelation5="���";
private String malerelation6="�ܵ�";
private String malerelation7="��ү";
private String femalerelation1="����";
private String femalerelation2="����";
private String femalerelation3="����";
private String femalerelation4="����";
private String femalerelation5="���";
private String femalerelation6="����";
private String femalerelation7="�ù�";
public Relations(int type)
{
	super();
	if(type==1)////////////////////1��2Ů
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
