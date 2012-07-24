package demo.ooieueioo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;       //+cy.0629
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import demo.ooieueioo.db.DbAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;                    //+cy.0629


public class hellojni extends Activity implements OnGestureListener  {

	static {
		System.loadLibrary("hellojni");
	}

	public native String stringFromJNI(int i);
	public native String unimplementedStringFromJNI();

	private Handler hd;
	private Message msg;
	private int time = 0;
	private TextView tv;
	private Toast toast;
	private LinearLayout Touch_View;
	private ScrollView ScrollView;
	private TextView Text_View;
	private int Total_Click = 0;
	private int message_count = 0;
	private GestureDetector detector;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private DbAdapter mDbHelper;
	  //Date date = new Date();     //cy.0629.test.   If you add "new Data()" into here, the result cannot update data.
	  //DateFormat dateformat;      //cy.0629.test
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initUI();
		root();
		Touch_Listene();
		mDbHelper = new DbAdapter(this);
		
		// CommandWriter()
		  /*
		   * CommandWriter cmd; Thread writer = new Thread((cmd = new
		   * CommandWriter(hd))); writer.start(); try {
		   * Thread.currentThread().sleep(1000); } catch (InterruptedException e1)
		   * { // TODO Auto-generated catch block e1.printStackTrace(); }
		   * //cmd.addCommand("cd ~/..\n"); //cmd.addCommand("ls\n");
		   */
		// po
		// try{
		// new Thread(new t()).start();
		// }catch (Exception e) {
		// // TODO: handle exception
		// }
		
		
		hd = new Handler() {
			public void handleMessage(Message msg) {
				// if()
				// }

				System.out.println("        handler" + msg.arg1);
				if (msg.what == 0) {
					NewTextView(msg.arg1);      // <---  send message :)
				} else if (msg.what == 1) {
					if (toast != null)
						toast.cancel();
					toast = Toast.makeText(hellojni.this, (String) msg.obj,
							Toast.LENGTH_SHORT);
					toast.show();
				}
			}

		};

		// NewTextView();
	}

	private void initUI() {
		this.Touch_View = (LinearLayout)findViewById(R.id.Touch_View);
		this.ScrollView = (ScrollView)findViewById(R.id.ScrollView);
		this.tv = (TextView) findViewById(R.id.tv);
		this.Text_View = (TextView) findViewById(R.id.Text_View);
		this.detector = new GestureDetector(this);//+po.0623 del
	}

	
