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
        honeycomb test =new honeycomb(4.00,200.00);
        Point[] k =test.creat_honeycomb(4.00,200.00);
        for(Sensor node:allSensor){
            System.out.println("编号:"+node.number+" 坐标:("+node.location.x+","+node.location.y+")属于编号为"+WsnFunction.judge(node,test)+"的正六边形，其中心坐标为"+"("+k[WsnFunction.judge(node,test)].x+","+k[WsnFunction.judge(node,test)].y+")");
        }
    }

}
