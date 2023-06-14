@file:Suppress("DEPRECATION")

package app.submission.movieapp.presentation.utils

//noinspection SuspiciousImport
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.*

fun ImageView.loadImage(url: String?, cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE) {
    Glide.with(this)
        .load(url)
        .override(480, 320)
        .thumbnail(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade())
        .timeout(20000)
        .diskCacheStrategy(cacheStrategy)
        .into(this)
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

fun isLastVisible(rv: RecyclerView): Boolean {
    if (rv.adapter!!.itemCount != 0) {
        val lastVisibleItemPositions =
            (Objects.requireNonNull(rv.layoutManager) as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                null
            )
        val lastVisibleItemPosition: Int = getLastVisibleItem(lastVisibleItemPositions)
        return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == rv.adapter!!.itemCount - 1
    }
    return false
}

fun RecyclerView.listenForPagination(onLoadMore: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isLastVisible(recyclerView)) {
                onLoadMore()
            }
        }
    })
}

fun isLastVisibleLinear(rv: RecyclerView, shouldFindLastCompletelyVisible: Boolean = true): Boolean {
    val layoutManager = rv.layoutManager as LinearLayoutManager
    val pos = if (shouldFindLastCompletelyVisible) {
        layoutManager.findLastCompletelyVisibleItemPosition()
    } else {
        layoutManager.findFirstVisibleItemPosition()
    }
    val numItems: Int = rv.adapter?.itemCount ?: 0
    return pos >= numItems - 1
}

fun RecyclerView.listenForPaginationLinear(onLoadMore: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isLastVisibleLinear(recyclerView)) {
                onLoadMore()
            }
        }
    })
}

fun getInitials(text: String): String {
    var initials = ""
    val myName: List<String> = text.split(" ")
    for (i in myName.indices) {
        val s = myName[i]
        initials += s[0]
    }
    return initials.uppercase()
}

class MarginItemDecorationVertical(private val spaceHeight: Int, private val spaceWeight: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }
            bottom = spaceHeight
            left = spaceWeight
            right = spaceWeight
        }
    }
}