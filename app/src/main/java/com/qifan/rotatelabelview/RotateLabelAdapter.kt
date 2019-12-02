package com.qifan.rotatelabelview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.qifan.library.Label
import com.qifan.library.RotateLabelView
import kotlin.random.Random

/**
 * Created by Qifan on 2019-12-02.
 */
private const val VIEW_TOP_RIGHT = 0
private const val VIEW_TOP_LEFT = 1
private const val VIEW_BOTTOM_RIGHT = 2
private const val VIEW_BOTTOM_LEFT = 3

data class RotateLabel(
    val title: String,
    val label: Label,
    val type: RotateLabelView.Type
)

class RotateLabelAdapter(private val dataSource: Array<RotateLabel>) :
    RecyclerView.Adapter<RotateLabelAdapter.RotateLabelViewHolder>() {
    private val RANDOM_COLORS = arrayOf(
        Color.parseColor("#E91E63"),
        Color.parseColor("#673AB7"),
        Color.parseColor("#3F51B5"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#009688"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#795548")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RotateLabelViewHolder {
        val view = when (viewType) {
            VIEW_TOP_LEFT -> LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_top_left,
                parent,
                false
            )
            VIEW_TOP_RIGHT -> LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_top_right,
                parent,
                false
            )
            VIEW_BOTTOM_LEFT -> LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_bottom_left,
                parent,
                false
            )
            else -> LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_bottom_right,
                parent,
                false
            )
        }
        return RotateLabelViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun getItemViewType(position: Int): Int {
        return when (dataSource[position].type) {
            RotateLabelView.Type.TOP_LEFT -> VIEW_TOP_LEFT
            RotateLabelView.Type.TOP_RIGHT -> VIEW_TOP_RIGHT
            RotateLabelView.Type.BOTTOM_LEFT -> VIEW_BOTTOM_LEFT
            RotateLabelView.Type.BOTTOM_RIGHT -> VIEW_BOTTOM_RIGHT
        }
    }

    override fun onBindViewHolder(holder: RotateLabelViewHolder, position: Int) {
        holder.title.text = dataSource[position].title
        holder.rotateLabel.setLabel(dataSource[position].label)
        holder.rotateLabel.setBackgroundColor(RANDOM_COLORS[Random.nextInt(RANDOM_COLORS.size)])
        holder.rotateLabel.setType(dataSource[position].type)
    }

    inner class RotateLabelViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val title: AppCompatTextView = itemView.findViewById(R.id.title)
        internal val rotateLabel: RotateLabelView = itemView.findViewById(R.id.rotate_label)
    }
}
