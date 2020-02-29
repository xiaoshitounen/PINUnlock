package swu.xl.a0228_pinunlock;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Lock extends ViewGroup {

    //锁图片的视图
    private ImageView lockImgView;
    //锁文本的视图
    private TextView lockTextView;

    //屏幕的密度
    float density = getResources().getDisplayMetrics().density;

    /**
     * 构造方法
     * @param context
     */
    public Lock(Context context) {
        super(context);

        //设置背景颜色
        this.setBackgroundColor(Color.parseColor("#F5EFFC"));
        //this.setBackgroundColor(Color.argb(1.0f,245.0f,239.0f, 252.0f));
    }

    /**
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //锁的图片视图
        if(lockImgView == null){
            lockImgView = new ImageView(getContext());
            lockImgView.setBackgroundResource(R.drawable.lock1);
            lockImgView.layout(Utils.dpToPx(5, density),
                    Utils.dpToPx(0, density),
                    Utils.dpToPx(35, density),
                    Utils.dpToPx(30, density));
            addView(lockImgView);
        }

        //锁的文本视图
        if(lockTextView == null){
            lockTextView = new TextView(getContext());
            lockTextView.setText("密码");
            lockTextView.setTextColor(Color.BLACK);
            lockTextView.setTextSize(Utils.spToPx(6, density));
            lockTextView.setGravity(Gravity.CENTER);
            lockTextView.layout(Utils.dpToPx(35, density),
                    Utils.dpToPx(6, density),
                    Utils.dpToPx(35+45, density),
                    Utils.dpToPx(0+30, density));
            addView(lockTextView);
        }
    }

    //setter/getter
    public ImageView getLockImgView() {
        return lockImgView;
    }

    public void setLockImgView(ImageView lockImgView) {
        this.lockImgView = lockImgView;
    }

    public TextView getLockTextView() {
        return lockTextView;
    }

    public void setLockTextView(TextView lockTextView) {
        this.lockTextView = lockTextView;
    }
}
