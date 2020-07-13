package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.done_button).setOnClickListener {
      addNickName(it)
    }

    findViewById<TextView>(R.id.nickname_text).setOnClickListener {
      updateNickName(it)
    }
  }



  private fun addNickName(view: View) {

    binding.nicknameText.text = binding.nicknameEdit.text
    binding.nicknameEdit.visibility = View.GONE
    view.visibility = View.GONE
    binding.nicknameText.visibility = View.VISIBLE

    // Hide the keyboard.
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }

  private fun updateNickName(view: View) {
    val editText = binding.nicknameEdit
    val doneButton = binding.doneButton
    editText.visibility = View.VISIBLE
    doneButton.visibility = View.VISIBLE
    view.visibility = View.GONE

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, 0)
  }
}
