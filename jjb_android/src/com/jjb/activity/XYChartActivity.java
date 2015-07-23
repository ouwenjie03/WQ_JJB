package com.jjb.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jjb.R;
import com.jjb.widget.MyDouble;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class XYChartActivity extends Activity implements ViewPager.OnPageChangeListener {
	
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mXYRenderer = new XYMultipleSeriesRenderer();
	/** The most recently added series. */
	private XYSeries mCurrentSeries;
	/** The most recently created renderer, customizing the current series. */
	private XYSeriesRenderer mCurrentRenderer;
	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	/** Colors to be used for the pie slices. */
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };
	/** The main series that will include all the data. */
	private CategorySeries mSeries1 = new CategorySeries("");
	private CategorySeries mSeries2 = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mPieRenderer1 = new DefaultRenderer();
	private DefaultRenderer mPieRenderer2 = new DefaultRenderer();

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;
	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;
	/**
	 * 装ImageView数组
	 */
	private GraphicalView[] mGraphicalViews = new GraphicalView[3];
	
	private String[] month2Str = {"Jan","Feb","Mar",
			"Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  // save the current data, for instance when changing screen orientation
	  outState.putSerializable("dataset", mDataset);
	  outState.putSerializable("renderer", mXYRenderer);
	  outState.putSerializable("current_series", mCurrentSeries);
	  outState.putSerializable("current_renderer", mCurrentRenderer);

		outState.putSerializable("current_series1", mSeries1);
		outState.putSerializable("current_series2", mSeries2);
		outState.putSerializable("current_renderer1", mPieRenderer1);
		outState.putSerializable("current_renderer2", mPieRenderer2);
	}
	  
	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
	  super.onRestoreInstanceState(savedState);
	  // restore the current data, for instance when changing the screen
	  // orientation
	  mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
	  mXYRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
	  mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
	  mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");

	  mSeries1 = (CategorySeries) savedState.getSerializable("current_series1");
	  mSeries2 = (CategorySeries) savedState.getSerializable("current_series2");
	  mPieRenderer1 = (DefaultRenderer) savedState.getSerializable("current_renderer1");
	  mPieRenderer2 = (DefaultRenderer) savedState.getSerializable("current_renderer2");
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xychart);


		ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		//将点点加入到ViewGroup中
		tips = new ImageView[3];
		for(int i=0; i<tips.length; i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
			lp.setMargins(10, 0, 10, 0);
			imageView.setLayoutParams(lp);
			tips[i] = imageView;
			if(i == 0){
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			}else{
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageView);
		}


		//设置Adapter
		viewPager.setAdapter(new MyAdapter());
		//设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		//设置ViewPager的默认项
		viewPager.setCurrentItem(0);


		//創建折綫圖
        String[] titles = new String[] { "支出", "收入" };
        
        // create a new renderer for the new series
        int[] colors = new int[] { Color.BLUE, Color.GREEN };
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND};
		mXYRenderer.setAxisTitleTextSize(16);
		mXYRenderer.setChartTitleTextSize(20);
		mXYRenderer.setLabelsTextSize(15);
		mXYRenderer.setLegendTextSize(15);
		mXYRenderer.setPointSize(5f);
        //mXYRenderer.setMargins(new int[] { 20, 30, 15, 20 });
        for (int i = 0; i < colors.length; i++) {
          XYSeriesRenderer renderer = new XYSeriesRenderer();
          renderer.setColor(colors[i]);
          renderer.setPointStyle(styles[i]);
          renderer.setFillPoints(true);
          renderer.setDisplayChartValues(true);
          mXYRenderer.addSeriesRenderer(renderer);
        }

		mXYRenderer.setChartTitle("daily income and spending");
		mXYRenderer.setXTitle("\nDate");
		mXYRenderer.setYTitle("money");
		mXYRenderer.setAxesColor(Color.LTGRAY);
		mXYRenderer.setLabelsColor(Color.LTGRAY);
		mXYRenderer.setShowGrid(true);

		mXYRenderer.setXLabels(0);
		mXYRenderer.setYLabels(10);
		mXYRenderer.setXLabelsAlign(Align.RIGHT);
		mXYRenderer.setYLabelsAlign(Align.RIGHT);
		mXYRenderer.setZoomButtonsVisible(true);
        
        
        //dataset
        List<double[]> keys = new ArrayList<double[]>();
        List<double[]> values = new ArrayList<double[]>();
        
        Bundle bundle = getIntent().getExtras();
		//price in every item
		ArrayList<MyDouble> allMyDoublesOut = (ArrayList<MyDouble>) getIntent().getSerializableExtra("allMyDoublesOut");
		//date in every item，format： 2015-06-17
		ArrayList<String> allMyTimeStrings = (ArrayList<String>) getIntent().getSerializableExtra("allMyTimeStrings");
				
		String thisDate = "",reg = "-";
		Calendar calendar = Calendar.getInstance();
		int myDoubleLengthOut = allMyDoublesOut.size();
		//支出的dataset
		double allSpendingPrice[] = new double[myDoubleLengthOut];
		double allSpendingKeys[] = new double[myDoubleLengthOut];
		for (int i = 0;i < myDoubleLengthOut;i++) {
			calendar.set(1970,0,1);
	        int startYear = calendar.get(Calendar.YEAR);
	        int startDayInYear = calendar.get(Calendar.DAY_OF_YEAR);
//			thisDate = allMyTimeStrings.get(i);
//			String[] f = thisDate.trim().split(reg);
//	        calendar.set(Integer.parseInt (f[0]), Integer.parseInt (f[1]) - 1, Integer.parseInt (f[2]));
//	        long fd2 = calendar.getTime().getTime();
//			int index = Integer.parseInt("" + (fd2 - 0) / 100000000);
			
			thisDate = allMyTimeStrings.get(i);
			String[] f = thisDate.trim().split(reg);
	        calendar.set(Integer.parseInt (f[0]), Integer.parseInt (f[1]) - 1, Integer.parseInt (f[2]));
	        int thisYear = calendar.get(Calendar.YEAR);
	        int thisDayInYear = calendar.get(Calendar.DAY_OF_YEAR);
	        int index = 0;
	        while(startYear < thisYear)  
            {  
                if(startYear%400 == 0 || (startYear%4 == 0 && startYear%100 != 0))
                {  
                	index += 366;  
                } else {
                	index += 365;
                }
                ++startYear;  
            } 
	        index += (thisDayInYear - startDayInYear);
			
			allSpendingPrice[i] = allMyDoublesOut.get(i).count;
			allSpendingKeys[i] = index;
			mXYRenderer.addXTextLabel(index, month2Str[Integer.parseInt (f[1]) - 1] + "\n" + f[2]);
        }
        values.add(allSpendingPrice);
        keys.add(allSpendingKeys);

        
		//price in every item
		ArrayList<MyDouble> allMyDoublesIn = (ArrayList<MyDouble>) getIntent().getSerializableExtra("allMyDoublesIn");
		
		//收入的dataset
		int myDoubleLengthIn = allMyDoublesIn.size();
		double allIncomePrice[] = new double[myDoubleLengthIn];
		double allIncomeKeys[] = new double[myDoubleLengthIn];
		for (int i = 0;i < myDoubleLengthIn;i++) {
			calendar.set(1970,0,1);
	        int startYear = calendar.get(Calendar.YEAR);
	        int startDayInYear = calendar.get(Calendar.DAY_OF_YEAR);
//			thisDate = allMyTimeStrings.get(i);
//			String[] f = thisDate.trim().split(reg);
//	        calendar.set(Integer.parseInt (f[0]), Integer.parseInt (f[1]) - 1, Integer.parseInt (f[2]));
//	        long fd2 = calendar.getTime().getTime();
//			int index = Integer.parseInt("" + (fd2 - 0) / 100000000);
//			
			thisDate = allMyTimeStrings.get(i);
			String[] f = thisDate.trim().split(reg);
	        calendar.set(Integer.parseInt (f[0]), Integer.parseInt (f[1]) - 1, Integer.parseInt (f[2]));
	        int thisYear = calendar.get(Calendar.YEAR);
	        int thisDayInYear = calendar.get(Calendar.DAY_OF_YEAR);
	        int index = 0;
	        while(startYear < thisYear)  
            {  
                if(startYear%400 == 0 || (startYear%4 == 0 && startYear%100 != 0))
                {  
                	index += 366;  
                } else {
                	index += 365;
                }
                ++startYear;  
            } 
	        index += (thisDayInYear - startDayInYear);
	        
			allIncomePrice[i] = allMyDoublesIn.get(i).count;
			allIncomeKeys[i] = index;
			mXYRenderer.addXTextLabel(index, month2Str[Integer.parseInt (f[1]) - 1] + "\n" + f[2]);
        }
        values.add(allIncomePrice);
        keys.add(allIncomeKeys);
        
        for (int i = 0; i < titles.length; i++) {
          XYSeries series = new XYSeries(titles[i], 0);
          double[] xV = keys.get(i);
          double[] yV = values.get(i);
          for (int k = 0; k < xV.length; k++) {
            series.add(xV[k], yV[k]);
          }
          mDataset.addSeries(series);
          mCurrentSeries = series;
        }
        
        //以下代码必须在render后面
        //LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
    	mChartView = ChartFactory.getLineChartView(this, mDataset, mXYRenderer);
    	//layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		mGraphicalViews[0] = mChartView;
		//mChartView.repaint();

		//支出饼图
		String name1[] = {"clothesOut","dietOut","shelterOut","activityOut"};
		String chineseName1[] = {"衣","食","住","行"};
		double price1[] = new double[4];
		for (int i = 0;i < 4;i++)
		{
			price1[i] = bundle.getDouble(name1[i]);

			mSeries1.add(chineseName1[i], price1[i]);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries1.getItemCount() - 1) % COLORS.length]);
			mPieRenderer1.addSeriesRenderer(renderer);
		}

		mPieRenderer1.setZoomButtonsVisible(true);
		mPieRenderer1.setStartAngle(180);
		mPieRenderer1.setDisplayValues(true);
		mPieRenderer1.setChartTitleTextSize(30);
		mPieRenderer1.setChartTitle("分类支出饼图");
		mPieRenderer1.setAxesColor(Color.RED);
		mPieRenderer1.setLegendTextSize(30);
		mPieRenderer1.setLabelsColor(Color.BLACK);
		mPieRenderer1.setLabelsTextSize(30);
		//mPieRenderer.setExternalZoomEnabled(false);
		//mPieRenderer.setFitLegend(false);
		mPieRenderer1.setPanEnabled(false);
		//mPieRenderer.setFitLegend(false);
		mPieRenderer1.setZoomButtonsVisible(true);
		mPieRenderer1.setZoomEnabled(false);//放大缩小
		//mPieRenderer.setScale(2);
		//mPieRenderer.setMargins(new int[]{0,0,0,10});


		//LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		mChartView = ChartFactory.getPieChartView(this, mSeries1, mPieRenderer1);
		mChartView.setPadding(0,0,0,10);
		//layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mGraphicalViews[1] = mChartView;

		//mChartView.repaint();

		//收入饼图
		String name2[] = {"clothesIn","dietIn","shelterIn","activityIn"};
		String chineseName2[] = {"衣","食","住","行"};
		double price2[] = new double[4];
		for (int i = 0;i < 4;i++)
		{
			price2[i] = bundle.getDouble(name2[i]);

			mSeries2.add(chineseName2[i], price2[i]);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries2.getItemCount() - 1) % COLORS.length]);
			mPieRenderer2.addSeriesRenderer(renderer);
		}

		mPieRenderer2.setZoomButtonsVisible(true);
		mPieRenderer2.setStartAngle(180);
		mPieRenderer2.setDisplayValues(true);
		mPieRenderer2.setChartTitleTextSize(30);
		mPieRenderer2.setChartTitle("分类收入饼图");
		mPieRenderer2.setAxesColor(Color.RED);
		mPieRenderer2.setLegendTextSize(30);
		mPieRenderer2.setLabelsColor(Color.BLACK);
		mPieRenderer2.setLabelsTextSize(30);
		//mPieRenderer.setExternalZoomEnabled(false);
		//mPieRenderer.setFitLegend(false);
		mPieRenderer2.setPanEnabled(false);
		//mPieRenderer.setFitLegend(false);
		mPieRenderer2.setZoomButtonsVisible(true);
		mPieRenderer2.setZoomEnabled(false);//放大缩小
		//mPieRenderer.setScale(2);
		//mPieRenderer.setMargins(new int[]{0,0,0,10});


		//LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		mChartView = ChartFactory.getPieChartView(this, mSeries2, mPieRenderer2);
		mChartView.setPadding(0,0,0,10);
		//layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mGraphicalViews[2] = mChartView;

		//mChartView.repaint();
    }

	@Override
	protected void onResume() {
		super.onResume();
	    if (mChartView == null) {
	    	//LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
	    	mChartView = ChartFactory.getLineChartView(this, mDataset, mXYRenderer);
	    	// enable the chart click events
			mXYRenderer.setClickEnabled(true);
			mXYRenderer.setSelectableBuffer(10);
	    	mChartView.setOnClickListener(new View.OnClickListener() {
	    		public void onClick(View v) {
	    			// handle the click event on the chart
	    			SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	    			if (seriesSelection == null) {
	    				Toast.makeText(XYChartActivity.this, "No chart element", Toast.LENGTH_SHORT).show();
	    			} else {
	    				// display information of the clicked point
	    				Toast.makeText(
	    						XYChartActivity.this,
	    						"Chart element in series index " + seriesSelection.getSeriesIndex()
	    						+ " data point index " + seriesSelection.getPointIndex() + " was clicked"
	    						+ " closest point value X=" + seriesSelection.getXValue() + ", Y="
	    						+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
	    			}
	    		}
	    	});
	    	//layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			//LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
//			mChartView = ChartFactory.getPieChartView(this, mSeries1, mPieRenderer1);
//			mPieRenderer1.setClickEnabled(true);
//			mChartView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
//					if (seriesSelection == null) {
//						Toast.makeText(XYChartActivity.this, "No chart element selected", Toast.LENGTH_SHORT)
//								.show();
//					} else {
//						for (int i = 0; i < mSeries1.getItemCount(); i++) {
//							mPieRenderer1.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
//						}
//						mChartView.repaint();
//						Toast.makeText(
//								XYChartActivity.this,
//								"Chart data point index " + seriesSelection.getPointIndex() + " selected"
//										+ " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
			//layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    } else {
	    	//mChartView.repaint();
	    }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	/**
	 *
	 * @author xiaanming
	 *
	 */
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mGraphicalViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mGraphicalViews[position % mGraphicalViews.length]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mGraphicalViews[position % mGraphicalViews.length], 0);
			return mGraphicalViews[position % mGraphicalViews.length];
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0 % mGraphicalViews.length);
	}
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev) ;
  
        return result;
    }
 
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 
        final int action = MotionEventCompat.getActionMasked(ev);
        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Do not intercept touch event, let the child handle it
            return false;
        }
 
        //TouchUtils.showEventInfo(TAG + "#   onInterceptTouchEvent", action);
 
     
        return false;
    }
 
    /*@Override
    public boolean onTouchEvent(MotionEvent ev) {
        //TouchUtils.showEventInfo(TAG + "# *** onTouchEvent", ev.getAction());
        Log.d(TAG, "### is Clickable = " + isClickable());
         return super.onTouchEvent(ev);
//        return true;
    }*/

	/**
	 * 设置选中的tip的背景
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems){
		for(int i=0; i<tips.length; i++){
			if(i == selectItems){
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			}else{
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

}
