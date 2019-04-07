package multihoptest;

public class run {

    public static void main(String[] args){
        //网络规模
        double networkSize = 200;
        //传感器节点个数
        int nodenum = 1500;
        //系统当前时间初始为0s
        int systemTime = 0;
        //能量消耗率最小值
        double minECR = 0.01;
        //能量消耗率最大值
        double maxECR = 0.06;


        Sensor[] allSensor = WsnFunction.initSensors(networkSize, nodenum, minECR, maxECR);
//        System.out.println("随机创建的节点信息如下");
//        for(Sensor node:allSensor) {
//            System.out.println("编号:"+node.number+" 坐标:("+node.location.x+","+node.location.y+") 剩余能量阈值:"+node.remainingE/node.maxCapacity +" 剩余寿命:"+node.remainingE/node.ecRate);
//        }
        honeycomb test =new honeycomb(8.00,210.00);    //创建蜂窝
        Point[] k =test.creat_honeycomb(8.00,210.00);//获取每个蜂窝的中心坐标

        for(int i=0;i<allSensor.length;i++){                //将节点分簇，每个节点的inHoneycomb值表示其所在的簇
            allSensor[i].inHoneycomb = WsnFunction.judgeHoneycomb(allSensor[i],test);
            //System.out.println("编号:"+allSensor[i].number+" 坐标:("+allSensor[i].location.x+","+allSensor[i].location.y+")属于编号为"+allSensor[i].inHoneycomb+"的正六边形，其中心坐标为"+"("+k[WsnFunction.judgeHoneycomb(allSensor[i],test)].x+","+k[WsnFunction.judgeHoneycomb(allSensor[i],test)].y+")");
        }

        //选取锚点
        Point[] Anchor = new Point[k.length];   //存放每个簇的锚点坐标，Anchor[0]表示第0个正六边形区域内的锚点位置
        Point[][] initialAnchor = WsnFunction.initialAnchor(test);     //初选锚点

//        for(int i=0;i<Anchor.length;i++)
//            for(int j=0;j<6;j++)
//            System.out.println(initialAnchor[i][j].x+","+initialAnchor[i][j].y);
//            //测试，输出某个锚点覆盖的节点数量
//        int number = WsnFunction.cloverNUM(initialAnchor[11][3],allSensor);
//        System.out.println(number);

        for (int i=0;i<Anchor.length;i++){
            int num=0,temp_1=0;
            for (int j=0;j<6;j++) {
                if(num < WsnFunction.cloverNUM(initialAnchor[i][j],allSensor)){
                    num = WsnFunction.cloverNUM(initialAnchor[i][j],allSensor);
                    temp_1=j;
                }
            }
//            System.out.println(i+","+temp_1+","+num);
            Anchor[i]=initialAnchor[i][temp_1];    //确定第i个正六边形区域的锚点坐标
//            //测试输出
//            System.out.println("第"+Anchor[i].num+"个簇的锚点坐标为（"+Anchor[i].x+"，"+Anchor[i].y+"),且以此为锚点，MC能覆盖到的节点个数为："+num);
        }

    }
}
