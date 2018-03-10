package igs;

import java.io.*;

public class SMSWriter
{
 String fname;
 
 public SMSWriter()
 {
    String msgDirectory;
    msgDirectory =  "d:/IGS/messages";
    File f = new File(msgDirectory);
    if(!f.exists())
        f.mkdirs();
    fname =  msgDirectory + "/messages.txt";
 }

 public boolean writeSMS(String pno, String msg)
 {
   boolean flag;
   try
   {
     FileWriter fw = new FileWriter(fname, true);//append
     fw.write(pno + "~" + msg +"~");
     fw.flush();
     fw.close();
     flag = true;
   }
   catch(Exception ex)
   {
      flag = false;	
   }   
   return flag;
 }

}