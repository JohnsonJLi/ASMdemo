package com.johnson.asm.demo

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.johnson.asm.common.ToastHelper
import com.johnson.asm.common.doubletap.DoubleTap
import com.johnson.asm.common.timelog.TimeLog
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SecondActivity : AppCompatActivity() {

    var poolExecutor: ExecutorService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestAdapter2().apply {
            setOnItemClickListener(object : OnItemClickListener {
                @TimeLog
                @DoubleTap(5000)
                override fun onItemClick(
                    adapter: BaseQuickAdapter<*, *>,
                    view: View,
                    position: Int
                ) {
                    ToastHelper.toast(
                        getItem(position),
                        view
                    )
                }
            })
            setOnItemChildClickListener(object : OnItemChildClickListener {
                @TimeLog
                @DoubleTap(10)
                override fun onItemChildClick(
                    adapter: BaseQuickAdapter<*, *>,
                    view: View,
                    position: Int
                ) {
                    ToastHelper.toast(
                        getItem(position) + "Child : " + itemCount,
                        view
                    )

                }
            })
        }
        poolExecutor = Executors.newFixedThreadPool(2)
        val wifiMgr = applicationContext.getSystemService(Context.WIFI_SERVICE)
                as WifiManager
        /*       val info = wifiMgr.connectionInfo
               val ssid = info?.bssid*/

        val list: MutableList<PackageInfo> = mutableListOf()
        val pm: PackageManager = packageManager
        val installedPackages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        //   val newList = PrivacyUtils.getAppPackageInfo(pm, PackageManager.GET_META_DATA)
        for (pi in installedPackages) {
            // list.add(pi)
        }
    }
}

public class TestAdapter2 : BaseQuickAdapter<String, BaseViewHolder>(R.layout.recycler_item_view) {
    init {
        setNewInstance(mutableListOf<String>().apply {
            for (i in 1..100) {
                add("这是第 $i 条目")
            }
        })
        addChildClickViewIds(R.id.titleTv2)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.titleTv, item)
    }

//    @TimeLog
    override fun getItemCount(): Int {
        return super.getItemCount()
    }

//    @TimeLog
    override fun getItem(position: Int): String {
        return super.getItem(position)
    }

}
