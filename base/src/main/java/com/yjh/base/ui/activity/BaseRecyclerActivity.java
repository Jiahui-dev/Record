package com.yjh.base.ui.activity;

import android.view.ViewStub;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yjh.base.R;
import com.yjh.base.decoration.SpaceItemDecoration;
import com.yjh.base.ui.adapter.BaseRecyclerAdapter;
import com.yjh.base.controller.LoadMoreController;
import com.yjh.base.controller.StateController;
import java.util.List;

/**
 * 列表专用基类
 * 1. 自动初始化 RecyclerView
 * 2. 自动管理 Adapter
 * 3. 自动管理 缺省页(StateController)
 * Created by youjiahui on 2026/1/28.
 */

public abstract class BaseRecyclerActivity<T> extends BaseRefreshActivity {

    protected RecyclerView mRecyclerView;
    protected BaseRecyclerAdapter<T> mAdapter;
    protected StateController mStateController;
    protected LoadMoreController mLoadMoreController;

    // 默认间距值
    private int mDefaultSpace = 16;

    @Override
    public void initView() {
        super.initView();//先初始化父类的 SwipeRefreshLayout

        //1.查找 RecyclerView
        int rvId=getResources().getIdentifier("contentView","id",getPackageName());
        if(rvId!=0){
            mRecyclerView=findViewById(rvId);
        }
        if(mRecyclerView!=null){
            //2.初始化 LayoutManager
            mRecyclerView.setLayoutManager(getLayoutManager());
            // 只在需要时添加默认间距
            if (shouldAddDefaultSpaceDecoration()) {
                mRecyclerView.addItemDecoration(new SpaceItemDecoration(mDefaultSpace));
            }

            //3.创建并绑定 Adapter
            mAdapter=createAdapter();
            mRecyclerView.setAdapter(mAdapter);

            //4.初始化状态控制器，查找空布局和错误布局
            mStateController=new StateController(this,mRecyclerView);
            int emptyStubId=getResources().getIdentifier("emptyStub","id",getPackageName());
            if(emptyStubId!=0){
                ViewStub stub=findViewById(emptyStubId);
                mStateController.setEmptyViewStub(stub);
            }

            int errorStubId=getResources().getIdentifier("errorStub","id",getPackageName());
            if(errorStubId!=0){
                ViewStub stub=findViewById(errorStubId);
                mStateController.setErrorViewStub(stub);
            }
            // 无论支不支持分页，统一初始化控制器，用来管理 Footer
            mLoadMoreController = new LoadMoreController(mRecyclerView, mAdapter);
            // 只有在支持分页的情况下，才去监听滑动触发加载更多
            if (isSupportLoadMore()) {
                mLoadMoreController.setOnLoadMoreListener(this::onLoadMore);
                // 注册控制器
                registerController("loadMore_controller", mLoadMoreController);
            }

            //注册生命周期
            registerController("state_controller",mStateController);
        }
    }

    /**
     * 数据请求成功后调用此方法
     * 停止刷新 + 填充数据 + 切换空布局状态
     */
    public void refreshListSuccess(List<T> list){
        refreshComplete();
        if (mAdapter != null) {
            mAdapter.setList(list);
        }
        if(mStateController!=null){
            mStateController.handleData(list);
        }

        if (mLoadMoreController != null) {
            // 下拉刷新成功时，我们需要告诉控制器：
            // 1. 如果列表不支持分页（比如你的首页），那么刷新完第一页，它直接就代表“没有更多数据了” (hasMore = false)
            // 2. 如果列表支持分页，我们假设第一页默认是“还有更多数据”的 (hasMore = true)
            boolean hasMore = isSupportLoadMore();

            // 让控制器去同步决定：是该隐藏 Footer 准备上拉，还是直接亮出“已经到底啦”
            mLoadMoreController.reset(hasMore,getEndFooterText());
        }
    }

    public void refreshListFailed(String msg) {
        refreshComplete();
        //如果当前列表是空的，就显示全屏错误页
        if(mAdapter==null||mAdapter.getItemCount()==0){
            if(mStateController!=null){
                mStateController.showError();
            }
        }else{
            //如果列表里已经有数据了，就不全屏显示，简单弹个吐司就行
            if (mLoadMoreController != null) {
                mLoadMoreController.loadMoreFail();
            } else {
                showError(msg);
            }
        }
    }

    /**
     * 默认线性布局，子类可覆盖
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    /**
     * 子类提供 Adapter
     */
    protected abstract BaseRecyclerAdapter<T> createAdapter();

    protected String getEndFooterText() {
        return "已经到底啦";
    }

    /**
     * 提供给子类手动调用：比如页面某个特殊按钮想触发点击重试底部的加载
     */
    protected void retryLoadMore() {
        if (mLoadMoreController != null) {
            mLoadMoreController.retryLoadMore();
        }
    }

    public void onLoadMore() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_base_recycler;
    }

    public void loadMoreSuccess(List<T> list, boolean hasMore) {
        if (mAdapter != null) {
            mAdapter.addList(list);
        }
        if (mLoadMoreController != null) {
            mLoadMoreController.loadMoreSuccess(hasMore);
        }
    }

    public void loadMoreFailed() {
        if (mLoadMoreController != null) {
            mLoadMoreController.loadMoreFail();
        }
    }

    protected boolean shouldAddDefaultSpaceDecoration() {
        return true;
    }

    /**
     * 设置默认间距值
     */
    protected void setDefaultSpace(int space) {
        mDefaultSpace = space;
    }

    protected boolean isSupportLoadMore() {
        return true;
    }

    /**
     * 强制显示内容区域（隐藏空布局/错误布局）
     */
    public void showContent() {
        if (mStateController != null) {
            mStateController.showContent();
        }
    }
}
