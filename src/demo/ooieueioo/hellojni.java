package demo.ooieueioo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

;

public class hellojni extends Activity implements OnGestureListener {

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
	private View Touch_View;
	private int Total_Click = 0;
	private GestureDetector detector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initUI();
		root();

		Touch_Listene();
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
					NewTextView(msg.arg1);
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
		tv = (TextView) findViewById(R.id.tv);
		Touch_View = (View) findViewById(R.id.Touch_View);
		this.detector = new GestureDetector(this);
	}

	private void Touch_Listene() {
		this.Touch_View.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {
				// System.out.println("Touch_Listene");
				return detector.onTouchEvent(arg1);
			}

		});
	}

	private void NewTextView(int Total_Click) {
		System.out.println("You Click : " + Total_Click);
		String Call_JNI_Text;
		Call_JNI_Text = stringFromJNI(1).toString();
		tv.setText("You Click : " + (Total_Click) + "  \n" + Call_JNI_Text);
		Log.i("stringFromJNI()", (Total_Click) + "  " + Call_JNI_Text);
		Call_JNI_Text = " ";
		// tv.append("Now Time : " +(this.time)+" "+ stringFromJNI());
		// setContentView(tv);
	}

	private void root() {
		Runtime ex = Runtime.getRuntime();
		String cmdBecomeSu = "su";
		String script = "busybox chmod 777 /dev/input/event1\n";
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
					Thread.currentThread().sleep(500);
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
						Thread.currentThread().sleep(1000);
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
					Thread.currentThread().sleep(10);
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

	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("onDown");
		msg = new Message();
		msg.arg1 = ++Total_Click;
		hd.sendMessage(msg);
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		System.out.println("onFling");
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub\
		System.out.println("onLongPress");

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		System.out.println("onScroll");
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("onShowPress");

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("onSingleTapUp");
		return false;
	}
}