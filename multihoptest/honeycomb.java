package multihoptest;

public class honeycomb {
    Point[] location;
    double edge;
    double networkSize=200;

    public  honeycomb(){
        double x=0;
        double y=0;
        int p=0,xi=0,yi=0;
        for(;x<=networkSize;){
            for(;y<=networkSize;){
                if(xi%2== 0&&yi%2==0) {
                    y=y+yi*edge;
                    x=x+xi*edge*Math.sqrt(3)/2;
                    location[p]=new Point(x,y);
                }
                p++;

            }

        }

    }
}
