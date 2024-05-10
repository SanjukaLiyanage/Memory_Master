package com.sanjuka.memorymaster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sanjuka.memorymaster.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity()
{
    private lateinit var binding:ActivityStartBinding
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStart.setOnClickListener {
            startActivity(Intent(this@StartActivity,MainActivity::class.java))
        }
    }
}