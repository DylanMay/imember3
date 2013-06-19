package com.yundong.imember.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpDownLoader {
	private URL url;
 
	 /**
	  * ��ȡ�ı��ļ�,�����ַ���
	  * @param urlStr
	  * @return
	  */
	 public String download(String urlStr) {
		  StringBuffer sb=new StringBuffer();
		  String line=null;
		  BufferedReader buffer=null;
		  try {
			   //����һ��url����
			   url=new URL(urlStr);
			   //����һ��Http����
			   HttpURLConnection urlcon=(HttpURLConnection)url.openConnection();
			   //ʹ��IO��ȡ����
			   buffer=new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
			   //ѭ��
			   while((line=buffer.readLine())!=null){
			    sb.append(line);
			   }
		  } catch (MalformedURLException e) {
			  e.printStackTrace();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }finally{
			   try {
				   if(buffer != null){
					   buffer.close();
				   }
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		  }
		  return sb.toString();
	 }
 
	 /**
	  * ��ȡ�ļ�������������
	  * @param urlStr
	  * @param path
	  * @param fileName
	  * @return -1:���س���  0:�ɹ�    1: �ļ��Ѿ�����
	  */
	 public int downFile(String urlStr,String path,String fileName){
		  int result=-1;
		  InputStream inputStream=null;
		  FileUtils fileUtils=new FileUtils();
		  
		  try {
			  //�ļ��Ѵ���
			   if(fileUtils.isFileExist(path+fileName)){
			    return 1;
			   }
			   else{
				    inputStream=getInputStreamFromUrl(urlStr);
				    File resultFile=fileUtils.write2SDFromInput(path, fileName, inputStream);
				    if(resultFile==null){
				    	return -1;
				    }
			   }
		  } catch (IOException e) {
			  e.printStackTrace();
			  return -1;
		  }finally{
			   try {
				   if(inputStream != null){
					   inputStream.close();
				   }
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
		  }
		  return 0;
	 }
	 
	 public int downFile(String path,String fileName, byte[] b){
		  int result=-1;
		  FileUtils fileUtils=new FileUtils();
		  
		  try {
			  //�ļ��Ѵ���
			   if(fileUtils.isFileExist(path+fileName)){
			    return 1;
			   }
			   else{
				    File resultFile=fileUtils.write2SDFromInput(path, fileName, b);
				    if(resultFile==null){
				    	return -1;
				    }
			   }
		  } catch (Exception e) {
			  e.printStackTrace();
			  return -1;
		  }
		  return 0;
	 }
	 
	 /**
	  * ����url��ַ��ȡ������
	  * @param urlStr
	  * @return
	  * @throws IOException
	  */
	 public InputStream getInputStreamFromUrl(String urlStr) throws IOException{
		  url=new URL(urlStr);
//		  HttpURLConnection urlCon=(HttpURLConnection)url.openConnection();
//		  InputStream inputStream=urlCon.getInputStream();
		  InputStream inputStream = (InputStream)url.getContent();
		  return inputStream;
		 
//		 InputStream is = null;
//		 HttpClient client = new DefaultHttpClient();     
//			// params[0]�������ӵ�url     
//			HttpGet get = new HttpGet(urlStr);     
//			HttpResponse response;     
//			try {     
//				response = client.execute(get);     
//				HttpEntity entity = response.getEntity();     
//				
//				is = entity.getContent();
//				return is;
//			} catch (ClientProtocolException e) {     
//				// TODO Auto-generated catch block     
//				e.printStackTrace();     
//			} catch (IOException e) {     
//				// TODO Auto-generated catch block     
//				e.printStackTrace();     
//			}
//			return null;
	 }
}