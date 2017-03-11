package com.example.ifamily.message;

import java.io.Serializable;

public class HeadM implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = -4451760273508129705L;
Object imgs;
  String name;
  long id;
  int type;
  public HeadM(Object img)
  {
	  super();
	  this.imgs=img;
  }
  public HeadM(long id,Object object)
  {
	  super();
	  this.imgs=object;
	  this.id=id;

  }
  public Object getimg()
  {
	  return imgs;
  }
  public long getid()
  {
	  return id;
  }
  public String getname()
  {
	  return (String)imgs;
  }
  public void settype(int type)
  {
	  this.type=type;
  }
  public int gettype()
  {
	  return type;
  }
}
