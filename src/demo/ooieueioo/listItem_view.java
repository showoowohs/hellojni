package demo.ooieueioo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class listItem_view extends Activity {
	private ListView maListViewPerso;
	private ArrayList<HashMap<String, Object>> listItem;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		setTitle("Historical data");
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);
		this.listItem = (ArrayList<HashMap<String, Object>>) getIntent()
				.getExtras().get("HashMap");
		Log.i("listItem_view onCreate", "HashMap size = " + listItem.size());
		// getMap();

		SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(),
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

	}

	public void getMap() {
		Log.i("getMap()", "Map Size = " + listItem.size());
		for (int i = 0; i < listItem.size(); i++) {
			HashMap<String, Object> m = listItem.get(i);
			m.get("time");
			m.get("Word");
			Log.i("getMap()", "m.get(time) = " + m.get("time"));
			Log.i("getMap()", "m.get(Word) = " + m.get("Word"));
		}
	}
}