package com.example.trackablehabit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {
    private Context mContext;
    private ArrayList<Integer> habit_id, habit_count;
    private ArrayList<String> date_array, habit_name;
    private HabitDBHelper habitDBHelper;

    StatsAdapter(Context context, ArrayList<String> date_array, ArrayList<Integer> habit_id,
                 ArrayList<String> habit_name, ArrayList<Integer> habit_count) {
        mContext = context;
        this.date_array = date_array;
        this.habit_id = habit_id;
        this.habit_name = habit_name;
        this.habit_count = habit_count;
    }

    class StatsViewHolder extends RecyclerView.ViewHolder {
        BarChart barChart;

        StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            barChart = itemView.findViewById(R.id.statsBarChart);
        }
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.statistics_graph_individual, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        int habitID = habit_id.get(position);
        String habitName = habit_name.get(position);
        addDataToGraph(holder.barChart, habitID, habitName);
        holder.barChart.invalidate();
    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }

    private void addDataToGraph(BarChart habit_chart, int habitID, String habitName) {
        habitDBHelper = new HabitDBHelper(mContext);

        ArrayList<Integer> indexArray = filterByHabit(habitID);

        final ArrayList<BarEntry> yVals = new ArrayList<>();

        for (int i = 0; i < indexArray.size(); i++) {
            int index = indexArray.get(i);
            int count = habit_count.get(index);
            BarEntry newBarEntry = new BarEntry(i, count);
            yVals.add(newBarEntry);
        }

        final ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < indexArray.size(); i++) {
            int index = indexArray.get(i);
            String date = date_array.get(index);
            xVals.add(date);
        }

        BarDataSet dataSet = new BarDataSet(yVals, habitName);

        ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
        dataSets1.add(dataSet);

        BarData data = new BarData(dataSets1);

        // Formatting
        habit_chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        habit_chart.setData(data);

        XAxis xAxis = habit_chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.isCenterAxisLabelsEnabled();
        xAxis.setGranularityEnabled(true);

        YAxis rightAxis = habit_chart.getAxisRight();
        rightAxis.setEnabled(false);

        habit_chart.setMaxVisibleValueCount(5);
        habit_chart.setFitBars(true);
    }

    private ArrayList<Integer> filterByHabit(int habitID) {
        ArrayList<Integer> indexArray = new ArrayList<>();
        for (int i = 0; i < habit_id.size(); i++) {
            if (habit_id.get(i).equals(habitID)) {
                indexArray.add(i);
            }
        }
        return indexArray;
    }
}
