package multihoptest;

import java.io.*;
public class honeycomb {
    Point[] location;                   //每个蜂窝正六边形的中心坐标位置
    double edge=4;                      //正六边形的边长
    double networkSize=200;             //方形网络边长

    //生成蜂窝
    public honeycomb(){
        double x;
        double y;
        int p=0,xi=0;
        location=new Point[getHONEYCOMB_NUM()];
        for(;xi<=networkSize/(edge*Math.sqrt(3)/2);xi++){
            int yi=0;
            for(;yi<=networkSize/(edge*3/2);yi++){
                if(xi%2==0&&yi%2==0) {
                    y=yi*edge*3/2;
                    x=xi*edge*Math.sqrt(3)/2;
                    location[p]=new Point(x,y);
                    p++;
                }
                else if(xi%2!=0&&yi%2!=0){
                    y=yi*edge*3/2;
                    x=xi*edge*Math.sqrt(3)/2;
                    location[p]=new Point(x,y);
                    p++;
                }

            }

        }
    }


    //计算出当前网络规格可容纳的蜂窝个数
    public int getHONEYCOMB_NUM(){

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
    public static void main(String[] args){

        honeycomb test =new honeycomb();
        for(Point s:test.location) {
            System.out.println("第"+"坐标("+s.x+","+s.y+")");
        }
    }
}
