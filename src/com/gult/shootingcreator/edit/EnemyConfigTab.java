package com.gult.shootingcreator.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.gult.shootingcreator.R;

/**
 * Created by okadakeiko on 13/09/14.
 */
public class EnemyConfigTab extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        getSupportFragmentManager().beginTransaction().add(R.id.select_enemy_container, new ConfigurationEnemyActivity()).commit();
    }

    public void changeBoss(View v) {
        Fragment fg = new ConfigurationBossActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.select_enemy_container, fg).commit();
    }

    public void changeNormal(View v) {
        Fragment fg = new ConfigurationEnemyActivity();
        getSupportFragmentManager().beginTransaction().replace(R.id.select_enemy_container, fg).commit();
    }


}