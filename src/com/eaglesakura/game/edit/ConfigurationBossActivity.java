package com.eaglesakura.game.edit;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.eaglesakura.game.App;
import com.eaglesakura.game.bundle.FileDisplayable;
import com.eaglesakura.game.bundle.ImageResourceDisplayable;
import com.eaglesakura.game.foxone.R;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.AttackType;
import com.eaglesakura.game.foxone.fighter.enemy.EnemyFighterBase.MoveType;
import com.eaglesakura.game.foxone.scene.GameSceneStage1.ImageType;

public class ConfigurationBossActivity extends Fragment {
	Context context = App.getContext();
    Intent baseIntent;
	MoveType moveType = MoveType.Not;
	AttackType attackType = AttackType.Not;
	ImageType imageType = ImageType.Frisbee;
    String picturePath;
    Button doneButton;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);


        baseIntent = getActivity().getIntent();



        return inflater.inflate(R.layout.activity_configuration_enemy, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        doneButton = (Button)getView().findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Done();
            }
        });

        Spinner spinnerMove = (Spinner)getView().findViewById(R.id.spinner1);
        Spinner spinnerAttack = (Spinner)getView().findViewById(R.id.spinner2);
        Spinner spinnerImage = (Spinner)getView().findViewById(R.id.spinner3);

        spinnerMove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("直進")){
                    moveType = MoveType.Straight;
                }else if (selectedItem.equals("波線")){
                    moveType = MoveType.Curved;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        }
        );

        spinnerAttack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem .equals("通常")){
                    attackType = AttackType.ShotStraight;
                }else if (selectedItem .equals("狙撃")){
                    attackType = AttackType.Snipe;
                }else if (selectedItem .equals("全方位弾")){
                    attackType = AttackType.AllDirection;
                }else if (selectedItem .equals("レーザー")){
                    attackType = AttackType.Laser;
                }else if (selectedItem .equals("全方位弾＆レーザー")){
                    attackType = AttackType.Snipe;
                }else if (selectedItem .equals("何もしない")){
                    attackType = AttackType.Not;
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

    public void Done(){

		Intent intent = new Intent(getActivity(),MainActivity.class);
		Log.d("","move"+moveType);

		intent.putExtra("MoveType", moveType.toString());
		intent.putExtra("AttackType", attackType.toString());
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