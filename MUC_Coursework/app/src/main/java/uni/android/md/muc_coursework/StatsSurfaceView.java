package uni.android.md.muc_coursework;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Mark on 03/12/2014.
 */
public class StatsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    private SurfaceHolder shStatsSurface;
    StatsThread drawingThread = null;


    public StatsSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public StatsSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatsSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        //Starts drawing thread for surface when created as an activity screen component
        shStatsSurface = getHolder();
        shStatsSurface.addCallback(this);
        drawingThread = new StatsThread(context, getHolder(), this);
        setFocusable(true);

    }

    public StatsThread getThread() {
        return drawingThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawingThread.setRunning(true);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawingThread.setSurfaceSize(width, height);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawingThread.setRunning(false);
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
