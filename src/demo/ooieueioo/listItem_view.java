package demo.ooieueioo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class listItem_view extends Activity {
	private ListView maListViewPerso;
	private ArrayList<HashMap<String, Object>> listItem;
	private Button Expotr_btn;
	private SimpleAdapter mSchedule;
	private boolean btn_Locke = false; 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		setTitle("Historical data");
		findID();

		this.listItem = (ArrayList<HashMap<String, Object>>) getIntent()
				.getExtras().get("HashMap");
		Log.i("listItem_view onCreate", "HashMap size = " + listItem.size());
		// getMap();
		Listeners();

	}

	public void findID() {
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);
		Expotr_btn = (Button) findViewById(R.id.Expotr_btn);

	}
	public void Listeners(){
		
		mSchedule = new SimpleAdapter(this.getBaseContext(),
				this.listItem, R.layout.affichageitem, new String[] { "img",
						"time", "Word" }, new int[] { R.id.img, R.id.titre,
						R.id.description });

		maListViewPerso.setAdapter(mSchedule);

		maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				HashMap<String, String> map = (HashMap<String, String>) maListViewPerso
						.getItemAtPosition(position);

				AlertDialog.Builder adb = new AlertDialog.Builder(
						listItem_view.this);

				adb.setTitle("Selection Item");

				adb.setMessage("time : " + map.get("time") + "\nMessage : "
						+ map.get("Word"));

				adb.setPositiveButton("Ok", null);

				adb.show();
			}
		});

		Expotr_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(btn_Locke == false){
					export_file export = new export_file();
					export.Write_file(listItem);

					if (export.Check_Create_Status() == 0) {
						ShowToast("Create MyFile.txt is Success");
					}

					if (export.Check_Sava_Status() == 0) {
						ShowDialog("Failed to save the SD card");
					} else {
						ShowDialog("Success to save the SD card");
					}
					Log.i("export.Check_Status() ", "Status " + export.Check_Sava_Status());
					btn_Locke = true;
				}else{
					ShowToast("You have been saved!!");
				}
				
			}
		});

	}
	// public void getMap() {
	// Log.i("getMap()", "Map Size = " + listItem.size());
	// for (int i = 0; i < listItem.size(); i++) {
	// HashMap<String, Object> m = listItem.get(i);
	// m.get("time");
	// m.get("Word");
	// Log.i("getMap()", "m.get(time) = " + m.get("time"));
	// Log.i("getMap()", "m.get(Word) = " + m.get("Word"));
	// }
	// }


	private void ShowToast(String str) {
		Toast toast = Toast.makeText(this, str.toString(), Toast.LENGTH_SHORT);
		toast.show();
	}

	private void ShowDialog(String str) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Inform").setMessage(str.toString())
				.setNeutralButton("Exit", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}
}