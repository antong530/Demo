import java.util.*;

/**
 * Created by antong on 16/3/30.
 */
public class TestMap extends TestMapA {
    private int i = 0;
    public TestMap(int i) {
        super(i);
        System.out.println("testMap"+this.i);
    }

    public static void main(String[] args){
        System.out.print(new TestMap(3).i);
    }
}
