package com.qifan.rotatelabelview

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qifan.library.RotateLabelView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                with(
                    MarginItemDecorator(
                        MarginItemDecorator.Space(
                            space = 5,
                            marginTop = 0
                        ),
                        MarginItemDecorator.Position.VERTICAL
                    )
                ) {
                    addItemDecoration(this)
                }
            }
            adapter = RotateLabelAdapter(
                arrayOf(
                    RotateLabel("TOP LEFT", "Shanghai", RotateLabelView.Type.TOP_LEFT),
                    RotateLabel("TOP RIGHT", "Shanghai", RotateLabelView.Type.TOP_RIGHT),
                    RotateLabel("BOTTOM LEFT", "Shanghai", RotateLabelView.Type.BOTTOM_LEFT),
                    RotateLabel("BOTTOM RIGHT", "Shanghai", RotateLabelView.Type.BOTTOM_RIGHT)
                )
            )
        }
    }

    class MarginItemDecorator(
        private val space: Space,
        private val decorator: Position = Position.ALL
    ) : RecyclerView.ItemDecoration() {

        private fun getSeparationSpaceTop(space: Space, isFirstItem: Boolean) = when {
            isFirstItem -> space.marginTop
            else -> space.top
        }

        private fun getSeparationSpaceBottom(space: Space, isLastItem: Boolean) = when {
            isLastItem -> space.marginBottom
            else -> space.bottom
        }

        private fun RecyclerView.isFirstItem(view: View): Boolean =
            getChildAdapterPosition(view) == 0

        private fun RecyclerView.isLastItem(view: View): Boolean =
            getChildAdapterPosition(view) == childCount - 1

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                when (decorator) {
                    Position.VERTICAL, Position.ALL -> {
                        top = getSeparationSpaceTop(space, parent.isFirstItem(view))
                        bottom = getSeparationSpaceBottom(space, parent.isLastItem(view))
                    }
                    Position.HORIZONTAL -> {
                        left = space.left
                        right = space.right
                    }
                }
            }
        }

        data class Space(
            val space: Int,
            val top: Int = space,
            val right: Int = space,
            val bottom: Int = space,
            val left: Int = space,
            val marginTop: Int = space,
            val marginBottom: Int = space
        )

        enum class Position {
            VERTICAL,
            HORIZONTAL,
            ALL
        }

    }
}
