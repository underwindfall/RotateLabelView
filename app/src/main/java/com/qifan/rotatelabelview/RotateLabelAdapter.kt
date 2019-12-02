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

data class RotateLabel(
    val title: String,
    val label: Label,
    val type: Type
) {
    enum class Type {
        TOP_RIGHT,
        TOP_LEFT,
        BOTTOM_RIGHT,
        BOTTOM_LFET,
        TOP_RIGHT_FILLED,
        TOP_LEFT_FILLED,
        BOTTOM_RIGHT_FILLED,
        BOTTOM_LEFT_FILLED
    }
}

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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return RotateLabelViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: RotateLabelViewHolder, position: Int) {
        holder.title.text = dataSource[position].title
        holder.rotateLabel.setLabel(dataSource[position].label)
        holder.rotateLabel.setBackgroundColor(RANDOM_COLORS[Random.nextInt(RANDOM_COLORS.size)])
    }

    inner class RotateLabelViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val title: AppCompatTextView = itemView.findViewById(R.id.title)
        internal val rotateLabel: RotateLabelView = itemView.findViewById(R.id.rotate_label)
    }
}
