package multihoptest;

import java.text.DecimalFormat;
import java.util.*;


//功能服务类
public class WsnFunction {


    //系统传感器死亡总时间
    public static double failureTime = 0;


    //判断是否对一个团分离成功,false表示还未分离成功
    private static boolean isolateStatus = false;
    //记录团分离之后的节点分离结果
    private static String isolateResult = "";

    public WsnFunction() {
        // TODO Auto-generated constructor stub
    }

//判断传感器在哪个正六边形中，并返回正六边形中心的编号
    public static int judgeHoneycomb(Sensor i,honeycomb j){
        int p=0;
        for (;p<=j.location.length-1;p++) {
            float x= Math.abs(i.location.x - j.location[p].x);
            float y= Math.abs(i.location.y - j.location[p].y);
            if (j.edge-y>x/Math.sqrt(3))
                break;
        }
        if(p==j.location.length) return 0;
        else return p;
    }
//判断传感器是否在MC直接覆盖区域内
    public static boolean judge_inMC(Sensor i){
        float distance = Point.getDistance(i.location,MCV.location);
        if(distance>=MCV.maxRadius) return false;
        else return true;
    }
    //初步确定锚点
    public static Point[][] initialAnchor(honeycomb q){
        Point[][] anchor = new Point[q.location.length][6];
        for(int i=0;i<q.location.length;i++) {
            anchor[i][0] = new Point(q.location[i].num,q.location[i].x, q.location[i].y + q.edge);
            anchor[i][1] = new Point(q.location[i].num,q.location[i].x + (float) (Math.sqrt(3)*q.edge/2), q.location[i].y + q.edge/2);
            anchor[i][2] = new Point(q.location[i].num,q.location[i].x + (float) (Math.sqrt(3)*q.edge/2), q.location[i].y - q.edge/2);
            anchor[i][3] = new Point(q.location[i].num,q.location[i].x, q.location[i].y - q.edge);
            anchor[i][4] = new Point(q.location[i].num,q.location[i].x - (float) (Math.sqrt(3)*q.edge/2), q.location[i].y - q.edge/2);
            anchor[i][5] = new Point(q.location[i].num,q.location[i].x - (float) (Math.sqrt(3)*q.edge/2), q.location[i].y + q.edge/2);
        }
        return anchor;
    }
    //MC在锚点p时覆盖到的传感器节点个数
    public static int cloverNUM(Point p,Sensor[] allSensor){
        int num=0;
        for (Sensor node:allSensor) {
            double distance = Point.getDistance(p, node.location);
            if(node.inHoneycomb==p.num && distance <= MCV.maxRadius) num++;
        }
        return num;
    }

    public static void cloverNODE(Point p,Sensor[] allSensor) {
        for (int i = 0; i < allSensor.length; i++) {
            double distance = Point.getDistance(p, allSensor[i].location);
            if (allSensor[i].inHoneycomb == p.num && distance <= MCV.maxRadius) allSensor[i].isClover = true;
        }
    }
//    public static Point[] Anchor(Point[][] p){
//
//    }


    //在指定区间大小networkSize获得n个随机数
    private static float[] getRandom(double networkSize, int n, long seed) {
        float[] rm = new float[n];
        double rd = 0.0;
        Random random = new Random(seed);
        //新建格式化器，设置格式,保留两位小数
        DecimalFormat Dformat = new DecimalFormat("0.00");
        for(int i=0;i<n;i++) {//生成n个随机数
            rd = random.nextDouble()*networkSize;//[0,100)
            //根据格式化器格式化数据
            String rdStr = Dformat.format(rd);
            //将String转为double
            rm[i] = Float.parseFloat(rdStr);
        }
        return rm;
    }


    //初始化n个节点
    public static Sensor[] initSensors(float networkSize, int n, float minECR, float maxECR) {
        //X轴的随机种子数
        long seedX = 92837;
        //Y轴的随机种子数
        long seedY = 626626;
        //横坐标50个随机数
        float[] dx = WsnFunction.getRandom(networkSize,n,seedX);
        //System.out.println(Arrays.toString(dx));
        //纵坐标50个随机数
        float[] dy = WsnFunction.getRandom(networkSize,n,seedY);
        //System.out.println(Arrays.toString(dy));
        Sensor[] sensors = new Sensor[n];
        //对n个传感器节点初始化
        for(int i=0;i < sensors.length;i++) {
            //初始化n个节点的编号、位置,节点的编号从0开始,0号节点的索引下标为0
            sensors[i] = new Sensor(i,dx[i],dy[i],networkSize,minECR,maxECR);
        }
        return sensors;
    }


//    if (cluster[i].length!=0)
//            for (int j=0;j < cluster[i].length; j++)
//            if(cluster[i][j].isClover)  cluster[i][j].erRateEFF=cluster[i][j].getERRate(Point.getDistance(cluster[i][j].location,Anchor[i]));
//
//        for(int j=0;j < cluster[i].length;j++){
//        if (!cluster[i][j].isClover)  {
//            int     nextHOP=-1 ;
//            double maxERrate=0;
//            boolean change =false;
//            for (int f=0;f < cluster[i].length;f++){
//                if (cluster[i][f].isClover && cluster[i][j].getERRate(Sensor.getDistance(cluster[i][j],cluster[i][f]))*cluster[i][f].erRateEFF > maxERrate )     //如果选择的下一跳节点为MC直接覆盖节点，且以此为其中继节点能量传输效率高，则记录此中继节点
//                    maxERrate = cluster[i][j].getERRate(Sensor.getDistance(cluster[i][j],cluster[i][f]))*cluster[i][f].erRateEFF;
//                nextHOP = f;
//                change  = true;
//            }
//            //若多跳效率大于阈值（0.1），则确定该多跳路径
//            if (maxERrate > 0.1) {
//                cluster[i][j].erRateEFF = cluster[i][j].getERRate(Sensor.getDistance(cluster[i][j], cluster[i][nextHOP])) * cluster[i][nextHOP].erRateEFF;   //多跳效率为多跳路径中每一段效率累乘。
//                cluster[i][j].multihop = nextHOP;
//            }


