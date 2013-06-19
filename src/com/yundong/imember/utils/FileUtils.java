package com.yundong.imember.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private String SDPATH;   //���ڴ�sd card���ļ���·��
	 
	public String getSDPATH() {
		return SDPATH;
	}
	public void setSDPATH(String sDPATH) {
		SDPATH = sDPATH;
	}
 
    /**
     * ���췽��
     * ��ȡSD��·��
     */
	public FileUtils() {
		//��õ�ǰ�ⲿ�洢�豸��Ŀ¼
		SDPATH=Environment.getExternalStorageDirectory()+"/";
//		System.out.println("sd card's directory path:"+SDPATH);
	}
 
 
	 /**
	  * ��SD���ϴ����ļ�
	  * @throws IOException 
	  */
	 public File createSDFile(String fileName) throws IOException{
		 File file=new File(SDPATH+fileName);
		 file.createNewFile();
		 return file;
	 }
	 /**
	  * ��SD���ϴ���Ŀ¼
	  */
	 public File createSDDir(String dirName){
		 File dir=new File(SDPATH+dirName);
		 System.out.println("storage device's state :"+Environment.getExternalStorageState());
		 
		 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			 System.out.println("this directory real path is:"+dir.getAbsolutePath());
			 System.out.println("the result of making directory:"+dir.mkdir());
		 }
		 return dir;
	 }
 
	 /**
	  * �ж�SD���ϵ��ļ����Ƿ����
	  */
	 public boolean isFileExist(String fileName){
		 File file=new File(SDPATH+fileName);
		 return file.exists();
	 }
 
	 /**
	  * ��һ��inputSteam���������д�뵽SD����
	  */
	 public File write2SDFromInput(String path,String fileName,InputStream inputStream){
		  File file=null;
		  OutputStream output=null;
		  try {
			   //����SD���ϵ�Ŀ¼
			   File tempf=createSDDir(path);
			   System.out.println("directory in the sd card:"+tempf.exists());
			   file=createSDFile(path+fileName);
			   System.out.println("file in the sd card:"+file.exists());
			   output=new FileOutputStream(file);
//			   byte buffer[]=new byte[4*1024];
			   byte buffer[]=new byte[1*1024];
			   while((inputStream.read(buffer))!=-1){
				   output.write(buffer);
			   }
			   output.flush();
		  } catch (FileNotFoundException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }finally{
			   try {
				   if(output != null){
					   output.close();
				   }
				   if(inputStream != null){
					   inputStream.close();
				   }
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		  }
		  return file;
	 }
	 
	 public File write2SDFromInput(String path,String fileName,byte[] b){
		  File file=null;
		  OutputStream output=null;
		  try {
			   //����SD���ϵ�Ŀ¼
			   File tempf=createSDDir(path);
			   System.out.println("directory in the sd card:"+tempf.exists());
			   file=createSDFile(path+fileName);
			   System.out.println("file in the sd card:"+file.exists());
			   output=new FileOutputStream(file);
			   output.write(b);
			   output.flush();
		  } catch (FileNotFoundException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }finally{
			   try {
				   if(output != null){
					   output.close();
				   }
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		  }
		  return file;
	 }
}