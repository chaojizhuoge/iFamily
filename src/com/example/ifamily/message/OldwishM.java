package com.example.ifamily.message;

public class OldwishM {
	private long account;
	private Object img;
	private Object head;
	private String name;
	private String text;
	private int state;
	private String time;
	private int type;
	private int comnum;

	/////////ʵ�����õ��� id,ͷ�����֣����ݣ�ʱ�䣬״̬,��������
	public  OldwishM(long account,Object heads,String names,String text,String time,int state,int comnum) {

		super();
		this.account=account;
		this.time=time;
		this.state=state;
		this.head=heads;
		this.name=names;
		this.text=text;
		this.comnum=comnum;

	}
	//��Ϊ�Լ���wish, id,ͷ�����֣����ݣ�ʱ�䣬״̬,
	public  OldwishM(long account,Object heads,String names,String text,String time,int state) {

		super();
		this.account=account;
		this.time=time;
		this.state=state;
		this.head=heads;
		this.name=names;
		this.text=text;
		this.comnum=comnum;

	}
	// ֻ���˺ţ�ͷ���ǳƣ�Ը����ʱ��
	public  OldwishM(long account,Object heads,String names,String text,String time) {

		super();
		this.account=account;
		this.time=time;
		this.head=heads;
		this.name=names;
		this.text=text;

	}
	public Object getimg()
	{
		return img;
	}
	public String getname()
	{
		return name;
	}
	public String gettext()
	{
		return text;
	}
	public int gettype()
	{
		return type;
	}
	public Object gethead()
	{
		return head;
	}
	
	public int getstate()
	{
		return state;
	}
	public String gettime()
	{
		return time;
	}
	public long getaccount()
	{
		return account;
	}
	public int getcomnum()
	{
		return comnum;
	}

}
