package multihoptest;

public class honeycomb {
    Point[] location;
    double edge;
    double networkSize=200;

    public  honeycomb(){
        double x=0;
        double y=0;
        int p=0;
        for(;y<=networkSize;){
            for(;x<=networkSize;){
                location[p]=new Point(x,y);
                p++;
                x+=2*edge;
            }
        }

    }
}
