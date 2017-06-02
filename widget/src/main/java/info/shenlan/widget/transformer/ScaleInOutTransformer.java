package info.shenlan.widget.transformer;

import android.view.View;

public class ScaleInOutTransformer extends ABaseTransformer {
    private int current;

    public ScaleInOutTransformer(int current) {
        this.current = current;
    }

    @Override
    protected void onTransform(View view, float position) {
        System.out.println(position);
//        view.setPivotX(position < 0 ? 0 : view.getWidth());
////        view.setPivotY(view.getHeight() / 2f);
//        float scale = position < 0 ? position - 1f  : 1f - position;
//        view.setScaleX(scale);
////        view.setScaleY(scale);


//        if (current != 0) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() / 2 * position);
//        } else {
//            view.setTranslationX(position < 0 ? -view.getWidth() / 2 * position : 0.8f * position);

//        }
//        System.out.println(view.getWidth());
//        float scale = position < 0 ? 0.5f + (0.5f * position * position) : position;
//        if (position > 0)
//        view.setScaleX(scale);

    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
