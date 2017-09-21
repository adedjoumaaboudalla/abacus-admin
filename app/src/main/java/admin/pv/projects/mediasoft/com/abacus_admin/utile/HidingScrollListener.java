package admin.pv.projects.mediasoft.com.abacus_admin.utile;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Abdallah on 12/01/2016.
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    protected static final int HIDE_THRESHOLD = 30;
    protected int scrolledDistance = 0;
    protected boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();
    public abstract void onShow();

}