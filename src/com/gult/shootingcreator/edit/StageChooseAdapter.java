package com.gult.shootingcreator.edit;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by okadakeiko on 13/08/30.
 */
public class StageChooseAdapter extends ArrayAdapter<String>{

    int position;
    public  StageChooseAdapter(Context context,int resource){
        super(context,resource);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        this.position = position;
        View v = super.getView(position,convertView,parent);
        v.setOnClickListener(new RowClickListener(position));
        v.setOnLongClickListener(new RowLongClickListener(position));
        setNotifyOnChange(true);
        
        return v;
    }

    private class RowClickListener implements View.OnClickListener{
        int position;
        String stageName;

       public RowClickListener(int i){
           position = i;
       }
        @Override
        public void onClick(View view) {
            Stage stage = StageContainer.getInstance().getStage(position);
            stageName = stage.getStageName();

            Intent intent = new Intent(getContext(),MainActivity.class);
            intent.putExtra("stageName", stageName);
            intent.putExtra("stageNumber", position);
            intent.putExtra("fromStageChooseActivity", true);
            getContext().startActivity(intent);
        }

    }

    private class RowLongClickListener implements View.OnLongClickListener{

        int position;
        String stageName;

        public RowLongClickListener(int i){
            position = i;

        }

        @Override
        public boolean onLongClick(View v) {
            Stage stage = StageContainer.getInstance().getStage(position);
            stageName = stage.getStageName();

            StageContainer stageContainer = StageContainer.getInstance();
            stageContainer.removeItem(position);
            remove(stageName);
            return true;
        }

    }

}
