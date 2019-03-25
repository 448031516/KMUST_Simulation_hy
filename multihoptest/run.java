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
        honeycomb test =new honeycomb(4.00,210.00);    //创建蜂窝
        Point[] k =test.creat_honeycomb(4.00,210.00);//获取每个蜂窝的中心坐标
        for(int i=0;i<allSensor.length;i++){
            allSensor[i].inHoneycomb=WsnFunction.judgeHoneycomb(allSensor[i],test);//将每个传感器节点分簇
            System.out.println("编号:"+allSensor[i].number+" 坐标:("+allSensor[i].location.x+","+allSensor[i].location.y+")属于编号为"+allSensor[i].inHoneycomb+"的正六边形，其中心坐标为"+"("+k[WsnFunction.judgeHoneycomb(allSensor[i],test)].x+","+k[WsnFunction.judgeHoneycomb(allSensor[i],test)].y+")");
        }

    }
}
