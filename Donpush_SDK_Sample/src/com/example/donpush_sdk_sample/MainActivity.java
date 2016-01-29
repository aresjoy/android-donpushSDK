package com.example.donpush_sdk_sample;

import org.json.JSONException;
import org.json.JSONObject;

import com.game.miniram.donpush.base.Result;
import com.game.miniram.donpush.game.DonpushSDK;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity implements android.view.View.OnClickListener{
	
	TextView mResult;
	
	DonpushSDK donpushSDK;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mResult=(TextView) findViewById(R.id.result);
		findViewById(R.id.getCoin).setOnClickListener(this);
		findViewById(R.id.getUserinfo).setOnClickListener(this);
		findViewById(R.id.getScore).setOnClickListener(this);
		findViewById(R.id.getRanking).setOnClickListener(this);
		findViewById(R.id.logout).setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.check_login).setOnClickListener(this);
		findViewById(R.id.putScore).setOnClickListener(this);
		
		
		if(CheckPermission_request(new String[]{Manifest.permission.READ_PHONE_STATE},1)) return ;
		
		donpushSDK=new DonpushSDK(this);
		donpushSDK.init("aaaaabbbbccccdd",true);
		
		
	}
	

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case 1:

                for (int result:grantResults){
                    if(result!= PackageManager.PERMISSION_GRANTED){
                    	
                    	Toast.makeText(this, "DonpushSDK initial Failed!! ,You can't use SDK (READ_PHONE_STATE is not allowed)", Toast.LENGTH_LONG).show();
                    	
                        return;
                    }
                }


                break;

        }

    }
	
	
	
	
	 /**
     *  Only for   23API ,check manifest
     * @param manifests
     * @param requestCode
     * @return   true means  need request permission
     */
    public boolean CheckPermission_request(String[] manifests,int requestCode){

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            // in  <23  sdk   no need check ,and request
            return false;
        }


        boolean result=true;

        for(String manifest: manifests){

            result=result&&!isPermissioned(manifest);
        }

        if(result){

            ActivityCompat.requestPermissions(this,manifests,requestCode);
        }

        return result;
    }


    
    /**
     * Check permission is granted
     * @param manifest_permission
     * @return
     */
    public boolean isPermissioned(String manifest_permission){
    		
        return ActivityCompat.checkSelfPermission(this,manifest_permission) == PackageManager.PERMISSION_GRANTED;

    }
	 
	  
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public void onClick(View v) {
		boolean status=false;
		switch (v.getId()) {
		case R.id.check_login:
			status=donpushSDK.isLogined();
			Toast.makeText(MainActivity.this,"isLogined:"+status, Toast.LENGTH_SHORT).show();
			break;
		case R.id.logout:
			status=donpushSDK.logout();
			Toast.makeText(MainActivity.this,"logout:"+status, Toast.LENGTH_SHORT).show();
			break;
		case R.id.login:
			status=donpushSDK.login();
			Toast.makeText(MainActivity.this,"login:"+status, Toast.LENGTH_SHORT).show();
			break;
		case R.id.putScore:
			donpushSDK.put_score("1000", "1111", "Myname","KR",result);
			break;
		case R.id.getScore:
			donpushSDK.get_score(result);
			break;
		case R.id.getRanking:
			donpushSDK.get_ranking(result);
			break;
		case R.id.getCoin:
			donpushSDK.give_bonus_coin(result);
			break;
		case R.id.getUserinfo:
			donpushSDK.get_user_info(result);
			break;

		default:
			break;
		}
	}
	
	
	Result result=new Result() {
		@Override
		public void result(final JSONObject arg0) {
			if(arg0!=null){
				 
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								mResult.setText(arg0.toString(4));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				
			}
		}
	};
	
}
