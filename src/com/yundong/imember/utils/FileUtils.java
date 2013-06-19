package com.yundong.imember.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private String SDPATH;   //用于存sd card的文件的路径
	 
	public String getSDPATH() {
		return SDPATH;
	}
	public void setSDPATH(String sDPATH) {
		SDPATH = sDPATH;
	}
 
    /**
     * 构造方法
     * 获取SD卡路径
     */
	public FileUtils() {
		//获得当前外部存储设备的目录
		SDPATH=Environment.getExternalStorageDirectory()+"/";
//		System.out.println("sd card's directory path:"+SDPATH);
	}
 
 
	 /**
	  * 在SD卡上创建文件
	  * @throws IOException 
	  */
	 public File createSDFile(String fileName) throws IOException{
		 File file=new File(SDPATH+fileName);
		 file.createNewFile();
		 return file;
	 }
	 /**
	  * 在SD卡上创建目录
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
	  * 判断SD卡上的文件夹是否存在
	  */
	 public boolean isFileExist(String fileName){
		 File file=new File(SDPATH+fileName);
		 return file.exists();
	 }
 
	 /**
	  * 将一个inputSteam里面的数据写入到SD卡中
	  */
	 public File write2SDFromInput(String path,String fileName,InputStream inputStream){
		  File file=null;
		  OutputStream output=null;
		  try {
			   //创建SD卡上的目录
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
			   //创建SD卡上的目录
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