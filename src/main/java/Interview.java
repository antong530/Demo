import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by antong on 16/2/29.
 */
public class Interview {
    private static Integer[] arr = new Integer[]{1,2,3,4,5,6};
    public static void main(String[] args){
        for(int i = 0,j = arr.length - 1; i < (arr.length)/2 && j > (arr.length)/2; i++,j-- ){
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        for(int i=0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }
}
