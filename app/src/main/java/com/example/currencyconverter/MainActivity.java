package com.example.currencyconverter;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new PagerAdapter(getSupportFragmentManager(), MainActivity.this));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


}
