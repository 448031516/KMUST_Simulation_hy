package multihoptest;


public class run {

    public static void main(String[] args) {
        //网络规模
        double networkSize = 2000;
        //传感器节点个数
        int nodenum = 15000;
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
        honeycomb test = new honeycomb(27.00, 2100.00);    //创建蜂窝
        Point[] k = test.creat_honeycomb(27.00, 2100.00);//获取每个蜂窝的中心坐标

        for (int i = 0; i < allSensor.length; i++) {                //将节点分簇，每个节点的inHoneycomb值表示其所在的簇
            allSensor[i].inHoneycomb = WsnFunction.judgeHoneycomb(allSensor[i], test);
            //System.out.println("编号:"+allSensor[i].number+" 坐标:("+allSensor[i].location.x+","+allSensor[i].location.y+")属于编号为"+allSensor[i].inHoneycomb+"的正六边形，其中心坐标为"+"("+k[WsnFunction.judgeHoneycomb(allSensor[i],test)].x+","+k[WsnFunction.judgeHoneycomb(allSensor[i],test)].y+")");
        }

        //========================选取锚点========================
        Point[] Anchor = new Point[k.length];   //存放每个簇的锚点坐标，Anchor[0]表示第0个正六边形区域内的锚点位置
        Point[][] initialAnchor = WsnFunction.initialAnchor(test);     //初选锚点

//        for(int i=0;i<Anchor.length;i++)
//            for(int j=0;j<6;j++)
//            System.out.println(initialAnchor[i][j].x+","+initialAnchor[i][j].y);
//            //测试，输出某个锚点覆盖的节点数量
//        int number = WsnFunction.cloverNUM(initialAnchor[11][3],allSensor);
//        System.out.println(number);

        for (int i = 0; i < Anchor.length; i++) {
            int num = 0, temp_1 = 0;
            for (int j = 0; j < 6; j++) {
                if (num < WsnFunction.cloverNUM(initialAnchor[i][j], allSensor)) {
                    num = WsnFunction.cloverNUM(initialAnchor[i][j], allSensor);
                    temp_1 = j;
                }
            }
//            System.out.println(i+","+temp_1+","+num);
            if(num==0)  Anchor[i] = new Point(-1,-1,-1);
             else   Anchor[i] = initialAnchor[i][temp_1];    //确定第i个正六边形区域的锚点坐标

            //测试输出
            //System.out.println("第"+Anchor[i].num+"个簇的锚点坐标为（"+Anchor[i].x+"，"+Anchor[i].y+"),且以此为锚点，MC能覆盖到的节点个数为："+num);
        }


        //========================整理，删除只能覆盖0个节点的锚点========================
        int h = 0; // 设置一个变量作为增量
        // 循环读取Anchor数组的值
        for (Point b : Anchor) {
            // 判断，如果Anchor数组的num值不为0那么h就加1
            if (b.num != -1) {
                h++;
            }
        }
        // 得到了数组里不为0的个数，以此个数定义一个新数组，长度就是h
        Point[] newAnchor = new Point[h];
        // 偷个懒，不想重新定义增量了，所以把增量的值改为0
        h = 0;
        // 在次循环读取Anchor数组的值
        for (Point c : Anchor) {
            // 把不为0的值写入到newAnchor数组里面
            if (c.num != -1) {
                newAnchor[h] = c;
                h++;// h作为newArr数组的下标，没写如一个值，下标h加1
            }
        }
//
//        for (Point node:newAnchor
//             ) {
//            System.out.println("第"+node.num+"个簇的锚点坐标为（"+node.x+"，"+node.y+"),且以此为锚点，MC能覆盖到的节点个数为：");
//
//        }

    //========================更新节点被覆盖的情况，将能够直接被MC覆盖的节点isCloverDirect置为true========================
    for(int i=0;i<newAnchor.length;i++){
        WsnFunction.cloverNODE(newAnchor[i],allSensor);
    }
//        System.out.println("更新节点信息如下");
//        for(Sensor node:allSensor) {
//            System.out.println("编号:"+node.number+" 坐标:("+node.location.x+","+node.location.y+"）属于序号为"+node.inHoneycomb+"的簇，其能否被直接覆盖到："+node.isClover+"剩余能量阈值:"+node.remainingE/node.maxCapacity +" 剩余寿命:"+node.remainingE/node.ecRate);
//        }
    //========================多跳开始，确定多跳路径========================
        //首先，将每一个簇的节点归类放到cluster数组中
        Sensor[][] cluster= new Sensor[Anchor.length][];
        for (int i=0; i < Anchor.length;i++){
            int f=0,num=0;
           for (int j=0; j < allSensor.length;j++){
               if(allSensor[j].inHoneycomb==i) num++;
           }
           cluster[i] = new Sensor[num];
            for (int j=0; j < allSensor.length;j++) {
                if(allSensor[j].inHoneycomb==i) cluster[i][f++] = allSensor[i];
            }
        }
       // System.out.println(cluster.length);
    // 第i个簇中
    for (int i=0;i < cluster.length;i++) {
        while (WsnFunction.IF_noPATH(cluster[i])){
            cluster[i] = WsnFunction.multihop_PATH(cluster[i]);

//        for(int j=0;j < cluster[i].length;j++){
//            if (!cluster[i][j].isClover)  {
//            int     nextHOP=-1 ;
//            double maxERrate=0;
//            boolean change =false;
//                for (int f=0;f < cluster[i].length;f++){
//                    if (cluster[i][f].isClover && cluster[i][j].getERRate(Sensor.getDistance(cluster[i][j],cluster[i][f])) > maxERrate )     //如果选择的下一跳节点为MC直接覆盖节点，且以此为其中继节点能量传输效率高，则记录此中继节点
//                        maxERrate = cluster[i][j].getERRate(Sensor.getDistance(cluster[i][j],cluster[i][f]));
//                        nextHOP = f;
//                        change  = true;
//                }
//                cluster[i][j].erRateEFF=cluster[i][j].getERRate(Sensor.getDistance(cluster[i][j],cluster[i][nextHOP]));
//                cluster[i][j].multihop = nextHOP;
//
//            }
//        }

            //从得到的所有未被覆盖节点中选取erRateEFF最大的节点及其路径（下一跳）
            double maxERrate = 0;
            int sensor_maxERrate = -1;
            for (int f1 = 0; f1 < cluster[i].length; f1++) {
                if (!cluster[i][f1].isClover && cluster[i][f1].erRateEFF > maxERrate) {
                    maxERrate = cluster[i][f1].erRateEFF;
                    sensor_maxERrate = f1;
                }
            }
            if (sensor_maxERrate >= 0) {
                cluster[i][sensor_maxERrate].isClover = true;//将最大erRateEFF的节点加入到已覆盖的集合中
            }
        }

    }






//        //========================TSP开始========================
//        //使用遗传算法
//        int[] best; //best[]中存放tsp输出顺序
//
//        //=======================method 1=======================
//        GeneticAlgorithm ga = new GeneticAlgorithm();
//        best = ga.tsp(getDist(Anchor));
//
//        int n = 0;
//        while (n++ < 100) {
//            best = ga.nextGeneration();
//
//            System.out.println("best distance:" + ga.getBestDist() +
//                    " current generation:" + ga.getCurrentGeneration() +
//                    " mutation times:" + ga.getMutationTimes());
//            System.out.print("best path:");
//            for (int i = 0; i < best.length; i++) {
//                System.out.print(best[i] + " ");
//            }
//            System.out.println();
//        }

        //=======================method 2========================
//        GeneticAlgorithm ga = GeneticAlgorithm.getInstance();
//
//        ga.setMaxGeneration(1000);
//        ga.setAutoNextGeneration(true);
//        best = ga.tsp(getDist(Anchor));
//        System.out.print("best path:");
//        for (int i = 0; i < best.length; i++) {
//            System.out.print(best[i] + " ");
//        }
//        System.out.println();
    }




    private static float[][] getDist(Point[] points) {
        float[][] dist = new float[points.length][points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                dist[i][j] = (float) Point.getDistance(points[i], points[j]);
            }
        }
        return dist;
    }

//    private static float distance(com.onlylemi.genetictsp.Point p1, com.onlylemi.genetictsp.Point p2) {
//        return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
//    }



}
