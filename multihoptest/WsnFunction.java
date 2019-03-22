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
    public static int judge(Sensor i,honeycomb j){
        int p=0;
        for (;p<=j.location.length-1;p++) {
            double  x= Math.abs(i.location.x - j.location[p].x);
            double  y= Math.abs(i.location.y - j.location[p].y);
            if (j.edge-y>x/Math.sqrt(3))
                break;
        }
        if(p==j.location.length) return 0;
        else return p;
    }
    //在指定区间大小networkSize获得n个随机数
    private static double[] getRandom(double networkSize,int n,long seed) {
        double[] rm = new double[n];
        double rd = 0.0;
        Random random = new Random(seed);
        //新建格式化器，设置格式,保留两位小数
        DecimalFormat Dformat = new DecimalFormat("0.00");
        for(int i=0;i<n;i++) {//生成n个随机数
            rd = random.nextDouble()*networkSize;//[0,100)
            //根据格式化器格式化数据
            String rdStr = Dformat.format(rd);
            //将String转为double
            rm[i] = Double.parseDouble(rdStr);
        }
        return rm;
    }


    //初始化n个节点
    public static Sensor[] initSensors(double networkSize,int n,double minECR,double maxECR) {
        //X轴的随机种子数
        long seedX = 92837;
        //Y轴的随机种子数
        long seedY = 626626;
        //横坐标50个随机数
        double[] dx = WsnFunction.getRandom(networkSize,n,seedX);
        //System.out.println(Arrays.toString(dx));
        //纵坐标50个随机数
        double[] dy = WsnFunction.getRandom(networkSize,n,seedY);
        //System.out.println(Arrays.toString(dy));
        Sensor[] sensors = new Sensor[n];
        //对n个传感器节点初始化
        for(int i=0;i < sensors.length;i++) {
            //初始化n个节点的编号、位置,节点的编号从1开始,1号节点的索引下标为0
            sensors[i] = new Sensor(i+1,dx[i],dy[i],networkSize,minECR,maxECR);
        }
        return sensors;
    }
    //判断传感器是否在某个

   /* //创建与需要被充电的节点对应的圆
    public static Circle[] creatCircle(Sensor...chargeSensor) {
        //圆的个数==充电节点个数
        int n = chargeSensor.length;
        Circle[] clrcle = new Circle[n];
        for(int i=0;i<n;i++) {
            //圆的编号从0开始,每个圆的编号和索引下标相同
            clrcle[i] = new Circle(i,chargeSensor[i].number,chargeSensor[i].location,MCV.maxRadius);
        }
        return clrcle;
    }

    //把某个团cq = 24,50,84,62,32转换成对应圆的集合
    public static Circle[] cliquetoCircle(String cq,Circle...circles) {

        if(cq.length()==0 || cq == null) return null;

        //System.out.println("cq=========="+cq);

        //获得每个节点的编号
        String[] nodenum = cq.split(",");
        //圆的个数==团内节点个数
        int n = nodenum.length;
        Circle[] groupCricle = new Circle[n];
        //遍历每个节点,得到相应编号的圆
        for(int i=0;i<n;i++) {
            groupCricle[i] = circles[Integer.parseInt(nodenum[i])];
            //System.out.println("圆的编号:"+groupCricle[i].number);
        }

        return groupCricle;
    }


    //将String数组里的元素按整数的大小排序
    public static void stringSort(String[] strArray) {
        int[] intArray = new int[strArray.length];
        for(int i=0;i<intArray.length;i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        Arrays.sort(intArray);//按整型数字大小排序
        for(int i=0;i<strArray.length;i++) {
            strArray[i] = intArray[i]+"";
        }
    }


    *//**
     *
     * @param isolateNode : 3,15,
     * @param remainingNode : 27,39,45,46,
     * @param circles : 全部圆的集合
     * @return  判断分离的两部分的圆是否有交集,有交集返回true,否则返回false
     *//*
    public static boolean isTwoIntersecting(String isolateNode,String remainingNode,Circle...circles) {
        Circle[] isolateCicrle = cliquetoCircle(isolateNode,circles);//分离出去的圆
        Circle[] remainingCicrle = cliquetoCircle(remainingNode,circles);//剩余的圆
        if(isolateCicrle.length == 1) return Circle.isMultiIntersection(remainingCicrle);
        if(remainingCicrle.length == 1) return Circle.isMultiIntersection(isolateCicrle);
        return Circle.isMultiIntersection(remainingCicrle)&&Circle.isMultiIntersection(isolateCicrle);
    }


    //把节点24,32,50,从数组"24","50","84","62","32"中删除,删除后返回用,隔开的字符串84,62,
    public static String getRemainingNode(String isolateNode,String[] nodes) {
        String remainingNode = "";
        for (String eachnode : nodes) {
            //把String数组转换成  字符串   并用.和,隔开  如   .24,.50,.84,.62,.32,
            remainingNode += "."+ eachnode +",";
        }

        String[] rmNode = isolateNode.split(",");

        for(int i=0;i<rmNode.length;i++) {
            String rmStr = "."+rmNode[i]+"," ;//把要删除的节点转成如.24,的字符串格式
            remainingNode = remainingNode.replace(rmStr,"");
        }

        return remainingNode.replace(".", "");//删除.
    }


    //从字符串数组中第begin个字符开始挑选number个节点加入list中
    public static void combine(String[] nodes, int begin, int number, List<String> list, Circle...circles) {

        if(number==0) {
            //System.out.println(list.toString());
            String listtoString = list.toString();
            //去掉首尾的[]
            String isolateNode = listtoString.substring(1,listtoString.length()-1);
            //隔离的节点     需去掉空格 尾部添加","
            isolateNode = isolateNode.replace(" ", "")+",";
            //剩余的节点
            String remainingNode = getRemainingNode(isolateNode,nodes);

//          System.out.println("分离情况如下:");
//        	System.out.println("分离节点:"+isolateNode);//3,15,
//        	System.out.println("剩下节点:"+remainingNode);//27,39,45,46,
//        	System.out.println();

            //如果还未分离成功,并且分离后的两部分圆都有交集
            if(!WsnFunction.isolateStatus && isTwoIntersecting(isolateNode,remainingNode,circles)) {
                isolateNode = isolateNode.substring(0, isolateNode.length()-1);
                remainingNode = remainingNode.substring(0, remainingNode.length()-1);
                WsnFunction.isolateResult = isolateNode + ";" + remainingNode;//分离结果用;隔开3,15;27,39,45,46
                WsnFunction.isolateStatus = true;//分离成功
            }
            return;
        }

        if(begin == nodes.length || WsnFunction.isolateStatus) return;

        //递归  针对第一个字符，我们有两种选择:
        //一是把这个字符放到组合中去,接下来我们需要在剩下的n-1个字符中选取number-1个字符;
        list.add(nodes[begin]);
        combine(nodes,begin+1,number-1,list,circles);
        //二是不把这个字符放到组合中去,接下来我们需要在剩下的n-1个字符中选择number个字符。
        list.remove((String)nodes[begin]);
        combine(nodes,begin+1,number,list,circles);
    }


    public static void combiantion(String[] nodes,Circle...circles){

        if(nodes==null||nodes.length==0){
            return;
        }

        List<String> list=new ArrayList<String>();

        for(int i=1;i<=nodes.length-1;i++){
            //每次从第0个节点开始隔离i个节点 i:[1,n-1]
            combine(nodes,0,i,list,circles);
        }

    }


    //对第k个团进行分离    cq = 24,50,84,62,32
    private static void isolateClique(String onecq,Circle...circles) {
        //每次进行隔离前要将初始化全局变量
        WsnFunction.isolateStatus = false;
        WsnFunction.isolateResult = "";
        String[] nodes = onecq.split(",");
        stringSort(nodes);//按编号从小到大排个序
        //处理节点的每一种组合
        combiantion(nodes,circles);
    }


    //检查每个团的形成的圆是否有交集区域,若没有就把团内的节点分离
    public static String[] checkCliques(String[] allClique,Circle...circles) {

        //遍历每个团  24,50,84,62,32
        for(int i=0;i<allClique.length;i++) {
            //System.out.println("团:"+allClique[i]);
            Circle[] groupCircle = cliquetoCircle(allClique[i],circles);

            //团对应的圆没有交集,需要对团进行分离
            if(groupCircle.length >= 3 && !Circle.isMultiIntersection(groupCircle)) {

                //System.out.println("团"+allClique[i]+"组成的圆之间没有交集区域!需要进行分离处理.");
                isolateClique(allClique[i],circles);
                //System.out.println("分离结果:"+WsnFunction.isolateResult);
                allClique[i] = WsnFunction.isolateResult;//保存分离结果

            }
        }

        //存放所有的团
        String allcq = "";

        //进行团的隔离后,需要重新对团进行组合
        for (String eachcq : allClique) {
            //对每个团用;隔开
            allcq += eachcq +";";
        }

        return allcq.split(";");

    }

    //判断是否需要进行隔离团,是返回true,否则返回false
    public static boolean isIsolate(String[] allClique,Circle...circles) {
        for (String eachcq : allClique) {
            Circle[] groupCircle = cliquetoCircle(eachcq,circles);
            if(groupCircle.length >= 3 && !Circle.isMultiIntersection(groupCircle)) {
                //如果有一组团形成的圆没有交集
                return true;
            }
        }
        return false;
    }


    //统计孤立节点的个数
    public static int getIsolateNumber(String[] allClique) {
        int count = 0;
        for (String eachcq : allClique) {
            //每个团:5,21,28,12
            if(eachcq.split(",").length == 1) {
                count ++;
            }
        }
        return count;
    }

    //将所有团转换成对应的组,组的大小为团的个数+1,组的第一个元素为基站的位置
    public static Group[] createGroup(double networkSize,String[] allClique,Circle[] circles,Sensor...allSensor) {
        //组的个数=团的个数+1
        int glen = allClique.length + 1;
        //创建glen个组
        Group[] groups = new Group[glen];
        //初始化第一个组表示基站的位置
        groups[0] = new Group(0,new Point(networkSize/2,networkSize/2));
        //遍历每个团   5,21,28,12 初始化其余的组
        for(int i=1;i < groups.length;i++) {     		//i:[1,allClique.length]
            //先获得每个团对应的一组圆 5 21 28 12
            Circle[] groupCircle = cliquetoCircle(allClique[i-1],circles);//i-1:[0,allClique.length-1]
            //组内传感器个数 == 圆的个数
            Sensor[] groupSensor = new Sensor[groupCircle.length];
            //由对应的圆获得第i个组的传感器节点
            for(int j=0;j<groupSensor.length;j++) {
                //对应传感器的索引=传感器的编号-1
                int index = groupCircle[j].nodenum-1;
                groupSensor[j] = allSensor[index];
            }
            //得到第i个组的停止点位置
            Point stop = Circle.getStopPoint(groupCircle);
            //创建第i个分组
            groups[i] = new Group(i,stop,groupSensor);
        }

        return groups;
    }

    //按照tsp路径顺序对分组排序,返回形成哈密顿回路的分组顺序
    public static Group[] orderGroupbyTSP(String[] tspPath,Group...allgroup) {
//		System.out.println("TSP:");
//		for (String str : tspPath) {
//			System.out.print(str+" ");
//		}
        System.out.println();
        //哈密顿回路的节点个数 = 路径中节点的个数
        int n = tspPath.length;
        //创建分组
        Group[] tspGroup = new Group[n];
        //遍历tsp路径,确定每个分组的充电顺序
        for(int i=0;i<tspGroup.length;i++) {
            tspGroup[i] = allgroup[Integer.parseInt(tspPath[i])];
        }
        return tspGroup;
    }


    //计算每个分组的充电时间之和
    public static double getChargingTime(Group[] group) {
        double chargingTime = 0;
        //遍历每个分组,对分组的充电时间累加求和
        for (Group eg : group) {
            chargingTime += eg.ctime;
            //System.out.println("各组充电时间:"+eg.ctime);
        }
        return chargingTime;
    }


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
    }


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
    public static Group[] onetoOneGroup(double networkSize,Sensor...chargingSensor) {
        Group[] groups = new Group[chargingSensor.length+1];
        groups[0] = new Group(0,new Point(networkSize/2,networkSize/2));
        for(int i=1;i < groups.length;i++) {     		//i:[1,chargingSensor.length]
            //创建第i个分组
            groups[i] = new Group(i,chargingSensor[i-1].location,chargingSensor[i-1]);
        }
        return groups;
    }
*/

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

