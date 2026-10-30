package com.gastronomy.coffeetrack

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class MenuItem(val name: String, val category: String)
data class Customer(val name: String, val avatar: String)

class MainActivity : AppCompatActivity() {

    private val menuList = listOf(
        MenuItem("Espresso", "Coffee"),
        MenuItem("Cappuccino", "Coffee"),
        MenuItem("Matcha Latte", "Tea & Specialty"),
        MenuItem("Turkish Coffee", "Traditional"),
        MenuItem("Croissant", "Bakery"),
        MenuItem("Berry Muffin", "Pastry")
    )

    private val customerList = listOf(
        Customer("Sarah", "👩‍💼"),
        Customer("Michael", "👨‍💻"),
        Customer("Elena", "👩‍🎨"),
        Customer("Tariq", "👨‍🎓"),
        Customer("Jessica", "👩‍⚕️")
    )

    private var currentScore = 0
    private var comboStreak = 1
    private var currentTargetItem: MenuItem = menuList[0]
    private var timer: CountDownTimer? = null

    private lateinit var tvScore: TextView
    private lateinit var tvStreak: TextView
    private lateinit var tvCustomerAvatar: TextView
    private lateinit var tvCustomerName: TextView
    private lateinit var tvOrderSpeech: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var pbPatience: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvScore = findViewById(R.id.tvScore)
        tvStreak = findViewById(R.id.tvStreak)
        tvCustomerAvatar = findViewById(R.id.tvCustomerAvatar)
        tvCustomerName = findViewById(R.id.tvCustomerName)
        tvOrderSpeech = findViewById(R.id.tvOrderSpeech)
        tvFeedback = findViewById(R.id.tvFeedback)
        pbPatience = findViewById(R.id.pbCustomerPatience)

        findViewById<Button>(R.id.btnItem1).setOnClickListener { checkOrder("Espresso") }
        findViewById<Button>(R.id.btnItem2).setOnClickListener { checkOrder("Cappuccino") }
        findViewById<Button>(R.id.btnItem3).setOnClickListener { checkOrder("Matcha Latte") }
        findViewById<Button>(R.id.btnItem4).setOnClickListener { checkOrder("Turkish Coffee") }
        findViewById<Button>(R.id.btnItem5).setOnClickListener { checkOrder("Croissant") }
        findViewById<Button>(R.id.btnItem6).setOnClickListener { checkOrder("Berry Muffin") }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            currentScore = 0
            comboStreak = 1
            tvScore.text = "Score: 0"
            tvStreak.text = "Streak: x1"
            spawnNewCustomer()
        }

        spawnNewCustomer()
    }

    private fun spawnNewCustomer() {
        timer?.cancel()

        val customer = customerList.random()
        currentTargetItem = menuList.random()

        tvCustomerAvatar.text = customer.avatar
        tvCustomerName.text = "Customer: ${customer.name}"
        tvOrderSpeech.text = "\"Hello! I'd love a ${currentTargetItem.name}, please!\""
        tvFeedback.text = "Craft the correct order before patience runs out!"

        pbPatience.progress = 100

        // 10-second timer per order
        timer = object : CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                pbPatience.progress = (millisUntilFinished / 100).toInt()
            }

            override fun onFinish() {
                comboStreak = 1
                currentScore = maxOf(0, currentScore - 5)
                tvScore.text = "Score: $currentScore"
                tvStreak.text = "Streak: x1"
                tvFeedback.text = "Customer left unhappy! (-5 pts)"
                spawnNewCustomer()
            }
        }.start()
    }

    private fun checkOrder(selectedItem: String) {
        if (selectedItem == currentTargetItem.name) {
            val earned = 10 * comboStreak
            currentScore += earned
            comboStreak += 1
            tvFeedback.text = "Perfect! Served $selectedItem (+$earned pts)"
        } else {
            comboStreak = 1
            currentScore = maxOf(0, currentScore - 5)
            tvFeedback.text = "Wrong order! Customer wanted ${currentTargetItem.name} (-5 pts)"
        }

        tvScore.text = "Score: $currentScore"
        tvStreak.text = "Streak: x$comboStreak"
        spawnNewCustomer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
