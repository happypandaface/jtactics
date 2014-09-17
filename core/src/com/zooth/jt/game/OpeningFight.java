package com.zooth.jt.game;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.zooth.jt.*;

public class OpeningFight extends JTGame
{
  OpeningCutscene os;
  public void setController(OpeningCutscene os)
  {
    this.os = os;
  }
  public JTField getField()
  {
    return new JTField()
    { 
      // for overriding
      public void drawBackground(SpriteBatch sb, Camera cam)
      {
        sb.setColor(1, 1, 1, .5f);
        sb.draw(JTactics.assets.darkForest, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      }
    };
  }
  @Override
  public void setupObjs()
  {
    {
      Guy g = new BlackMage();
      g.setController(players.get(0));
      g.tile = new JTTile(1, 1, 5);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(players.get(0));
      g.tile = new JTTile(1, 1, 4);
      addObj(g);
    }
    {
      Guy g = new Frog();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 4, 6);
      addObj(g);
    }
    {
      Guy g = new Frog();
      g.setController(players.get(1));
      g.tile = new JTTile(0, 4, 5);
      addObj(g);
    }
  }
  @Override
  public void endGame()
  {
    // if we're the winner, end the fight, otherwise
    // start over
    if (winner == 0)
      os.endedFight();
    else
    {
      os.lostFight();
    }
  }
}