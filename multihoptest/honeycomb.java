package multihoptest;

import java.io.*;
public class honeycomb {
    private  Point[] location;                   //每个蜂窝正六边形的中心坐标位置
    private  double edge;                      //正六边形的边长
    private  double networkSize;             //方形网络边长

    //生成蜂窝
    private Point[] creat_honeycomb(double _edge , double _networkSize ){
        edge=_edge;
        networkSize=_networkSize;
        location=new Point[getHONEYCOMB_NUM()];
        double x;
        double y;
        int p=0,xi=0;
        for(;xi<=networkSize/(edge*Math.sqrt(3)/2);xi++){
            int yi=0;
            for(;yi<=networkSize/(edge*3/2);yi++){
                if(xi%2==0&&yi%2==0) {
                    y=yi*edge*3/2;
                    x=xi*edge*Math.sqrt(3)/2;
                    location[p]=new Point(p,x,y);
                    p++;
                }
                else if(xi%2!=0&&yi%2!=0){
                    y=yi*edge*3/2;
                    x=xi*edge*Math.sqrt(3)/2;
                    location[p]=new Point(p,x,y);
                    p++;
                }
            }
        }
        return location ;
    }
    //计算出当前网络规格可容纳的蜂窝个数
    private int getHONEYCOMB_NUM(){

//        double x;
//        double y;
        int p=0,xi=0;
        for(;xi<=networkSize/(edge*Math.sqrt(3)/2);xi++){
            int yi=0;
            for(;yi<=networkSize/(edge*3/2);yi++){
                if(xi%2==0&&yi%2==0) {
//                    y=yi*edge*3/2;
//                    x=xi*edge*Math.sqrt(3)/2;
//                    location[p]=new Point(x,y);
                    p++;
                }
                else if(xi%2!=0&&yi%2!=0){
//                    y=yi*edge*3/2;
//                    x=xi*edge*Math.sqrt(3)/2;
//                    location[p]=new Point(x,y);
                    p++;
                }
            }
        }
        return p;
    }
//    public int length(){
//
//    }
//    测试方法
    public static void main(String[] args){
        honeycomb test =new honeycomb();
        Point[] k =test.creat_honeycomb(4.00,200.00);
        System.out.println("----------------------------------------------");
        for(int i=0;i<k.length;i++) {
            System.out.println("第"+k[i].num+"个"+"坐标("+k[i].x+","+k[i].y+")");
        }
    }
}
