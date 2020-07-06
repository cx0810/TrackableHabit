package com.example.trackablehabit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    private BarChart barChart;
    private SQLiteDatabase habitDatabase;
    private HabitDBHelper habitDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Statistics");
        }

        barChart = findViewById(R.id.barchart);

        addDataToGraph();
        barChart.invalidate();
    }

    public void addDataToGraph() {
        habitDBHelper = new HabitDBHelper(this);

        final ArrayList<BarEntry> yVals = new ArrayList<>();
        final ArrayList<String> yData = habitDBHelper.queryYData();

        for (int i = 0; i < yData.size(); i++) {
            BarEntry newBarEntry = new BarEntry(i, Float.parseFloat(habitDBHelper.queryYData().get(i)));
            yVals.add(newBarEntry);
        }

        final ArrayList<String> xVals = new ArrayList<>();
        final ArrayList<String> xData = habitDBHelper.queryXData();

        for (int i = 0; i < habitDBHelper.queryXData().size(); i++) {
            xVals.add(xData.get(i));
        }

        BarDataSet dataSet = new BarDataSet(yVals, "Statistics Graph");

        ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
        dataSets1.add(dataSet);

        BarData data = new BarData(dataSets1);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.isCenterAxisLabelsEnabled();
        xAxis.setGranularityEnabled(true);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.setMaxVisibleValueCount(5);
        barChart.setFitBars(true);
    }
}
