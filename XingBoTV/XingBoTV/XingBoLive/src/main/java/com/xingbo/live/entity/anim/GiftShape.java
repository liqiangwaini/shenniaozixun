package com.xingbo.live.entity.anim;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Pair;


import com.xingbobase.util.XingBoUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 批量礼物动画对象
 * Created by WuJinZhou on 2015/12/19.
 */
public class GiftShape {
    public final static String KEY_LOVE="love";
    public final static String KEY_SMILE="smile";
    public final static String KEY_V="v";
    public final static String KEY_V520="v520";
    public final static String KEY_V1314="v1314";
    public final static String KEY_XIN="xin";

    private final static String TAG="GiftShape";
    private String key;//名称
    private List<GiftShapePoint>points;//各控件运行终点坐标集
    private GiftShapePoint minPoint;//原始坐系统中最小点
    private GiftShapePoint maxPoint;//原始坐系统中最大点
    public int iconSize;//拼成图形的ImageView大小

    public GiftShape(String key,List<GiftShapePoint> points) {
        this.key = key;
        this.points = points;
        setMaxAndMinPoint(points);
    }

    /**
     *
     */
    public void initShape(Context context,Pair<Integer, Integer> screen){
        float sw=screen.first.floatValue();
        float sh=screen.second.floatValue();
        float shapeHeight=getMaxPoint().getY()-getMinPoint().getY();
        float shapeWidth=getMaxPoint().getX()-getMinPoint().getX();
        //固定宽为屏幕的60%，高为屏幕的30%
        float mWidth=sw*0.6f;
        float mHeight=sw*0.3f;
        if(shapeWidth>mWidth){
            float scaleW=shapeWidth/mWidth;
            shapeWidth=mWidth;
            XingBoUtil.log(TAG, "宽度缩小比例" + scaleW);
            shapeHeight=shapeHeight/scaleW;
            scalePoint(scaleW);
        }
        float centerY=sh/3;
        if(shapeHeight>mHeight){
            float scaleH=shapeHeight/mHeight;
            XingBoUtil.log(TAG, "高度缩小比例" + scaleH);
            shapeWidth=shapeWidth/scaleH;
            scalePoint(scaleH);
        }
        float shapeStatX=(sw-shapeWidth)/2-getMinPoint().getX()- XingBoUtil.dip2px(context, 8);
        conversionX(shapeStatX);

        float shapeStatY=centerY-shapeHeight/2;
        conversionY(shapeStatY);
        System.out.println("缩放后图形宽高="+shapeWidth+"x"+shapeHeight);
        iconSize= XingBoUtil.dip2px(context, 20);
    }

    public void setMaxAndMinPoint(List<GiftShapePoint>points){
        /**
         *  如果要按照降序排序
         则o1 小于o2，返回1（正数），相等返回0，o1大于o2返回-1（负数）
         *
         * */
        Collections.sort(points, new Comparator<GiftShapePoint>() {
            @Override
            public int compare(GiftShapePoint lhs, GiftShapePoint rhs) {
                if (lhs.getX() < rhs.getX()) {
                    return 1;
                } else if (lhs.getX() > rhs.getX()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        float minX=points.get(points.size() - 1).getX();
        float maxX=points.get(0).getX();
        XingBoUtil.log("X排序", "最小X=" + minX + "--最大X=" + maxX);
        Collections.sort(points, new Comparator<GiftShapePoint>() {
            @Override
            public int compare(GiftShapePoint lhs, GiftShapePoint rhs) {
                if (lhs.getY() < rhs.getY()) {
                    return 1;
                } else if (lhs.getY() > rhs.getY()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        float minY=points.get(points.size() - 1).getY();
        float maxY=points.get(0).getY();
        XingBoUtil.log("Y排序", "最小Y=" + minY + "--最大Y=" + maxY);
        minPoint=new GiftShapePoint(minX,minY);
        maxPoint=new GiftShapePoint(maxX,maxY);
    }

    /**
     * 坐标缩放
     */
    public void scalePoint(float scale){
        for(GiftShapePoint point:points){
            point.scale(scale);
        }
        maxPoint.scale(scale);
        minPoint.scale(scale);
    }

    /**
     * 换算Y坐标
     */
    public void conversionY(float startY){
        for(GiftShapePoint point:points){
            point.conversionY(startY);
        }
    }
    /**
     * 换算X坐标
     */
    public void conversionX(float startX){
        for(GiftShapePoint point:points){
            point.conversionX(startX);
        }
    }

    /**
     * 解析xml坐标配置文件，得到动画的坐标集
     */
    public static List<GiftShapePoint> parseXMLPointsFile(Context context,String fileName){
        List<GiftShapePoint> pointList=new ArrayList<GiftShapePoint>();
        AssetManager manager=context.getAssets();
        try {
            InputStream in=manager.open("points/" + fileName+".xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);
            NodeList points = doc.getElementsByTagName("point");
            for (int i=0;i<points.getLength();i++) {
                Element point =  (Element) points.item(i);
                float x=Float.parseFloat(point.getAttribute("x"));
                float y=Float.parseFloat(point.getAttribute("y"));
                pointList.add(new GiftShapePoint(x,y));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return pointList;
    }

    public List<GiftShapePoint> getPoints() {
        return points;
    }

    public GiftShapePoint getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(GiftShapePoint minPoint) {
        this.minPoint = minPoint;
    }

    public GiftShapePoint getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(GiftShapePoint maxPoint) {
        this.maxPoint = maxPoint;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
