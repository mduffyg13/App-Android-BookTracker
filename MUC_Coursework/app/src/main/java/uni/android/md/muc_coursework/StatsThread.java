package uni.android.md.muc_coursework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark on 03/12/2014.
 */

public class StatsThread extends Thread {
//Class which draws and interperets graphical data on surface

    private int canvasWidth;
    private int canvasHeight;

    //Graph axis
    private float axisOffSet;
    private float barDistance;

    CommitDBMgr cDBMgr;
    Commit[] commits;

    Map<Integer, Float> graphData; //Holds commit data split into months
    Map<Integer, Float> graphDataPercentage; //Processed percentage per month data
    float totalPageCount;
    Calendar cal;


    private boolean first = true;
    private boolean run = false;

    private SurfaceHolder shStatsSurface;
    private Paint pStats;
    private StatsSurfaceView svStats;

    public StatsThread(Context context, SurfaceHolder surfaceHolder, StatsSurfaceView svStats) {
        this.shStatsSurface = surfaceHolder;
        this.svStats = svStats;
        pStats = new Paint();

        //Load commit data from database
        cDBMgr = new CommitDBMgr(context, "sqlLibrary2.s3db", null, 1);

        try {
            cDBMgr.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        commits = cDBMgr.loadCommits();
        calcMonthdata();
        calcPercentage();
    }

    public void calcMonthdata() {

        //Adds commit data to a new map, total pages per month

        graphData = new HashMap<Integer, Float>();

        cal = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {

            graphData.put(i, 0.0f);
        }


        for (int i = 0; i < commits.length; i++) {
            cal.setTime(commits[i].getDate());
            int month = cal.get(Calendar.MONTH);

            float pages = (float) commits[i].getPages();

            float pagedata = (float) graphData.get((Object) month);
            float totalpages = (float) pages + pagedata;

            totalPageCount += totalpages;
            graphData.put(month, totalpages);

        }
    }

    public void calcPercentage() {

        //Uses totals to calculate each months pages read as a percentage for the entire yeat
        graphDataPercentage = new HashMap<Integer, Float>();

        for (int i = 0; i < 12; i++) {

            float monthPageTotal = graphData.get((Object) i);
            float monthPercentage = (monthPageTotal / totalPageCount) * 100;

            graphDataPercentage.put(i, monthPercentage);

        }
    }

    public void doStart() {
        synchronized (shStatsSurface) {

            first = false;

        }
    }

    public void run() {
        while (run) {
            Canvas c = null;
            try {
                c = shStatsSurface.lockCanvas(null);
                synchronized (shStatsSurface) {
                    svDraw(c);
                }
            } finally {
                if (c != null) {
                    shStatsSurface.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean b) {
        run = b;
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (shStatsSurface) {
            canvasWidth = width;
            canvasHeight = height;

            axisOffSet = 50;
            barDistance = (canvasWidth - (axisOffSet * 2)) / 12;

            doStart();
        }
    }

    private void svDraw(Canvas canvas) {
        if (run) {
            canvas.save();
            canvas.restore();
            canvas.drawColor(Color.WHITE);
            pStats.setStyle(Paint.Style.FILL);
            drawAxes(canvas);
            pStats.setColor(Color.RED);
            drawBars(canvas);
            drawText(canvas);
        }
    }

    //Draw graph axis
    public void drawAxes(Canvas theCanvas) {
        pStats.setColor(Color.BLACK);

        theCanvas.drawLine(axisOffSet, canvasHeight - axisOffSet,
                axisOffSet, axisOffSet,
                pStats); // Vertical Axes

        theCanvas.drawLine(axisOffSet, canvasHeight - axisOffSet,
                canvasWidth - axisOffSet, canvasHeight - axisOffSet,
                pStats); // Horizontal Axes
    }

    public void drawBars(Canvas theCanvas) {

        //Draw monthly percentage values as bar graph
        float barTotalLength = canvasHeight - (axisOffSet * 2);
        float barHeight = 0.0f;

        float xPoints[] = new float[12];

        pStats.setStrokeWidth(5.0f);
        for (int i = 0; i < 12; i++) {
            xPoints[i] = (axisOffSet) + (barDistance * i) + (axisOffSet / 2);
            barHeight = (barTotalLength / 100) * graphDataPercentage.get((Object) i);

            theCanvas.drawLine(xPoints[i], canvasHeight - axisOffSet, //bottom of bar
                    xPoints[i], canvasHeight - axisOffSet - barHeight, //top of bar
                    pStats);
        }
    }

    void drawText(Canvas theCanvas) {
        //Draw graph text
        pStats.setTextSize(24.0f);
        theCanvas.drawText("Reading trend for 2014", 0, 22, canvasWidth / 2 - 40, canvasHeight / 3, pStats);

        pStats.setTextSize(20.0f);
        theCanvas.drawText("Months", 0, 6, canvasWidth / 2 - 10, canvasHeight - axisOffSet / 2, pStats);

    }
}