    //初步选取多跳路径
    public static Sensor[] multihop_PATH(Sensor[] cluster){

        for(int i=0;i < cluster.length;i++){
            if (!cluster[i].isClover)  {
                int     nextHOP=-1 ;
                double maxERrate=0;
                boolean change =false;
                for (int f=0;f < cluster.length;f++){
                    if (cluster[f].isClover && cluster[i].getERRate(Sensor.getDistance(cluster[i],cluster[f]))*cluster[f].erRateEFF > maxERrate ){     //如果选择的下一跳节点为MC直接覆盖节点，且以此为其中继节点能量传输效率高，则记录此中继节点
                        maxERrate = cluster[i].getERRate(Sensor.getDistance(cluster[i],cluster[f]))*cluster[f].erRateEFF ;
                        nextHOP = f;
                        change  = true;
                    }
                }
                if (nextHOP >= 0) {
                    cluster[i].erRateEFF = cluster[i].getERRate(Sensor.getDistance(cluster[i], cluster[nextHOP])) * cluster[nextHOP].erRateEFF;     //多跳效率为多跳路径中每一段效率累乘。
                    cluster[i].multihop = cluster[nextHOP].number;                                                                                  //确定该节点i的下一跳节点。
                }
            }
        }
        return cluster ;
    }
    //查询簇中是否还存在没有分配多跳路径的节点，存在没有分配多跳路径的节点就返回true ，否则false
    public static boolean IF_noPATH (Sensor[] cluster){
        boolean judge = false ;
        if (cluster.length!=0) {
            for (int i = 0; i < cluster.length; i++) {
                if (!cluster[i].isClover) judge = true;
            }
        }
        return judge;
    }
    //查询簇中是否没有任何节点是被MC直接覆盖的，对于没有
//    public static boolean IF_noCLOVER(Sensor[] cluster){
//
//    }




    //计算每个分组的充电时间之和
    public static double getChargingTime(Sensor[] cluster) {
        float chargingTime = 0;
        //遍历每个分组,对分组的充电时间累加求和
        if (cluster.length != 0)
        for (Sensor eg : cluster) {
            chargingTime += eg.ctime;
            //System.out.println("各组充电时间:"+eg.ctime);
        }
        return chargingTime;
    }

/*
    //求行驶路径的长度
    public static double getPathLength(Group[] group) {

        if(group.length==0) return 0;
        //路径长度之和
        double pathLength = 0;
        //遍历组至倒数第二个[0,group.length-2]
        for (int i=0;i<=group.length-2;i++) {
            double distance = Point.getDistance(group[i].stop, group[i+1].stop);
            pathLength += distance;
        }

        return pathLength;
    }


    //统计本轮充电MCV消耗的充电能量
    public static double getChargingEnergy(Group[] group) {
        double chargingEnergy = 0;
        for (Group eg : group) {
            chargingEnergy += eg.cEnergy;
        }
        return chargingEnergy;
    }*/


    //更新其他本轮未充电节点的剩余能量
    public static void updateOtherRE(double time,int rn,Sensor...allSensor) {
        //如果是所有节点都充电,则不需要更新节点剩余能量
        if(rn == allSensor.length)  return;

        //未充电节点个数=全部节点个数-充电节点个数
        int number = allSensor.length - rn;
        //创建其余未充电节点集合
        Sensor[] otherNode = new Sensor[number];
        int k = 0;//数组otherNode的索引
        for (Sensor sensor : allSensor) {
            //找出未请求充电的节点
            if(!sensor.isCharging) {
                otherNode[k++] = sensor;
            }
        }
        //更新未请求节点的剩余能量
        for(int i=0;i<otherNode.length;i++) {
            otherNode[i].remainingE -= otherNode[i].ecRate * time;
        }
    }

    //统计死亡节点数
    public static int getFailureNumber(Sensor...sensors) {
        int count = 0;
        for (Sensor node : sensors) {
            if(node.isFailure) {
                count++;
            }
        }
        return count;
    }


    //创建一对一充电的分组
//    public static Group[] onetoOneGroup(double networkSize,Sensor...chargingSensor) {
//        Group[] groups = new Group[chargingSensor.length+1];
//        groups[0] = new Group(0,new Point(networkSize/2,networkSize/2));
//        for(int i=1;i < groups.length;i++) {     		//i:[1,chargingSensor.length]
//            //创建第i个分组
//            groups[i] = new Group(i,chargingSensor[i-1].location,chargingSensor[i-1]);
//        }
//        return groups;
//    }

/*
    //测试方法
    public static void main(String[] args) {
        HashMap<String, Point[]> m = new HashMap<String, Point[]>();
        m.put("dog", new Point[] {new Point(2,1)});
        System.out.println(m);
        Point[] p = (Point[])m.get("dog");
        for (Point point : p) {
            System.out.println(point.x+","+point.y);
        }

        System.out.println(getRemainingNode("24,32,50,",new String[] {"24","50","84","62","32"}));

    }
*/


}

