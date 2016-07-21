import java.io.UnsupportedEncodingException;

public class Test {
    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String text = "硚";
        System.out.println(new String(text.getBytes("utf-8"),"gb2312"));
        System.out.print(text.getBytes("UTF-8").length + "\t");
        System.out.println(bytesToHexString(text.getBytes("UTF-8")));

        System.out.print(text.getBytes("GBK").length + "\t");
        System.out.println(bytesToHexString(text.getBytes("GBK")));

        System.out.print(text.getBytes("GB2312").length + "\t");
        System.out.println(bytesToHexString(text.getBytes("GB2312")));
    }
}