package com.sanjuka.memorymaster

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.sanjuka.memorymaster.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private var score = 0
    private var highScore = 0
    private lateinit var sharedPreferences: SharedPreferences
    private var result: String = ""
    private var userAnswer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE)

        // Load high score
        highScore = sharedPreferences.getInt("highScore", 0)
        updateHighScoreUI()

        //initViews
        binding.apply {
            panel1.setOnClickListener(this@MainActivity)
            panel2.setOnClickListener(this@MainActivity)
            panel3.setOnClickListener(this@MainActivity)
            panel4.setOnClickListener(this@MainActivity)
            startGame()
        }
    }

    private fun updateHighScoreUI() {
        binding.tvHighScore.text = "High Score: $highScore"
    }

    private fun saveHighScore() {
        val editor = sharedPreferences.edit()
        editor.putInt("highScore", highScore)
        editor.apply()
    }

    private fun startGame() {
        result = ""
        userAnswer = ""
        disableButtons()
        lifecycleScope.launch {
            val round = (3..5).random()
            repeat(round) {
                delay(400)
                val randomPanel = (1..4).random()
                result += randomPanel

                val panel = when (randomPanel) {
                    1 -> binding.panel1
                    2 -> binding.panel2
                    3 -> binding.panel3
                    else -> binding.panel4
                }

                val drawableYellow =
                    ActivityCompat.getDrawable(this@MainActivity, R.drawable.btn_yellow)
                val drawableDefault =
                    ActivityCompat.getDrawable(this@MainActivity, R.drawable.btn_state)
                panel.background = drawableYellow
                delay(1000)
                panel.background = drawableDefault
            }
            runOnUiThread {
                enableButtons()
            }

            // Check if the current score is higher than the high score and save it if necessary
            if (score > highScore) {
                highScore = score
                updateHighScoreUI() // Update UI to reflect the change in high score
                saveHighScore() // Save the new high score
            }
        }
    }

    private fun loseAnimation() {
        binding.apply {
            score = 0
            tvScore.text = "0"
            disableButtons()
            val drawableLose = ActivityCompat.getDrawable(this@MainActivity, R.drawable.btn_lose)
            val drawableDefault =
                ActivityCompat.getDrawable(this@MainActivity, R.drawable.btn_state)
            lifecycleScope.launch {
                binding.root.forEach { view ->
                    if (view is Button) {
                        view.background = drawableLose
                        delay(200)
                        view.background = drawableDefault
                    }
                }
                delay(1000)
                startGame()
            }
        }
    }

    private fun enableButtons() {
        binding.root.forEach { view ->
            if (view is Button) {
                view.isEnabled = true
            }
        }
    }

    private fun disableButtons() {
        binding.root.forEach { view ->
            if (view is Button) {
                view.isEnabled = false
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            userAnswer += when (it.id) {
                R.id.panel1 -> "1"
                R.id.panel2 -> "2"
                R.id.panel3 -> "3"
                R.id.panel4 -> "4"
                else -> ""
            }

            if (userAnswer == result) {

                Toast.makeText(this@MainActivity, "Y O U  W I N ", Toast.LENGTH_SHORT).show()
                score++
                binding.tvScore.text = score.toString()
                startGame()

            } else if (userAnswer.length >= result.length) {
                loseAnimation()
            }
        }
    }
}
