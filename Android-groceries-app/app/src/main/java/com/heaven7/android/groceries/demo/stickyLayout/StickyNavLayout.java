package com.heaven7.android.groceries.demo.stickyLayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.heaven7.android.groceries.demo.R;
import com.heaven7.core.util.Logger;

/**
 * 仿360 软件详情:
 *
 * @链接 http://blog.csdn.net/lmj623565791/article/details/43649913
 * @注意    <li>1, 注意设置  viewpager的高度 <pre>
 *    ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
 * params.height = getMeasuredHeight() - mNav.getMeasuredHeight(); </pre>
 * <li>2, 顶部 view(高度必须是固定的) 的 隐藏和显示问题。注意{@link #onInterceptTouchEvent(MotionEvent)},何时返回true
 * <li>3, 控制StickyNavLayout 滑动的高度，使得滑动距离限定在范围内,eg: mTopViewHeight
 * <li>4, OverScroller 与 VelocityTracker的使用
 */
public class StickyNavLayout extends LinearLayout {

    private static final String TAG = "StickyNavLayout";
    /**
     * 顶部的view
     */
    private View mTop;

    /**
     * 带指示器的view
     */
    private View mNav;

    private ViewPager mViewPager;

    private int mTopViewHeight;
    private boolean isTopHidden = false;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private int mLastY;
    private boolean mDragging;

    private final int mTopViewId;
    private final int mIndicatorId;
    private final int mViewPagerId;

