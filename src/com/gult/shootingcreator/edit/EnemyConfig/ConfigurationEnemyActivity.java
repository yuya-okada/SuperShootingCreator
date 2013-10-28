package com.gult.shootingcreator.edit.EnemyConfig;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gult.shootingcreator.App;
import com.gult.shootingcreator.R;
import com.gult.shootingcreator.bundle.FileDisplayable;
import com.gult.shootingcreator.bundle.ImageResourceDisplayable;
import com.gult.shootingcreator.edit.CustomAdapter;
import com.gult.shootingcreator.edit.ImageList;
import com.gult.shootingcreator.edit.MainActivity;
import com.gult.shootingcreator.foxone.fighter.FighterBase.AttackType;
import com.gult.shootingcreator.foxone.fighter.FighterBase.MoveType;
import com.gult.shootingcreator.foxone.scene.GameSceneStage1.ImageType;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationEnemyActivity extends Fragment {
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
        ArrayAdapter<String> adapterMove = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.spinnermoveType));
        ArrayAdapter<String> adapterAttack = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.spinnerattackType));
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
        spinnerMove.setAdapter(adapterMove);
        spinnerAttack.setAdapter(adapterAttack);

        Bitmap image[]=new Bitmap[7];
        image[0] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_00);
        image[1] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_00_g);
        image[2] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_00_y);
        image[3] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_01);
        image[4] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_01_p);
        image[5] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_01_r);
        image[6] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_01_r);

        List<ImageList> objects = new ArrayList<ImageList>();
        ImageList item[] = new ImageList[7];


        for(int i=0;i<7;i++){
            item[i]=new ImageList();

            item[i].setImagaData(image[i]);
        }
        item[0].setTextData("円盤A");
        item[1].setTextData("円盤B");
        item[2].setTextData("円盤C");
        item[3].setTextData("戦艦A");
        item[4].setTextData("戦艦B");
        item[5].setTextData("戦艦D");
        item[6].setTextData("保存された画像");

        for(int i=0;i<7;i++){
            objects.add(item[i]);
        }

        CustomAdapter customAdapater = new CustomAdapter(getActivity(), 0, objects);
        spinnerImage.setAdapter(customAdapater);



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

        intent.putExtra("EnemyType", "Normal");
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