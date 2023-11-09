package com.example.readcsvsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readcsvsample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter
    private var tempList = mutableListOf<TempData>()
    private var originList = mutableListOf<TempData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fileName = "sample.csv"
        val inputStream = assets.open(fileName)
        val file = File(filesDir,fileName)
        val list = binding.list
        list.layoutManager = LinearLayoutManager(this)
        //空のデータを使ってリストのインスタンス化をしておき、データ取得出来次第notifyDataSetChanged()
        adapter = ListAdapter(tempList)
        list.adapter = adapter
        if(! file.exists()){
            file.outputStream().use { inputStream.copyTo(it) }
        }
        lifecycleScope.launch {
            getData(file)
        }

        //スピナーで選ばれた都道府県に合わせてソート
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent?.selectedItem.toString()
                if (item != "全て"){
                    tempList.clear()
                    val data = originList.filter { it.ken.contains(item) }
                    tempList.addAll(data)
                    adapter.notifyDataSetChanged()
                }
                else{
                    tempList.clear()
                    tempList.addAll(originList)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
    private suspend fun getData(file: File){
        withContext(Dispatchers.Default){
            val inputStream = file.inputStream()
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferReader = BufferedReader(inputStreamReader)
            var lineData: String?
            var line = 0
            do {
                //lineDataに一行ずつの文字列を入れる
                lineData = bufferReader.readLine()
                //一行目は各列の項目名がついているので飛ばす
                if (line != 0){
                    // ","で分けて文字列のリストを取得
                    val data = lineData?.split(",")?: return@withContext
                    Log.d("lineData", "$data")
                    //欲しい列のデータを取得する
                    val ken = data[1]
                    val place = data[2]

                    val temp = data[9]
                    val maxTemp = data[21]
                    //データリストに入れる
                    tempList.add(TempData(ken, place, temp, maxTemp))
                    originList.add(TempData(ken, place, temp, maxTemp))
                }
                line += 1
            } while (lineData != null)     //一行分のデータが取得できなくなるまで繰り返す
        }
        //データの変更を通知
        adapter.notifyDataSetChanged()
    }
}
data class TempData(val ken: String,val place: String,val temp: String,val maxTemp: String)