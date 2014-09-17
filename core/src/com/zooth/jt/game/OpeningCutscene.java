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
import com.zooth.jt.cinematics.*;

public class OpeningCutscene
{
  SpriteBatch sb;
  float time;
  JTGame currGame;
  OpeningFight game;
  OpeningFight2 game2;
  public Track lastTrack = null;
  public Track currTrack = null;
  public Track introTrack;
  public Track afterFightTrack;
  public Track afterFight2Track;
  public Track finalTrack;
  
  public OpeningCutscene()
  {
    introTrack = new Track();
    introTrack.events.add(new TrackEvent("You've been surrounded by dire frogs in the forest!"));
    introTrack.events.add(new TrackEvent("Click to select a character and right click to give them actions."));
    introTrack.events.add(new TrackEvent("You can select an adjacent hexagon to move there."));
    introTrack.events.add(new TrackEvent("Or select an enemy to attack it."));
    introTrack.events.add(new TrackEvent("Each character gets 3 actions per turn, shown below them by white hexagons."));
    introTrack.events.add(new TrackEvent("You have to fight, click to select a character and right click to give them actions!"));
    currTrack = introTrack;
    afterFightTrack = new Track();
    afterFightTrack.events.add(new TrackEvent("Great job, you beat the first wave!"));
    afterFightTrack.events.add(new TrackEvent("Oh no! Here comes a second!"));
    afterFight2Track = new Track();
    afterFight2Track.events.add(new TrackEvent("Great job, you beat the second wave!"));
    afterFight2Track.events.add(new TrackEvent("Go now and rest, YOU'VE earned it!"));
    finalTrack = new Track();
    finalTrack.events.add(new TrackEvent("To be continued..."));
  }
  
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
    // after we win a fight,
    // play the next dialogue
    // this should lead into the
    // next fight
    if (currGame == game)
    {
      currGame = null;
      currTrack = afterFightTrack;
    }else
    if (currGame == game2)
    {
      currGame = null;
      currTrack = afterFight2Track;
    }
  }
  public void lostFight()
  {
    // we lost the fight
    // display the previous dialogue again
    // doing this should automatically lead into the correct fight
    // and provide some backstory
    currTrack = lastTrack;
    currTrack.reset();
    currGame.startedGame = false;
  }
  public void nextPart()
  {
    if (currTrack == introTrack)
    {
      lastTrack = currTrack;
      currTrack = null;
      currGame = game;
    }else
    if (currTrack == afterFightTrack)
    {
      lastTrack = currTrack;
      currTrack = null;
      currGame = game2;
    }else
    if (currTrack == afterFight2Track)
    {
      lastTrack = currTrack;
      currTrack = finalTrack;
    }
  }
  public void render()
  {
    time += Gdx.graphics.getDeltaTime();
    if (currTrack != null)
    {
      Gdx.gl.glClearColor(1, 1, 1, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      sb.begin();
      sb.setColor(1, 1, 1, 1);
      sb.draw(JTactics.assets.openingBG, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      drawCharacterText(sb, JTactics.assets.chatBox, currTrack.events.get(currTrack.currIdx).dialogue);
      sb.end();
      if (Gdx.input.justTouched())
      {
        if (currTrack != finalTrack)
          currTrack.currIdx++;
        if (currTrack.currIdx >= currTrack.events.size())
          nextPart();
      }
    }else
    if (currGame != null)
    {
      if (!currGame.startedGame)
        currGame.startGame();
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      sb.begin();
      currGame.render(sb);
      sb.end();
    }
  }
  public void drawCharacterText(SpriteBatch sb, Texture tex, String str)
  {
    float width = Gdx.graphics.getWidth()*.8f;
    float textWidth = Gdx.graphics.getWidth()*.6f;
    float height = Gdx.graphics.getHeight()*.2f;
    BitmapFont.TextBounds tb = JTactics.assets.font.getWrappedBounds(str, textWidth);
    float midX = Gdx.graphics.getWidth()/2f;
    float pad = width*.1f;
    float midY = height/2f+pad/2f;
    sb.setColor(.4f, .5f, .9f, .7f);
    sb.draw(JTactics.assets.chatBox, midX-width/2f-pad/2f, midY-tb.height/2f-pad/2f, width+pad, tb.height+pad);
    float mouseWidth = width*.15f;
    float mouseHeight = height;
    sb.setColor(1, 1, 1, .9f);
    if (currTrack != finalTrack)
      sb.draw(
        ((int)(time*2.5f))%2 == 0?JTactics.assets.mouseNoClick:JTactics.assets.mouseLeftClick,
        midX+width/2f-pad-mouseWidth/2f, midY-mouseHeight/2f, mouseWidth, mouseHeight);
    JTactics.assets.font.setColor(1f, 1f, 1, .9f);
    JTactics.assets.font.drawWrapped(sb, str, midX-(width-textWidth)/2f-tb.width/2f, midY+tb.height/2f, tb.width, BitmapFont.HAlignment.CENTER);
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