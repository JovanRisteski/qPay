package mk.grabit.gpay.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import mk.grabit.gpay.R
import mk.grabit.gpay.databinding.FragmentHomeBinding
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnChartValueSelectedListener {

    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)

        val transactionAdapter = TransactionAdapter {
            Toast.makeText(
                requireContext(),
                "Transaction id: ${it.paymentId}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding?.transactionRecyclerViewAdapter?.adapter = transactionAdapter

        setPieChart()
        setLineChart()

        observeUi(transactionAdapter)
    }

    private fun observeUi(transactionAdapter: TransactionAdapter) {

        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            transactionAdapter.submitList(it.take(5)) {
                viewModel.setIsLoading(false)
                binding?.transactionRecyclerViewAdapter?.smoothScrollToPosition(0);
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding?.progressBar?.visibility = View.VISIBLE
                binding?.viewGroup?.visibility = View.GONE
            } else {
                binding?.progressBar?.visibility = View.GONE
                binding?.viewGroup?.visibility = View.VISIBLE
            }
        })
    }

    private fun setPieChart() {
        val entries = arrayListOf(
            PieEntry(9920.00f, "April"),
            PieEntry(10991.00f, "May"),
            PieEntry(12120.0f, "June"),
            PieEntry(6340.0f, "July")
        )
        binding?.pieChart?.setUsePercentValues(false)
        binding?.pieChart?.description?.isEnabled = true

        binding?.pieChart?.isDrawHoleEnabled = true
        binding?.pieChart?.setHoleColor(Color.WHITE)

        binding?.pieChart?.setTransparentCircleColor(Color.WHITE)
        binding?.pieChart?.setTransparentCircleAlpha(110)

        binding?.pieChart?.holeRadius = 58f
        binding?.pieChart?.transparentCircleRadius = 61f

        binding?.pieChart?.setDrawCenterText(true)

        binding?.pieChart?.rotationAngle = 0f
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        binding?.pieChart?.isRotationEnabled = true
        binding?.pieChart?.isHighlightPerTapEnabled = true

        // add a selection listener
        binding?.pieChart?.setOnChartValueSelectedListener(this)

        binding?.pieChart?.animateY(1400, Easing.EaseInOutQuad)

        val l: Legend = binding?.pieChart?.legend!!
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.isEnabled = false

        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(), R.color.gray_300))
        colors.add(ContextCompat.getColor(requireContext(), R.color.red_200))
        colors.add(ContextCompat.getColor(requireContext(), R.color.purple_200))
        colors.add(ContextCompat.getColor(requireContext(), R.color.blue_300))
        dataSet.colors = colors

        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        binding?.pieChart?.data = data

        // undo all highlights
        binding?.pieChart?.highlightValues(null)

        binding?.pieChart?.invalidate()
    }

    private fun setLineChart() {

        val values =
            ArrayList<Entry>()

        for (i in 0 until 4) {
            val num = (Math.random() * (2 + 1)) + 3
            values.add(Entry(i * 0.001f, num.toFloat()))
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "DataSet 1")

        set1.color = Color.BLACK
        set1.lineWidth = 0.5f
        set1.setDrawValues(false)
        set1.setDrawCircles(false)
        set1.mode = LineDataSet.Mode.LINEAR
        set1.setDrawFilled(false)

        // create a data object with the data sets
        val data = LineData(set1)

        // set data
        binding?.lineChart?.data = data

        // get the legend (only possible after setting data)
        val l: Legend = binding?.lineChart?.getLegend()!!
        l.isEnabled = true
    }


    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
}