package com.zooth.jt;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;

public class Guy
{
  JTTile tile;
  JTTile destTile;
  JTGame game;
  boolean inTransit = false;
  float time;
  boolean selected;
  boolean takingTurn;
  JTPlayer controller;
  int ap = 0;
  int hp = 0;
  static int NOTHING = 0;
  static int ATTACK = 1;
  static int MOVE = 2;
  int actionType;
  Guy attackTarget;
  // QoL action queueing
  List<Object> actionQueue;

  public Guy()
  {
    actionQueue = new ArrayList<Object>();
    tile = new JTTile(0, 0, 0);
  }

  public void setGame(JTGame g)
  {
    game = g;
  }

  public List<JTTile> getAdjTiles(int num)
  {
    List<JTTile> tiles = new ArrayList<JTTile>();
    if (num == 1)
    {
      
      //tiles.add(
    }
    return tiles;
  }
  public boolean canMove(JTTile t)
  {
    if (!inTransit && ap > 0)
    {
      int xDiff = t.x-tile.x;
      int yDiff = t.y-tile.y;
      int offDiff = t.off-tile.off;
      if (t.off == tile.off)
      {

        // directly up or down
        if (xDiff == 0 && (int)Math.abs(yDiff) == 1)
          return true;
      }else
      if (xDiff == 0)
      {
        // right and up or down
        if (yDiff == -offDiff || yDiff == 0)
          return true;
      }else
      if (xDiff == -offDiff)
      {
        // left and up or down
        if (yDiff == -offDiff || yDiff == 0)
          return true;
      }
    }
    return false;
  }
  public void attack(Guy g)
  {
    if (!inTransit)
    {
      if (canMove(g.tile) && game.checkIsMovable(g.tile))
      {
        destTile = g.tile;
        attackTarget = g;
        actionType = ATTACK;
        time = 0;
        useAp(1);
        inTransit = true;
      }else
      {
        // we couldn't do a move in the chain, clear the rest
        actionQueue.clear();
      }
    }else
    {
      if (ap-actionQueue.size() > 0)
      {
        actionQueue.add(g);
      }
    }
  }
  public void moveTo(JTTile t)
  {
    if (!inTransit)
    {
      if (!t.check(tile) && canMove(t) && game.checkIsMovable(t))
      {
        destTile = t;
        inTransit = true;
        actionType = MOVE;
        time = 0;
        useAp(1);
      }else
      {
        // we couldn't do a move in the chain, clear the rest
        actionQueue.clear();
      }
    }else
    {
      if (ap-actionQueue.size() > 0)
      {
        actionQueue.add(t);
      }
    }
  }
  public void useAp(int num)
  {
    ap-=num;
  }
  public void getDamaged(Guy g, int amnt)
  {
    hp -= amnt;
  }
  public void reset()
  {
    hp = 3;
  }

  public int movesLeft()
  {
    return ap;
  }
  public void newTurn()
  {
    // if we have enough hp, give full ap
    if (hp > 1)
      ap = 3;
    else // if we're hurt a lot, give -1 ap
    if (hp > 0)
      ap = 2;
    else // we're dead:
      ap = 0;
  }

  public void setController(JTPlayer p)
  {
    controller = p;
  }

  public boolean checkSelect(JTTile t)
  {
    return (tile.check(t));
  }
  public boolean okayForTurnEnd()
  {
    // this returns true if the character would
    // be fine with ending it's turn
    return (!inTransit && (hp <= 0 || ap == 0));
  }
  public boolean isDead()
  {
    return hp <= 0;
  }

  public void setSelected(boolean b)
  {
    selected = b;
  }
  public void setTurn(boolean b)
  {
    takingTurn = b;
  }
  
  public void step(float dt)
  {
    if (inTransit)
    {
      time += dt;
      if (time > 1)
      {
        // perform the action
        if (actionType == MOVE)
        {
          tile = destTile;
          inTransit = false;
          destTile = null;
        }else
        if (actionType == ATTACK)
        {
          inTransit = false;
          destTile = null;
          attackTarget.getDamaged(this, 1);
        }
        // if there's more in the queue, do the next one
        if (actionQueue.size() > 0)
        {
          Object nextAction = actionQueue.remove(0);
          if (nextAction instanceof Guy)
          {
            attack((Guy)nextAction);
          }else
          if (nextAction instanceof JTTile)
          {
            moveTo((JTTile)nextAction);
          }
        }
      }
    }
  }

  public Texture getTexture()
  {
    return JTactics.assets.mage;
  }

  public void draw(SpriteBatch sb, Camera cam)
  {
    Vector2 pos = null;
    if (inTransit && destTile != null)
    {
      Vector2 src = game.field.getPos(tile);
      Vector2 dst = game.field.getPos(destTile);
      if (actionType == MOVE)
      {
        pos = dst.cpy().sub(src).scl(time).add(src);
      }else
      if (actionType == ATTACK)
      {
        float closeness = .75f;
        float lenDist = time<.5f?
          closeness*4*time*time:
          closeness*4*(time-1f)*(time-1f);
        pos = dst.cpy().sub(src).scl(lenDist).add(src);
      }
    }else
      pos = game.field.getPos(tile);
    float width = game.field.height*.7f;
    float height = game.field.width*.6f;
    if (selected)
    {
      sb.setColor(1, 1, 1, 1);
      float added = .8f;
      drawTex(sb, JTactics.assets.glow, width*(1f+added), height*(1f+added), height*added/2f, 0, pos);
    }
    
    sb.setColor(1, 1, 1, 1);
    drawTex(sb, getTexture(), width, height, 0, 0, pos);
    if (takingTurn)
    {
      // draw the ap
      float added = -.7f;
      for (int i = -1; i < 2; ++i)
      {
        if (i+1 < ap)
          sb.setColor(1, 1, 1, 1);
        else
          sb.setColor(0, 0, 0, 1);
        drawTex(sb, JTactics.assets.hex, width*(1f+added), width*(1f+added), height*(1f+added)/2, width*.4f*i, pos);
      }
    }
    // draw health bar:
    // back of it
    sb.setColor(.4f, 0, 0, 1);
    sb.draw(JTactics.assets.white,
      pos.x+game.field.width*.2f,
      pos.y+game.field.height/2+height+game.field.height*.05f,
      game.field.width*.6f, game.field.height*.15f);
    // filled part:
    sb.setColor(1, 0, 0, 1);
    sb.draw(JTactics.assets.white,
      pos.x+game.field.width*.2f,
      pos.y+game.field.height/2+height+game.field.height*.05f,
      game.field.width*.6f*hp/3f, game.field.height*.15f);
  }

  public void drawTex(SpriteBatch sb, Texture t, float width, float height, float offsetY, float offsetX, Vector2 pos)
  {
    sb.draw(t, pos.x+game.field.width/2-width/2+offsetX, pos.y+game.field.height/2-offsetY, width, height);

  }
}
