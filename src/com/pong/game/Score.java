package com.pong.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Score {

  public int score1;
  public int score2;
  public int x;
  public int y;

  public Score(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int P1Score(int playerScore) {
    this.score1 += playerScore;
    return score1;
  }
  //
  public int P2Score(int bot) {
    this.score2 += bot;
    return score2;
  }
  //
  public String scoreOneString() {
    if(score1 > 0) {
      return String.valueOf(score1);
    } else {
      return "0";
    }
  }
  //
  public String scoreTwoString() {
    if(score2 > 0) {
      return String.valueOf(score2);
    } else {
      return "0";
    }
  }
  //
  public void draw1(Graphics g) {
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 100));
    g.drawString(scoreOneString(), x, y);
  }
  public void draw2(Graphics g) {
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 100));
    g.drawString(scoreTwoString(), x, y);
  }
}
