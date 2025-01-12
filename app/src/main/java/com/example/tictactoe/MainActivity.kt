package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : ComponentActivity() {

    private lateinit var gridButtons: Array<Array<Button>>
    private lateinit var statusTextView: TextView
    private lateinit var playAgainButton: Button
    private var currentPlayer = 'X'
    private var board = Array(3) { CharArray(3) { ' ' } }

    // On create override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.playerStatus)
        playAgainButton = findViewById(R.id.btnPlayAgain)
        gridButtons = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("btn${row * 3 + col + 1}", "id", packageName)
                findViewById<Button>(buttonId).apply {
                    setOnClickListener { onCellClicked(this, row, col) }
                }
            }
        }

        playAgainButton.setOnClickListener { resetGame() }
    }

    //On cell click
    private fun onCellClicked(button: Button, row: Int, col: Int) {
        if (board[row][col] != ' ' || isGameOver()) return

        board[row][col] = currentPlayer
        button.text = currentPlayer.toString()

        // Change the text color based on the player
        val color = if (currentPlayer == 'X') {
            ContextCompat.getColor(this, R.color.colorX)  // Set X color to red
        } else {
            ContextCompat.getColor(this, R.color.colorO)  // Set O color to yellow
        }
        button.setTextColor(color)

        // Check conditions
        if (checkWinner(row, col)) {
            statusTextView.text = getString(R.string.player_wins, currentPlayer)
            playAgainButton.visibility = View.VISIBLE
        } else if (isBoardFull()) {
            statusTextView.text = getString(R.string.draw)
            playAgainButton.visibility = View.VISIBLE
        } else {
            currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
            statusTextView.text = getString(if (currentPlayer == 'X') R.string.player_x_turn else R.string.player_o_turn)
        }
    }

    // Check winner
    private fun checkWinner(row: Int, col: Int): Boolean {
        // Check row, column, and diagonals
        return (board[row].all { it == currentPlayer } ||
                board.all { it[col] == currentPlayer } ||
                (row == col && board.indices.all { i -> board[i][i] == currentPlayer }) ||
                (row + col == 2 && board.indices.all { i -> board[i][2 - i] == currentPlayer }))
    }

    // Check if the board is full
    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it != ' ' } }
    }

    // Define game state to show play again button
    private fun isGameOver(): Boolean {
        return playAgainButton.visibility == View.VISIBLE
    }

    // Reset game upon ending
    private fun resetGame() {
        board = Array(3) { CharArray(3) { ' ' } }
        currentPlayer = 'X'
        statusTextView.text = getString(R.string.player_x_turn)
        playAgainButton.visibility = View.GONE

        for (row in gridButtons) {
            for (button in row) {
                button.text = ""
            }
        }
    }
}