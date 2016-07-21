import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by antong on 15/12/7.
 */
public class MergeSort {
    public void mergeSort(int[] a, int[] tmp, int start, int end){
        int mid;
        if(start<end){
            mid = (start+end)/2;
            mergeSort(a,tmp,start,mid);
            mergeSort(a,tmp,mid+1,end);
            merge(a,tmp,start,mid,end);
        }
    }
    public void merge(int[] a, int[] tmp, int start, int mid, int end){
        int i = start,j = mid + 1,k = start;
        while (i != mid + 1 && j != end + 1){
            if(a[j] > a[i]){
                tmp[k++] = a[i++];
            }else {
                tmp[k++] = a[j++];
            }
        }
        while (i != mid +1){
            tmp[k++] = a[i++];
        }
        while (j != end +1){
            tmp[k++] = a[j++];
        }
        for(int i1 = start;i1<end;i1++){
            a[i1] = tmp[i1];
        }
    }

    public static Object createProxy(final Object target){
        return Proxy.newProxyInstance(MergeSort.class.getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(method.equals("")){
                    method.invoke(target,args);
                }
                return null;
            }
        });
    }
}
