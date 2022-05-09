package com.johnson.asm.demo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnson.asm.common.doubletap.DoubleTap

class TestAdapter : RecyclerView.Adapter<TestAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_item_view, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindViewHolder(i)
    }

    override fun getItemCount(): Int {
        return 300
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var itemView222: View = itemView

        @DoubleTap(4000)
        override fun onClick(v: View?) {
            v?.context?.apply {
                Log.e(">>>>>> ", "<<<<<<<<<<< 点击")
//                startActivity(Intent(this, ModuleActivity::class.java))
            }
        }

        fun bindViewHolder(position: Int) {
            itemView.findViewById<TextView>(R.id.titleTv).text = "这是第" + position + "条目"
            itemView.setOnClickListener(this)
//            itemView.setOnClickListener {
//                Log.e(">>>>>> ", "<<<<<<<<<<< 点击 $itemView")
//            }
        }
    }

    fun View.isVisible(visible: Boolean): Boolean {
        return if (visible) {
            this.visibility = View.VISIBLE
            true
        } else {
            this.visibility = View.GONE
            false
        }
    }

    var isDragOnLongPressEnabled = true
        set(value) {
            field = value
            Log.e("if xxxxx ", "ififififififif")
        }

}
