package com.example.contacts_demo.entity;

public class Contact {
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private int fav;

    public Contact()
    { }
    public Contact(String fn,String ln,String em,String ph,int f)
    {
        this.first_name=fn;
        this.last_name=ln;
        this.email=em;
        this.phone=ph;
        this.fav=f;
    }
    public String getFirst_name()
    {
        return first_name;
    }
    public String getLast_name()
    {
        return last_name;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPhone()
    {
        return phone;
    }
    public int getFav()
    {
        return fav;
    }
    public void setFirst_name(String fn)
    {
        this.first_name = fn;
    }
    public void setLast_name(String fn)
    {
        this.last_name = fn;
    }
    public void setEmail(String fn)
    {
        this.email = fn;
    }
    public void setPhone(String fn)
    {
        this.phone = fn;
    }
    public void setFav(int fn)
    {
        this.fav = fn;
    }


    public int compareTo(Contact compareCont) {
        return this.first_name.compareTo(compareCont.first_name);
    }

}