    private IStickyDelegate mStickyDelegate;
    private final Rect mExpectTopRect = new Rect();
    private boolean mNeedIntercept;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//触摸阙值
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StickyLayout);
        mTopViewId = a.getResourceId(a.getIndex(R.styleable.StickyLayout_stickyLayout_top_id), 0);
        mIndicatorId = a.getResourceId(a.getIndex(R.styleable.StickyLayout_stickyLayout_indicator_id), 0);
        mViewPagerId = a.getResourceId(a.getIndex(R.styleable.StickyLayout_stickyLayout_viewpager_id), 0);
        a.recycle();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mExpectTopRect.set(0, 0, dm.widthPixels, dm.heightPixels);
        //getWindowVisibleDisplayFrame(mExpectTopRect);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTop = findViewById(mTopViewId);
        mNav = findViewById(mIndicatorId);

        View view = findViewById(mViewPagerId);
        if (!(view instanceof ViewPager)) {
            throw new RuntimeException("must used ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置viewpager的高度 (将mViewPager。的高度设置为  整个 Height - 导航的高度)
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    public void setStickyDelegate(IStickyDelegate interceptot) {
        this.mStickyDelegate = interceptot;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 1, 上拉的时候，停靠后分发给child滑动. max = mTopViewHeight
         * 2, 下拉时，先拉上面的，拉完后分发给child.
         *
         *  final float offsetX = mScrollX - child.mLeft;
         final float offsetY = mScrollY - child.mTop;
         直接对MotionEvent进行坐标变换，将MotionEvent传递下去
        event.offsetLocation(offsetX, offsetY);
        handled = child.dispatchTouchEvent(event);
                   回复MotionEvent
        event.offsetLocation(-offsetX, -offsetY);
        */
        int action = ev.getAction();
        int y = (int) (ev.getY() + 0.5f);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                //Logger.i(TAG, "onInterceptTouchEven", "top visible = " + 	mTop.getLocalVisibleRect(mExpectTopRect));
                //mTop.getWindowVisibleDisplayFrame(mExpectTopRect);
                //Logger.i(TAG, "onInterceptTouchEvent_getWindowVisibleDisplayFrame", "top: " + mExpectTopRect + ",top = " + getTop());
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;

                //Logger.i(TAG, "onInterceptTouchEven", "top visible = " + 	mTop.getLocalVisibleRect(mExpectTopRect));
                //mTop.getWindowVisibleDisplayFrame(mExpectTopRect);
                //Logger.i(TAG, "onInterceptTouchEvent_getWindowVisibleDisplayFrame", "top: " + mExpectTopRect + ",top = " + getTop());

                if (Math.abs(dy) > mTouchSlop) {
                    if(mNeedIntercept){
                        return true;
                    }
                    if (dy > 0) {
                        return getScrollY() == mTopViewHeight;
                    }
                    if (mStickyDelegate != null && mStickyDelegate.shouldIntercept(this, dy,
                            isTopHidden ? VIEW_STATE_HIDDLE : VIEW_STATE_SHOW)) {
                        mDragging = true;
                        return true;
                    }
                }
			/*getCurrentScrollView();
			if (Math.abs(dy) > mTouchSlop) {
				mDragging = true;
             //当top没有隐藏时，就拦截触摸事件（即不让viewpager接收 Touch event）
				if (mInnerScrollView instanceof ScrollView) {
					if (!isTopHidden  
							|| (mInnerScrollView.getScrollY() == 0 
									&& isTopHidden && dy > 0)  ) {
						//viewpager向下滑动完毕,即将显示topview时
						return true;
					}
				} else if (mInnerScrollView instanceof ListView) {
					ListView lv = (ListView) mInnerScrollView;
					View c = lv.getChildAt(lv.getFirstVisiblePosition());
					if (!isTopHidden
							|| (c != null && c.getTop() == 0 && isTopHidden && dy > 0)) {
						//listview 滑动到顶部，并且要继续向下滑动时，拦截触摸
						return true;
					}
				}else if(mInnerScrollView instanceof RecyclerView){
					final RecyclerView rv = (RecyclerView) mInnerScrollView;
					final int position = findFirstVisibleItemPosition(rv);
					final View child = rv.getChildAt(position);
					if (!isTopHidden
							|| (child != null && child.getTop() == 0 && isTopHidden && dy > 0)) {
						//listview 滑动到顶部，并且要继续向下滑动时，拦截触摸
						return true;
					}
				}

			}*/
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public static int findFirstVisibleItemPosition(RecyclerView rv) {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        int firstPos = RecyclerView.NO_POSITION;
        if (lm instanceof GridLayoutManager) {
            firstPos = ((GridLayoutManager) lm).findFirstVisibleItemPosition();

        } else if (lm instanceof LinearLayoutManager) {
            firstPos = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();

        } else if (lm instanceof StaggeredGridLayoutManager) {
            int positions[] = ((StaggeredGridLayoutManager) lm).findFirstVisibleItemPositions(null);
            for (int pos : positions) {
                if (pos < firstPos) {
                    firstPos = pos;
                }
            }
        }
        return firstPos;
    }


    /**
     * 获取viewpager里面当前fragment的滑动的view (eg: listview,scrollview)。如果需要
     */
/*	private void getCurrentScrollView() {

		PagerAdapter a = mViewPager.getAdapter();
		
		//得到fragement中滑动的 视图view
		if (a instanceof FragmentPagerAdapter) {
			FragmentPagerAdapter fadapter = (FragmentPagerAdapter) a;
			Fragment item = fadapter.getItem(currentItem);
			mInnerScrollView = (ViewGroup) (item.getView()
					.findViewById(R.id.id_stickynavlayout_innerscrollview));
		} else if (a instanceof FragmentStatePagerAdapter) {
			FragmentStatePagerAdapter fsAdapter = (FragmentStatePagerAdapter) a;
			Fragment item = fsAdapter.getItem(currentItem);
			mInnerScrollView = (ViewGroup) (item.getView()
					.findViewById(R.id.id_stickynavlayout_innerscrollview));
		}else if( a instanceof BaseFragmentPagerAdapter){
			final Fragment item = ((BaseFragmentPagerAdapter) a).getFragment(currentItem);
			mInnerScrollView = (ViewGroup) (item.getView()
					.findViewById(R.id.id_stickynavlayout_innerscrollview));
		}else{
			throw new IllegalStateException("can't find inner scroller view");
		}
	}*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int action = event.getAction();
        int y = (int) (event.getY() + 0.5f);

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mVelocityTracker.addMovement(event);
                mLastY = y;
                return true;

            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;

                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    //手向下滑动， dy >0 否则 <0.
                    final int scrollY = getScrollY();
                    Logger.i(TAG, "onTouchEvent", "ScrollY = " + scrollY + " ,dy = " + dy + " , mTopViewHeight = " + mTopViewHeight);
                    //scrollBy(0, (int) -dy); //手势向下滑动，view向上滑动
                    mLastY = y;

                    if (dy < 0) {
                        //手势向上滑动
                        /**
                         *  called [ onTouchEvent() ]: ScrollY = 666 ,dy = -7.4692383 , mTopViewHeight = 788
                         *  called [ onTouchEvent() ]: ScrollY = 673 ,dy = -3.748291 , mTopViewHeight = 788
                         */
                        if (scrollY == mTopViewHeight) {
                            //分发给child
                            mStickyDelegate.offsetTopAndBottom(dy);
                           // return mStickyDelegate.dispatchTouchEventToChild(event);
                        } else if (scrollY - dy > mTopViewHeight) {
                            scrollTo(getScrollX(), mTopViewHeight);
                        } else {
                            scrollBy(0, -dy);
                        }
                    } else {
                        //手势向下
                        if (scrollY == 0) {
                            //分发事件给child
                            mStickyDelegate.offsetTopAndBottom(dy);
                           // return mStickyDelegate.dispatchTouchEventToChild(event);
                        } else {
                            if (scrollY - dy < 0) {
                                dy = scrollY;
                            }
                            scrollBy(0, -dy);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                if (!mScroller.isFinished()) {//如果手离开了,就终止滑动
                    mScroller.abortAnimation();
                }
                break;

            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity); //1000表示像素/秒
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void fling(int velocityY) {
        //使得当前对象只滑动到mTopViewHeight
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override //限定滑动的y范围
    public void scrollTo(int x, int y) {
       // Logger.i(TAG, "scrollTo", "x = " + x + ", y = " + y);
        if (y < 0) {
            y = 0;
        }
        //使得 StickyNavLayout 只滑动 mTopViewHeight
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y == 0 || y == mTopViewHeight) {
            mNeedIntercept = false;
        }else{
            mNeedIntercept = true;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
           // System.out.println("y != getScrollY()");
        }
        isTopHidden = getScrollY() == mTopViewHeight;
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        if (mScroller.computeScrollOffset()) {//true滑动尚未完成
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public interface IStickyDelegate {
        /**
         * called when you should intercept child's touch event.
         *
         * @param snv          the StickyNavLayout
         * @param dy           the delta y distance
         * @param topViewState the view state of top view. {@link #VIEW_STATE_SHOW} or {@link #VIEW_STATE_HIDDLE}
         * @return true to intercept
         */
        boolean shouldIntercept(StickyNavLayout snv, int dy, int topViewState);

        boolean dispatchTouchEventToChild(MotionEvent event);

        void offsetTopAndBottom(int dy);
    }

    public static final int VIEW_STATE_SHOW = 1;
    public static final int VIEW_STATE_HIDDLE = 2;
}
