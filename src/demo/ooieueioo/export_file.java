package demo.ooieueioo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.widget.Toast;



public class export_file extends hellojni{
	private String path = "/sdcard/i_mobile/";
	private ArrayList<HashMap<String, Object>> listItem;
	private File myFile;
	private StringBuffer contents;
	private String Notepad = "";
	private int Sava_Status = 0;//0 is not Success
	private int Create_Status = 1;//0 is not Success
	
	public void Write_file(ArrayList<HashMap<String, Object>> obj) {
		this.listItem =  obj;
		try {  
			
  
	        File SDFile = android.os.Environment  
	                .getExternalStorageDirectory();  
	  
	        // open  
	        myFile = new File(SDFile.getAbsolutePath()  
	                + File.separator + "MyFile.txt");  
	  
	        // if not file add new file   
	        if (!myFile.exists()) {  
	            myFile.createNewFile();
	            this.Create_Status = 0;
	        }
	        ReadTextFile();
	        // Write
	        Write();
//	        Check_Status();
	    } catch (Exception e) {  
	        // TODO: handle exception  
	    }
		Log.i("listItem.size()", " "+this.listItem.size());
//		obj.size();
		
	}
	
	private void ReadTextFile(){
        contents = new StringBuffer();
        BufferedReader reader = null;
 
        try {
            reader = new BufferedReader(new FileReader(myFile));
            String Old_text = null;
 
            // repeat until all lines is read
            while ((Old_text = reader.readLine()) != null) {
                contents.append(Old_text)
                        .append(System.getProperty(
                                "line.separator"));
            }
            contents.append("\n\n\n------------ // " + get_Phone_Time() + " add ------------\n\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        // show file contents here
        System.out.println("start" + contents.toString() +" end");
	}
	
	private void Write(){
		
//		Log.i("getMap()", "Map Size = " + listItem.size());
    	for(int i = 0 ;i < this.listItem.size(); i++){
        	HashMap<String,Object> m = this.listItem.get(i);
        	this.Notepad += m.get("time")+"\n" + m.get("Word") + "\n";
//    			m.get("time");
//                m.get("Word");
//                Log.i("getMap()", "m.get(time) = " + m.get("time"));
//                Log.i("getMap()", "m.get(Word) = " + m.get("Word"));
        }

    	//Write file
		try {
//	        String szOutText = "Hello, World!11";  
	        FileOutputStream outputStream;
			outputStream = new FileOutputStream(myFile);
			String tmp_text = this.contents.toString();
			if(tmp_text != null){
				Log.i("xx", " xx");
				tmp_text += this.Notepad;
				outputStream.write(tmp_text.getBytes());
				outputStream.close();
			}else{
				outputStream.write(this.Notepad.getBytes());  
		        outputStream.close();
			}
	        Sava_Status = 1; //Success
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Sava_Status = 0;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Sava_Status = 0;
			e.printStackTrace();
		}
		  
    }
	public int Check_Sava_Status(){
//		Log.i("Check_Status()", "Check_Status " + Sava_Status);
		return this.Sava_Status;
	}
	public int Check_Create_Status(){
//		Log.i("Check_Status()", "Check_Status " + Sava_Status);
		return this.Create_Status;
	}
	
}