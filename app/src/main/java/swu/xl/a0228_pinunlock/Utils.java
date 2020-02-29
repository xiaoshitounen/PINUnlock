package swu.xl.a0228_pinunlock;

public class Utils {
    /**
     * 将传递的 dp 值转化为 px
     * @param dp
     * @param density
     * @return
     */
    public static int dpToPx(int dp, float density){
        return (int) (dp * density);
    }

    /**
     * 将传递的 dp 值转化为 px
     * @param dp
     * @param density
     * @return
     */
    public static int dpToPx(float dp, float density){
        return (int) (dp * density);
    }

    /**
     * 将传递的 sp 值转化为 px
     * @param sp
     * @param density
     * @return
     */
    public static int spToPx(int sp, float density){
        return (int) (sp * density);
    }
}
