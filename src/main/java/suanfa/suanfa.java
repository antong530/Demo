package suanfa;

/**
 * Created by antong on 16/1/25.
 */
public class suanfa {
    public static void main(String[] args){

    }
    public static int fib(int n) {
        if (n == 1 || n == 2) {
            return n;
        }else {
            return fib(n-1) + fib(n-2);
        }
    }

//    public static Integer[][] getArr(){
//        Integer[][] arr = new Integer[10][10];
//        for(int i = 0;i< arr.length;i++){
//            arr[i][0] = 1;
//            arr[i][i] = 1;
//        }
//        for(int i = 2;i<arr.length;i++){
//            for(int j = i;j<i;j++){
//                arr[i][j] = arr[i-1][j-1] + arr[i-1][j];
//            }
//        }
//        for()
//    }
}
