package com.example.ifamily.message;

public class RelationM {
private long account;
private Object head;
private String relationship;//���û�о����ǳ�
private int sex;//1��2Ů

public RelationM(long account,Object head,String relationship,int sex)
{
	super();
	this.account =account;
	this.head=head;
	this.relationship=relationship;
	this.sex=sex;
}
public RelationM(long account,Object head,String relationship)
{
	super();
	this.account =account;
	this.head=head;
	this.relationship=relationship;
}
public long getaccount()
{
	return account;
	
}
public Object gethead()
{
	return head;
}

public String getrelationship()
{
	return relationship;
}
public int getsex()
{
	return sex;
}
}
