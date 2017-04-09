package ds.dbtests

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.*

class ChartActivity : AppCompatActivity(), OnChartValueSelectedListener {

	private lateinit var chart: BarChart

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chart)
		chart = findViewById(R.id.chart) as BarChart

		chart.setOnChartValueSelectedListener(this)

		chart.setDescription("")

		// scaling can now only be done on x- and y-axis separately
		chart.setPinchZoom(false)

		chart.setDrawBarShadow(false)

		chart.setDrawGridBackground(false)

		val l = chart.legend
		l.position = Legend.LegendPosition.ABOVE_CHART_CENTER

		val leftAxis = chart.axisLeft
		//leftAxis.valueFormatter = LargeValueFormatter()
		leftAxis.setDrawGridLines(false)

		val xAxis = chart.xAxis
		xAxis.position = XAxis.XAxisPosition.TOP_INSIDE

		chart.axisRight.isEnabled = false

		setData()
	}

	private fun setData() {

		val tests = ChartData.results
		if (tests.size == 0)
			return

		tests.sortByDescending { it.readValue }

		val xVals = ArrayList<String>()
		val writeVals = ArrayList<BarEntry>()
		val readVals = ArrayList<BarEntry>()

		for ((index, t) in tests.withIndex()) {
			writeVals.add(BarEntry(t.writeValue.toFloat(), index, t.writeMessage))
			readVals.add(BarEntry(t.readValue.toFloat(), index, t.readMessage))
			xVals.add(t.title!!)
		}

		val writeSet = BarDataSet(writeVals, "Write")
		writeSet.color = ContextCompat.getColor(this, R.color.red)
		val readSet = BarDataSet(readVals, "Read")
		readSet.color = ContextCompat.getColor(this, R.color.green)

		val dataSets = ArrayList<IBarDataSet>()
		dataSets.add(readSet)
		dataSets.add(writeSet)

		val data = BarData(xVals, dataSets)
		data.setValueTextSize(10f)
		chart.data = data
		chart.animateY(1000)
		chart.invalidate()
	}

	override fun onValueSelected(e: Entry, dataSetIndex: Int, h: Highlight) {
		Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex)
	}

	override fun onNothingSelected() {
		Log.i("Activity", "Nothing selected.")
	}
}