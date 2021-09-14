package com.daisukekawamura.gitapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.api.load
import com.daisukekawamura.gitapiapp.databinding.ActivityMainBinding
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // アンダースコアを対処するGSONをインスタンス化
        val gson: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

        // baseのURLを設置して、interfaceで定義したリンクを追加するだけでOKな状態にする
        // JSONからDataClassへの変換を上記で生成したgsonインスタンスで実行できるようにretrofitに組み込む
        // それぞれを組み立てる
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        // 上記の設定のretrofitを作成したインターフェイスに付与する
        val userService: UserService = retrofit.create(UserService::class.java)

        binding.requestButton.setOnClickListener {
            // CoroutineScopeの中身は別スレッド！
            // runBlockingの中では、別スレッドが起動する
            runBlocking(Dispatchers.IO) {
                kotlin.runCatching {
                    userService.getUser("lifeistech")
                }
            }.onSuccess {
                binding.avatarImageView.load(it.avatarUrl)
                binding.nameTextView.text = it.name
                binding.userIdTextView.text = it.userId
                binding.followersTextView.text = it.followers.toString()
                binding.followingTextView.text = it.following.toString()
            }.onFailure {
                // アラートと一緒
                Toast.makeText(this, "失敗", Toast.LENGTH_LONG).show()
            }
        }
    }


}