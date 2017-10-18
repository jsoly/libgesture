package d.cityaurora.com.libgesture;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static android.R.attr.path;
import static android.R.attr.radius;
import static android.R.attr.startX;

/**
 * Created by jsj on 2017/10/16.
 */

public class Pattern {

    private float x;
    private float y;
    private Path mPath;
    private int r;
    private int color = Color.parseColor("#1E90FF");
    private boolean isSelected = false;
    private boolean isError = false;
    private int id;

    public Pattern(float x, float y , int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        mPath = new Path();
        mPath.reset();
        mPath.addCircle(x,y,r, Path.Direction.CCW);
    }

    public void onDraw(Canvas canvas, Paint paint){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x,y,r,paint);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath,paint);
        if(isSelected){
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x,y,r/3,paint);
        }
    }


    public void drawDirection(Canvas canvas, Paint paint, Pattern pattern1, Pattern pattern2){
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        float x1 = pattern1.getX();
        float y1 = pattern1.getY();
        float x2 = pattern2.getX();
        float y2 = pattern2.getY();
        Path path = new Path();
        path.moveTo(x1,y1);
        path.lineTo(x2,y2);
        PathMeasure measure = new PathMeasure(path,false);
        float[] pos = new float[2];
        float[] tan = new float[2];
        measure.getPosTan(r*7/10,pos,tan);
        float degrees = (float)(Math.atan2(tan[1], tan[0])*180.0/Math.PI);
        RectF rect = new RectF(pos[0]-r/5,pos[1]-r/5,pos[0]+r/5,pos[1]+r/5);
        canvas.drawArc(rect, -210f+degrees, 60, true, paint);
    }


    public boolean isContain(int x,int y) {
        RectF bounds = new RectF();
        mPath.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(mPath, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region.contains(x, y);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
        if(error) color = Color.parseColor("#f43f47");
        else color = Color.parseColor("#1E90FF");
    }
}
