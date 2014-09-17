package com.zooth.jt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.zooth.jt.*;

public class OpeningCutscene
{
  SpriteBatch sb;
  OpeningFight game;
  OpeningFight2 game2;
  String[] intro = 
  {
    "You have to fight, click to select a character and right click to give them actions!"
  };
  int introIdx = 0;
  String[] afterFight = 
  {
    "Good job!",
    "You won the fight!",
    "Now, you must fight again!"
  };
  int afterFightIdx = -1;
  String[] afterFight2 = 
  {
    "Good job, again!",
    "You won another fight!",
    "Go now and rest, YOU'VE earned it!"
  };
  int afterFight2Idx = -1;
  
  public void create()
  {
    sb = new SpriteBatch();
    game = new OpeningFight();
    game.create();
    game.setController(this);
    game2 = new OpeningFight2();
    game2.create();
    game2.setController(this);
  }
  public void endedFight()
  {
    // this starts the after fight dialogue
    // if we haven't seen it
    if (afterFightIdx < 0)
      afterFightIdx = 0;
    else
      afterFight2Idx = 0;
  }
  public void lostFight()
  {
    // we lost the fight
    // figure out which one it was, and 
    // display the previous dialogue again
    if (afterFightIdx < 0)
    {
      introIdx = 0;
      game.startedGame = false;
    }else
    {
      afterFightIdx = 2;
      game2.startedGame = false;
    }
  }
  public void render()
  {
    if (introIdx < intro.length)
    {
      Gdx.gl.glClearColor(1, 1, 1, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      sb.begin();
      drawMiddleText(sb, intro[introIdx]);
      sb.end();
      if (Gdx.input.justTouched())
        introIdx++;
    }else
    if (afterFightIdx >= 0 && afterFightIdx < afterFight.length)
    {
      Gdx.gl.glClearColor(1, 1, 1, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      sb.begin();
      drawMiddleText(sb, afterFight[afterFightIdx]);
      sb.end();
      if (Gdx.input.justTouched())
        afterFightIdx++;
    }else
    if (afterFight2Idx >= 0 && afterFight2Idx < afterFight2.length)
    {
      Gdx.gl.glClearColor(1, 1, 1, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      sb.begin();
      drawMiddleText(sb, afterFight2[afterFight2Idx]);
      sb.end();
      if (Gdx.input.justTouched() && afterFight2Idx < afterFight2.length-1)
        afterFight2Idx++;
    }else
    if (afterFightIdx < 0)
    {
      if (!game.startedGame)
        game.startGame();
      game.render();
    }else
    {
      if (!game2.startedGame)
        game2.startGame();
      game2.render();
    }
  }
  public void drawMiddleText(SpriteBatch sb, String str)
  {
    BitmapFont.TextBounds tb = JTactics.assets.font.getWrappedBounds(str, Gdx.graphics.getWidth());
    float midX = Gdx.graphics.getWidth()/2f;
    float midY = Gdx.graphics.getHeight()/2f;
    float pad = Gdx.graphics.getWidth()*.29f;
    sb.setColor(.4f, .5f, .9f, .9f);
    sb.draw(JTactics.assets.glow, midX-tb.width/2f-pad/2f, midY-tb.height/2f-pad/2f, tb.width+pad, tb.height+pad);
    JTactics.assets.font.setColor(1f, 1f, 1, 1);
    JTactics.assets.font.drawWrapped(sb, str, midX-tb.width/2f, midY+tb.height/2f, tb.width, BitmapFont.HAlignment.CENTER);
  }
}