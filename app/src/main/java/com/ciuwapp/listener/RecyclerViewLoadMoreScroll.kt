package com.ciuwapp.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll : RecyclerView.OnScrollListener {

    private var visibleThreshold = 1
    private var mOnLoadMoreListener: OnLoadMoreListener? = null
    var loaded: Boolean = false

    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var mLayoutManager: RecyclerView? = null

    fun setLoaded() {
        loaded = false
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    constructor(layoutManager: RecyclerView) {
        this.mLayoutManager = layoutManager
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        totalItemCount = mLayoutManager?.layoutManager!!.itemCount

        if (mLayoutManager?.layoutManager is LinearLayoutManager) {
            lastVisibleItem = (mLayoutManager?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (!loaded && totalItemCount <= lastVisibleItem + visibleThreshold) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener!!.onLoadMore()
            }
            loaded = true
        }
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}
