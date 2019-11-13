package com.taskapp.onBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.taskapp.MainActivity;
import com.taskapp.R;

public class OnBoardActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        ViewPager viewPager = findViewById(R.id.viewPager);
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,true);






    }




    public void skipBtnClick(View view) {
       Intent intent = new Intent(this,MainActivity.class);
       startActivity(intent);
       finish();
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isShown",true).apply();
    }


    public class SectionPagerAdapter extends FragmentPagerAdapter{

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            BoardFragment boardFragment = new BoardFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("pos",position);
            boardFragment.setArguments(bundle);
            return boardFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
