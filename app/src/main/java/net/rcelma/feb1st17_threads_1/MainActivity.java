package net.rcelma.feb1st17_threads_1;

import android.os.AsyncTask;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv);
	}

	public void onThread(View view) {

		new Thread() {
			@Override
			public void run() {

				try {
					Thread.sleep(500);
					tv.post(new Runnable() {
						@Override
						public void run() {

							tv.setText("Hola Charly");
						}
					});
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}.start();
	}

	public void onThread2(View view) {

		//AsyncTask a way Android of handling threads
		new AsyncTask<Integer, Void, String>() {
			@Override
			protected String doInBackground(Integer... params) {

				try {
					Thread.sleep(params[0]);
					return "Async Task Success!!";
				} catch (InterruptedException e) {
					e.printStackTrace();
					return "Async Err";
				}
			}

			@Override
			protected void onPostExecute(String s) {

				super.onPostExecute(s);
				tv.setText(s);
			}
		}.execute(400);
	}

	public void onThread3(View view) {

		NoTask task = new NoTask();
		task.execute(4000);
	}

	private class NoTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {

			try {
				Thread.sleep(params[0]);
				return "Async Task3 Success!!";
			} catch (InterruptedException e) {
				e.printStackTrace();
				return "Async Err";
			}
		}

		@Override
		protected void onPostExecute(java.lang.String s) {

			tv.setText(s);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////
	public class EventFail {
		private String m;
		private Integer code;

		public String getM() {

			return m;
		}

		public Integer getCode(){
			return code;
		}

		public EventFail(Integer code, String m) {

			this.code = code;
			this.m = m;
		}
	}

	public void onEB(View view) {

		new Thread(){
			@Override
			public void run() {

				android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
				try {
					Thread.sleep(1500);
					EventBus.getDefault().post(new EventFail(747, "Niki is HOT!"));
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}.start();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void fooBar(EventFail fail){
		if(fail.getCode() == 747){
			tv.setText(fail.getM());
		}
	}

	@Override
	protected void onStart() {

		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {

		super.onStop();
		EventBus.getDefault().unregister(this);
	}
}