package ds.dbtests;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.*;

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        chart = (BarChart) findViewById(R.id.chart);

        chart.setOnChartValueSelectedListener(this);

        chart.setDescription("DB tests result");

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        setData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IBarDataSet set : chart.getData().getDataSets()) { set.setDrawValues(!set.isDrawValuesEnabled()); }

                chart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (chart.isPinchZoomEnabled())
                    chart.setPinchZoom(false);
                else
                    chart.setPinchZoom(true);

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleHighlight: {
                if(chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                    chart.invalidate();
                }
                break;
            }
            case R.id.actionToggleHighlightArrow: {
                if (chart.isDrawHighlightArrowEnabled())
                    chart.setDrawHighlightArrow(false);
                else
                    chart.setDrawHighlightArrow(true);
                chart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // chart.saveToGallery("title"+System.currentTimeMillis());
                chart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.animateX: {
                chart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                chart.animateXY(3000, 3000);
                break;
            }
        }*/
        return true;
    }

    private void setData() {

        Map<String, TestResult> tests = ChartData.INSTANCE.getResults();
        int size = tests.size();
        if (size == 0)
            return;

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> writeVals = new ArrayList<>();
        ArrayList<BarEntry> readVals = new ArrayList<>();


        int index = 0;
        for (Map.Entry<String, TestResult> e : tests.entrySet()) {
            writeVals.add(new BarEntry(e.getValue().getWriteValue(), index, e.getValue().getWriteMessage()));
            readVals.add(new BarEntry(e.getValue().getReadValue(), index, e.getValue().getReadMessage()));
            xVals.add(e.getKey());
            index++;
        }

        BarDataSet writeSet = new BarDataSet(writeVals, "Write");
        writeSet.setColor(ContextCompat.getColor(this, R.color.red));
        BarDataSet readSet = new BarDataSet(readVals, "Read");
        readSet.setColor(ContextCompat.getColor(this, R.color.green));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(readSet);
        dataSets.add(writeSet);

        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}