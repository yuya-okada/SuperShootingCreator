package com.gult.shootingcreator.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.gult.shootingcreator.App;
import com.gult.shootingcreator.bundle.FileDisplayable;
import com.gult.shootingcreator.bundle.ImageResourceDisplayable;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.foxone.fighter.FighterBase.AttackType;
import com.gult.shootingcreator.foxone.fighter.FighterBase.MoveType;
import com.gult.shootingcreator.foxone.fighter.enemy.BossFighterBase.ConductType;
import com.gult.shootingcreator.foxone.scene.GameSceneStage1.ImageType;

import java.util.ArrayList;

public class ConfigurationBossActivity extends Fragment {
	Context context = App.getContext();
    Intent baseIntent;
	MoveType moveType = MoveType.Not;
	AttackType attackType = AttackType.Not;
	ImageType imageType = ImageType.Frisbee;
    String picturePath;
    Button addButton;
    Button doneButton;
    ListView listView;
    String conduct ="下へ１マス移動";
    ArrayList<ConductType> conductArray = new ArrayList<ConductType>();
    ArrayAdapter<String> adapter;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);


        baseIntent = getActivity().getIntent();
        adapter  = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        return inflater.inflate(R.layout.activity_configuration_boss, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ArrayAdapter<String> adapterConduct = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.spinnerbossType));
        ArrayAdapter<String> adapterImage = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.spinnerimageType));
        addButton = (Button)getView().findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(conduct);
            }
        });
        doneButton = (Button)getView().findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });
        Spinner spinnerImage = (Spinner)getView().findViewById(R.id.image);
        Spinner spinnerConduct = (Spinner)getView().findViewById(R.id.conduct);
        listView = (ListView)getView().findViewById(R.id.listView);

        spinnerConduct.setAdapter(adapterConduct);
        spinnerImage.setAdapter(adapterImage);

        spinnerConduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem .equals("通常")){
                    conduct = "通常の銃弾を発射";
                    conductArray.add(ConductType.Shot);
                }else if (selectedItem .equals("狙撃")){
                    conduct = "プレイヤーの方向に銃弾を発射";
                    conductArray.add(ConductType.SnipeShot);
                }else if (selectedItem .equals("全方位弾")){
                    conduct = "全方向に銃弾を発射";
                    conductArray.add(ConductType.AllDirectionShot);
                }else if (selectedItem .equals("レーザー")){
                    conduct = "レーザーを発射";
                    conductArray.add(ConductType.Laser);
                }else if (selectedItem .equals("何もしない")){
                    conduct = "少しの間、何もせずに待機";
                    conductArray.add(ConductType.Wait);
                }else if (selectedItem .equals("下へ移動")){
                    conduct = "下へ１マス移動";
                    conductArray.add(ConductType.MoveToUnder);
                }else if (selectedItem .equals("上へ移動")){
                    conduct = "上へ１マス移動";
                    conductArray.add(ConductType.MoveToUp);
                }else if (selectedItem .equals("右へ移動")){
                    conduct = "右へ１マス移動";
                    conductArray.add(ConductType.MoveToRight);
                }else if (selectedItem .equals("左へ移動")){
                    conduct = "左へ１マス移動";
                    conductArray.add(ConductType.MoveToLeft);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        }
        );

        spinnerImage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem .equals("円盤A")){
                    imageType = ImageType.Frisbee;
                }else if (selectedItem .equals("円盤B")){
                    imageType = ImageType.FrisbeeYellow;
                }else if (selectedItem .equals("円盤C")){
                    imageType = ImageType.FrisbeeGreen;
                }else if (selectedItem .equals("戦艦A")){
                    imageType = ImageType.Tongari;
                }else if (selectedItem .equals("戦艦B")){
                    imageType = ImageType.TongariPink;
                }else if (selectedItem .equals("戦艦C")){
                    imageType = ImageType.TongariRed;
                }else if (selectedItem .equals("保存された画像")){
                    imageType = ImageType.Custom;
                    IntentToGrid();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        }
        );
    }

    public void add(String conduct){
        adapter.add(conduct);
        listView.setAdapter(adapter);
    }

    public void done(){

		Intent intent = new Intent(getActivity(),MainActivity.class);
		Log.d("","move"+moveType);

        intent.putExtra("EnemyType", "Boss");

		intent.putParcelableArrayListExtra("ConductArray", conductArray);
		intent.putExtra("ImageType", imageType.toString());

        if(imageType == ImageType.Custom){
            intent.putExtra("ImageType", new FileDisplayable(picturePath));
        }else{
            intent.putExtra("ImageType", new ImageResourceDisplayable(imageType));
        }

		intent.putExtra("x", baseIntent.getIntExtra("x",0));
		intent.putExtra("y", baseIntent.getIntExtra("y",0));
	    getActivity().setResult(getActivity().RESULT_OK,intent);
		getActivity().finish();

	}
    public void IntentToGrid(){
        Intent intent = new Intent(getActivity(),GridActivity.class);
        startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestcode,int resultcode ,Intent data){

        if(resultcode == getActivity().RESULT_OK){
            System.out.println(data.hasExtra("picturePath"));
            picturePath = data.getStringExtra("picturePath");
            //System.out.println("picturePath: " + picturePath);
        }
    }
}