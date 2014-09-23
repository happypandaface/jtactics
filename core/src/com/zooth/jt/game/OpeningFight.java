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
import java.util.*;
import com.zooth.jt.guys.*;

public class OpeningFight extends JTGame
{
  OpeningCutscene os;
  public void setController(OpeningCutscene os)
  {
    this.os = os;
  }
  public String getEndMessage()
  {
    String endStr = "A Glitch Occurred!";//default string
    // this was the old text:
    // endStr = "Player "+(winner+1)+" wins!";
    // it makes more sense if you know what player you
    // are (which you don't in campaign, which is the
    // only mode right now)

    if (winner == JTGame.NO_WINNER)
    {
      endStr = "Everyone loses!";
    }else
    if (winner == 0)
    {
      endStr = "You win!"; 
    }else
    {
      endStr = "You lose!"; 
    }
    return endStr;
  }
  @Override
  public void setupInPlay()
  {
    addHex(0, 2, 5, 1);
    addHex(0, 3, 5, 1);
    addHex(0, 4, 5, 1);
  }
  public JTField getField()
  {
    return new JTField()
    { 
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
  @Override
  public void render(SpriteBatch sb)
  {
    super.render(sb);
    if (turnsTaken%2 == 0)
    {
      float maxTime = 2.2f;
      float currTime = os.time%maxTime;
      float offSetAmnt = (currTime<1f?1-currTime:0)*40f;
      for (int i = 0; i < objs.size(); ++i)
      {
        Guy obj = objs.get(i);
        // make sure the guy is on our team
        if (obj.controller == players.get(0))
        {
          float offSetAmntX = 0;
          float offSetAmntY = 0;
          Vector2 pos = null;
          int drawRightOrLeft = 0;// 1 is left, 2 is right
          // make sure there's no current guy or he's out of ap,
          // and this one has ap.
          if ((selectedGuy == null || !selectedGuy.hasAp(1)) && obj.hasAp(1))
          {
            offSetAmntX = offSetAmnt;
            offSetAmntY = -offSetAmnt;
            pos = field.getPos(obj.tile);
            drawRightOrLeft = 1;
          }else
          if (obj == selectedGuy && selectedGuy.hasAp(1) && !obj.inTransit)
          {// check if this is the selected guy, in which case do basic AI to see what to do.
            // first see if we can fireball
            boolean chosenAdvice = false;
            List<JTTile> outerTiles = obj.getAdjTiles(2);
            List<JTTile> innerTiles = obj.getAdjTiles(1);
            for (int c = 0; c < outerTiles.size()+innerTiles.size(); ++c)
            {
              JTTile t = null;
              if (c < outerTiles.size())
                t = outerTiles.get(c);
              else
                t = innerTiles.get(c-outerTiles.size());
              Guy g = this.guyAt(t);
              if (g != null && !g.isDead())
              {
                if (!obj.inTransit && obj.canRange() && g.controller != players.get(0))
                {
                  pos = field.getPos(g.tile);
                  
                  offSetAmntX = -offSetAmnt;
                  offSetAmntY = -offSetAmnt;
                  drawRightOrLeft = 2;// right click
                  chosenAdvice = true;
                }else
                if (!obj.inTransit && obj.canHeal() && g.controller == players.get(0) && g.hp < g.maxHp)
                {
                  pos = field.getPos(g.tile);
                  
                  offSetAmntX = -offSetAmnt;
                  offSetAmntY = -offSetAmnt;
                  drawRightOrLeft = 2;// right click
                  chosenAdvice = true;
                }
              }
            }
            if (!chosenAdvice)
            {
              List<JTTile> path = null;
              for (int c = 0; c < objs.size(); ++c)
              {
                Guy currObj = objs.get(c);
                if (currObj.controller != obj.controller && !currObj.isDead())
                {
                  List<JTTile> currPath = currObj.tile.getPath(obj.tile, this);
                  if (path == null || currPath.size() < path.size())
                    path = currPath;
                }
              }
              // because the first node in the path, is the
              // tile we're currently on, we use the second
              // on
              if (path != null && path.size() > 1)
              {
                JTTile tarTile = path.get(1);
                pos = field.getPos(tarTile);
                
                offSetAmntX = -offSetAmnt;
                offSetAmntY = -offSetAmnt;
                drawRightOrLeft = 2;// right click
              }
            }
          }
          // draw the mouse tutorial only if we were told
          // above, and there's no winner
          if (drawRightOrLeft > 0 && winner == -1)
          {
            // this will do good graphics for either
            sb.setColor(1,1,1,currTime<.5f?currTime*2f:currTime<maxTime-.5f?1:-2f*(currTime-maxTime));
            float mouseWidth = Gdx.graphics.getWidth()*.1f;
            float mouseHeight = Gdx.graphics.getHeight()*.2f;
            sb.draw(
              currTime<maxTime-1f||currTime>maxTime-.5f?JTactics.assets.mouseNoClick:drawRightOrLeft==1?JTactics.assets.mouseLeftClick:JTactics.assets.mouseRightClick,
              pos.x+offSetAmntX-mouseWidth/2f+field.width/2f, pos.y+offSetAmntY-mouseHeight*.85f, mouseWidth, mouseHeight);
            // we only want to help the player move one person at a time, so break
            // I happen to know all my players are very slow
            break;
          }
        }
      }
    }
  }
}