//      -cy.0629.del...... Build new function Touch_Listene
//	private void Touch_Listene() {
//		this.Touch_View.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				 System.out.println("Touch_Listene");
//				return detector.onTouchEvent(arg1);
//			}
//
//		});
//		
//		this.ScrollView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				 System.out.println("ScrollView");
//				return detector.onTouchEvent(arg1);
//			}
//
//		});
//	}

	private void NewTextView(int Total_Click) {
		System.out.println("You Click : " + Total_Click);
		String Call_JNI_Text;
		//String Call_JNI_Text_1;                                       // +clayder.0629.test
		Call_JNI_Text = stringFromJNI(1).toString();          
		//Call_JNI_Text_1 = stringFromJNI(2).toString();    // +clayder.0629.test
		
		this.tv.setText("You Click : " + (Total_Click));
	     //  String message = get_Now_Time().toString() + Call_JNI_Text + "\n ";      //+cy.test. modify
		String message =  Call_JNI_Text + "\n";      //+cy.0706
	        message_count++;
		if(message_count % 100 == 0){
			Text_View.setText("");
		}
		//this.Text_View.setText(message_count + "]" + message + "[" + this.Text_View.getText().toString() );  //send message clayder
		this.Text_View.setText(this.Text_View.getText().toString() +  message );  //send message clayder

		
		Log.i("stringFromJNI()", (Total_Click) + "  " + Call_JNI_Text);
		setMap(Call_JNI_Text);
//		getMap();
		Call_JNI_Text = " ";
//		 tv.append("Now Time : " +(this.time)+" "+ stringFromJNI());
		// setContentView(tv);
	}

	private String setMap(String str) {
		Log.i("setMap(String str)", "str = " + str.toString());
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		map.put("time", get_Now_Time().toString());
		map.put("Word", str.toString());

		// map.put("img", String.valueOf(R.drawable.word));

		listItem.add(map);
		return str;
	}
	private CharSequence get_Now_Time() {
		long dtMili = System.currentTimeMillis();
		Date dt = new Date(dtMili);    //-clayder.0702
		//yyyy-MM-dd HH:mm:ss)
		
		SimpleDateFormat formatter   =  new SimpleDateFormat("[ss.SSS]");
		String now = formatter.format( new Date());
		//CharSequence Now_Time  = DateFormat.format("yyyy, MM/dd, hh:mm:ss", dt.getTime());
		//----- clayder,0626

		//		Log.i("NewTextView()", "time == " + Now_Time);
		return now;
	}
	public CharSequence get_Phone_Time() {
		long dtMili = System.currentTimeMillis();
		Date dt = new Date(dtMili);
		//yyyy-MM-dd HH:mm:ss)
		CharSequence Now_Time  = DateFormat.format("yyyy, MM/dd, hh:mm:ss", dt.getTime());
		//		Log.i("NewTextView()", "time == " + Now_Time);
		return Now_Time;
	}
	
	private void getMap(){
		Log.i("getMap()", "Map Size = " + listItem.size());
    	for(int i = 0 ;i < listItem.size(); i++){
        	HashMap<String,Object> m = listItem.get(i);
    			m.get("time");
                m.get("Word");
                Log.i("getMap()", "m.get(time) = " + m.get("time"));
                Log.i("getMap()", "m.get(Word) = " + m.get("Word"));
        }
    }
	public void open_data(View v){
//		Log.i("open_data(View v)", "Onclick");
		Intent newIntent = new Intent(hellojni.this,
              listItem_view.class);
		newIntent.putExtra("HashMap", listItem);
		startActivityForResult(newIntent, 7);
	}

	private void root() {
		Runtime ex = Runtime.getRuntime();
		String cmdBecomeSu = "su";
		String script = "busybox chmod 777 /dev/input/event2\n";
		try {
			java.lang.Process runsum = ex.exec(cmdBecomeSu);
			int exitVal = 0;
			final OutputStreamWriter out = new OutputStreamWriter(
					runsum.getOutputStream());
			// Write the script to be executed
			out.write(script);

			// Ensure that the last character is an "enter"
			// out.write("\n");
			// out.flush();
			// Terminate the "su" process
			// out.write("cd ~/..\n");
			out.write("exit\n");

			out.flush();
			exitVal = runsum.waitFor();
			if (exitVal == 0) {
				Log.e("Debug", "Successfully to su");
			}
		} catch (Exception e) {
			Log.e("Debug", "Fails to su");
		}
	}

	class t implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			int i = 0;
			while (true) {
				try {

					System.out.println(i++ + ".......");
					msg = new Message();
					msg.what = 0;
					msg.arg1 = time++;
					hd.sendMessage(msg);
					// NewTextView();
					Thread.currentThread();
					Thread.sleep(500);
					//Thread.currentThread().sleep(500);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}

	}

	class CommandWriter implements Runnable {
		private Handler hd;
		private List<String> cmd;
		private boolean isExit;
		private OutputStreamWriter out;
		private InputStream ins;

		CommandWriter(Handler hd) {
			this.cmd = new ArrayList<String>();
			isExit = false;
			this.hd = hd;
			this.init();
		}

		private void init() {
			Runtime ex = Runtime.getRuntime();
			String cmdBecomeSu = "su";
			String script = "busybox chmod a+rw /dev/pmem\n";
			try {
				java.lang.Process runsum = ex.exec(cmdBecomeSu);
				int exitVal = 0;
				this.out = new OutputStreamWriter(runsum.getOutputStream());
				out.write(script);
				out.flush();
				ins = runsum.getInputStream();
			} catch (Exception e) {
			}
		}

		public void addCommand(String cmd) {
			if (this.cmd == null)
				this.cmd = new ArrayList<String>();
			this.cmd.add(cmd);
		}

		public void run() {
			while (!isExit) {
				// write command
				getInputData();
				if (this.cmd == null || this.cmd.size() <= 0) {
					try {
						Thread.currentThread();
						Thread.sleep(10);
						//Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				// more code here
				try {
					this.out.write(cmd.get(0));
					this.out.flush();
				} catch (IOException e) {
					this.cmd.remove(0);
					continue;
				}
				this.cmd.remove(0);
				try {
					Thread.currentThread();
					Thread.sleep(10);
					//Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void Exit() {
			this.isExit = true;
		}

		private void getInputData() {
			int size = -1;
			try {
				if (ins != null && (size = ins.available()) > 0) {
					byte[] arr = new byte[size];
					String data = new String(arr, size);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = data;
					hd.sendMessage(msg);
				}
				Log.w("get INS", "get INS");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void set_msg(){
		msg = new Message();
		msg.arg1 = ++Total_Click;
		hd.sendMessage(msg);
	}
	private void touchAction(MotionEvent event)         //+cy.0629. add new function include down move up
	{
		// event.getAction() type ==> 0: down 1:up 2: move
		switch(event.getAction())
		{
			case 0:
				set_msg();
				//NewTextView(msg.arg1);      // <---  send message :)
				//Log.d( "","down Event");
				break;
			case 1:
				set_msg();
				//NewTextView(msg.arg1);      // <---  send message :)
				//Log.d( "","up Event");
				break;
			case 2:
				set_msg();
				//NewTextView(msg.arg1);      // <---  send message :)
				//Log.d( "","motion Event");
				break;
		}
		
		
	    
		
		
//		if(listItem.size() > 0){
//			mDbHelper.open();
//			HashMap<String,Object> m = listItem.get(listItem.size()-1);
//			mDbHelper.addlog(m.get("time").toString(), m.get("Word").toString());
//			mDbHelper.close();
//		}
		
		
	}
	/**
	 * touch listene
	 */
	private void Touch_Listene() {                     //+cy.0629. add new function
		//¶Â¦âpanel
		this.Touch_View.setOnTouchListener(new OnTouchListener() {
			
			//public abstract boolean onTouch (View v, MotionEvent event)
			public boolean onTouch(View arg0, MotionEvent event) {
				touchAction(event);
				return detector.onTouchEvent(event);
			}

		});
		// white panel
		this.ScrollView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				touchAction(event);
				return false; //+po.0723
				//return detector.onTouchEvent(event);
			}

		});
	}
	public void ShowDialog(String str){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);		
	    dialog.setTitle("Inform")
	    .setMessage(str.toString())
	    .setNeutralButton("Exit", new OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                    // TODO Auto-generated method stub

	            }

	    }).show();
	}
	public void ShowToast(String str){
		toast = Toast.makeText(hellojni.this, str.toString(),
				Toast.LENGTH_SHORT);
		toast.show();
	}
	
	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
//		touchAction(e);
		return false;
	}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		touchAction(e);
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
