package ramanathan.pascal.motionmeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ramanathan.pascal.motionmeter.model.Event;

public class ChartOwnerEventActivity extends AppCompatActivity {

    Event event;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_owner_event);

        chart = findViewById(R.id.chart);

        event = (Event) getIntent().getSerializableExtra("event");
        fillData();

        //chart.setBackgroundColor(getResources().getColor()); // use your bg color
        chart.setDescription(new Description());
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        chart.setAutoScaleMinMaxEnabled(true);

        // remove axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setLabelCount(10);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        // hide legend
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        chart.invalidate();
    }


    public void fillData(){
        List<Entry> entries = new ArrayList<Entry>();
        int i = 1;
        int sum = 0;
        for(Map.Entry<String, Integer> e:event.getBewertung().entrySet()){
            sum += e.getValue();
            sum = sum / i;
            entries.add(new Entry(i,sum));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Zufriedenheit");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
}
