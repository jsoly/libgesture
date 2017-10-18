package d.cityaurora.com.libgesture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.x;

/**
 * Created by jsj on 2017/10/16.
 */

public class PatternManager {

    private int width;
    private int height;
    private int top;
    private List<Pattern> mPatterns;
    private List<Pattern> mlockPatterns;
    private Path path = new Path();
    private boolean isError = false;


    private String mText="";
    private int textColor = Color.parseColor("#A9A9A9");
    private int mblank;
    private int mcircle;
    private int mcloumns;


    public PatternManager(int width, int height , int top) {
        this(width,height,top,3);
    }

    public List<Pattern> getPatterns() {
        return mPatterns;
    }

    public PatternManager(int width, int height , int top, int cloumns) {
        this.width = width;
        this.height = height;
        this.top = top;
        int squareWidth = Math.min(width,height);
        int circle = squareWidth*3/17;
        int blank = circle*2/3;
        setMblank(blank);
        setMcircle(circle);
        setMcloumns(cloumns);
        mPatterns = new ArrayList<>();
        mlockPatterns = new ArrayList<>();
        path .reset();
        float x;
        float y;
        int r = circle/2;
         for (int i=0;i<cloumns;i++){
             for(int j=0;j<cloumns;j++){
                //中心点x = 空白偏移量 + 圆形个数 + 半径
                 x = blank*(j+1)  + circle*j + r;
                 y = top + blank*(i+1) + circle*i + r;
                 Pattern child = new Pattern(x,y,r);
                 child.setId(i*cloumns+j);
                 mPatterns.add(child);
             }
         }
    }

    public void drawPattern(Canvas canvas, Paint paint,float x,float y){
        drawLines(canvas,paint,x,y);
        drawChildren(canvas,paint);
        drawTriDirection(canvas,paint);
        drawText(canvas,paint);
        drawCircles(canvas,paint);
    }



    public void drawLines(Canvas canvas, Paint paint,float x,float y) {
        paint.setColor(Color.parseColor("#1E90FF"));
        paint.setStyle(Paint.Style.STROKE);
        if(mlockPatterns.size()!=0){
            path.reset();
            path.moveTo(mlockPatterns.get(0).getX(),mlockPatterns.get(0).getY());
            float lastX = 0;
            float lastY = 0;
            for(Pattern child : mlockPatterns){
                lastX = child.getX();
                lastY = child.getY();
                path.lineTo(lastX,lastY);
            }
            canvas.drawPath(path,paint);
            if(x!=0 && y!=0)
                canvas.drawLine(lastX,lastY,x,y,paint);
        }
    }

    public void drawChildren(Canvas canvas, Paint paint){
        for(Pattern child : mPatterns){
            child.onDraw(canvas,paint);
        }
    }


    private void drawTriDirection(Canvas canvas, Paint paint) {
        for(int i=0;i<mlockPatterns.size();i++){
            if(i!=mlockPatterns.size()-1){
                mlockPatterns.get(i).drawDirection(canvas,paint,mlockPatterns.get(i),mlockPatterns.get(i+1));
            }
        }
    }


    private void drawText(Canvas canvas, Paint paint) {
        paint.setColor(textColor);
        float length = paint.measureText(mText);
        canvas.drawText(mText,width/2 - length/2,top+getMblank()/2,paint);
    }


    private void drawCircles(Canvas canvas, Paint paint) {
        float x;
        float y;
        float blank = 5;
        float circle = getMcircle()/8;
        float top = this.top -getMcircle() + getMblank()*2/3;
        float offsetX = width/2 - blank*2 - circle - circle/2;
        int r = (int) (circle/2);
        for (int i=0;i<getMcloumns();i++){
            for(int j=0;j<getMcloumns();j++){
                //中心点x = 空白偏移量 + 圆形个数 + 半径
                x = offsetX +  blank*(j+1)  + circle*j + r;
                y = top + blank*(i+1) + circle*i + r;
                int index = i*getMcloumns() + j;
                if(mPatterns.get(index).isSelected()){
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.parseColor("#1E90FF"));
                }else {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.parseColor("#A9A9A9"));
                }
                canvas.drawCircle(x,y,circle/2,paint);
            }
        }
    }

    public void reset(){
        for(Pattern child : mPatterns){
            child.setSelected(false);
            child.setError(false);
        }
        setError(false);
        mlockPatterns.clear();
        path.reset();
    }

    public boolean isContain(float x, float y){
        for(Pattern child : mPatterns){
            if(child.isContain((int)x,(int)y)){
                child.setSelected(true);
                if(!mlockPatterns.contains(child)){
                    mlockPatterns.add(child);
                }
                return true;
            }
        }
        return false;
    }

    public String getPassword(){
        String psw = "";
        for(Pattern child : mlockPatterns){
            psw+= child.getId();
        }
        return psw;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
        if(error){
            textColor = Color.parseColor("#f43f47");
        }else {
            textColor = Color.parseColor("#A9A9A9");
        }
    }

    public void setErrorLine(boolean b){
        for(Pattern child : mlockPatterns){
            child.setError(true);
        }
    }


    public int getMblank() {
        return mblank;
    }

    public void setMblank(int mblank) {
        this.mblank = mblank;
    }

    protected int getMcircle() {
        return mcircle;
    }

    protected void setMcircle(int mcircle) {
        this.mcircle = mcircle;
    }

    protected int getMcloumns() {
        return mcloumns;
    }

    protected void setMcloumns(int mcloumns) {
        this.mcloumns = mcloumns;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

}
