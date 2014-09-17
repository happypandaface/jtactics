package com.zooth.jt;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.zooth.jt.game.*;
import java.util.*;

public class Guy
{
  public JTTile tile;
  public JTTile destTile;
  public JTGame game;
  public boolean inTransit = false;
  public float time;
  public boolean selected;
  public boolean takingTurn;
  public JTPlayer controller;
  public int ap = 0;
  public int hp = 0;
  public static int NOTHING = 0;
  public static int ATTACK = 1;
  public static int MOVE = 2;
  public static int HEAL = 3;
  public static int MAGIC_ATTACK = 4;
  public static int CAST_FB = 5;
  public int actionType;
  public Guy attackTarget;
  public List<JTTile> destTiles;
  // QoL action queueing
  public List<Object> actionQueue;
  public int maxAp = 3;
  public int maxHp = 3;

  public Guy()
  {
    actionQueue = new ArrayList<Object>();
    tile = new JTTile(0, 0, 0);
  }

  public void setGame(JTGame g)
  {
    game = g;
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
  public boolean inRing(JTTile t, int num)
  {
    // check if the target is in the given ring:
    List<JTTile> ring = getAdjTiles(num);
    boolean inRing = false;
    for (int i = 0; i < ring.size(); ++i)
    {
      JTTile t2 = ring.get(i);
      if (t2.check(t))
      {
        inRing = true;
        break;
      }
    }
    return inRing;
  }
  public boolean canHeal()
  {
    return false;
  }
  public boolean canRange()
  {
    return false;
  }
  public boolean heal(Guy g)
  {
    if (!inTransit && canHeal() && ap > 0 && g.hp < maxHp && g.hp > 0)
    {
      if ((inRing(g.tile, 1) || inRing(g.tile, 2)) && game.checkIsMovable(g.tile) && hasAp(2))
      {
        destTile = g.tile;
        attackTarget = g;
        actionType = HEAL;
        time = 0;
        useAp(2);
        inTransit = true;
        return true;
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
    return false;
  }
  public boolean attack(Guy g)
  {
    if (ap > 0)
    {
      if (!inTransit)// make sure we're not already taking an action
      {
        // make sure the target is traversable
        if (game.checkIsMovable(g.tile))
        {
          // if it's in the inner ring it's a basic attack
          if (inRing(g.tile, 1))
          {
            destTile = g.tile;
            attackTarget = g;
            actionType = ATTACK;
            time = 0;
            useAp(1);
            inTransit = true;
            return true;
          }else// if it's in the outer ring, it's a ranged attack
          if (inRing(g.tile, 2) && canRange() && hasAp(2))
          {
            destTile = g.tile;
            attackTarget = g;
            actionType = MAGIC_ATTACK;
            time = 0;
            useAp(2);
            inTransit = true;
            return true;
          }
        }
        if (!inTransit)
        {
          // we're still not in an action which means
          // we couldn't do a move in the chain, clear the rest
          // so that we don't act irregularly
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
    return false;
  }
  public boolean moveTo(JTTile t)
  {
    if (!inTransit)
    {
      if (!t.check(tile) && canMove(t) && game.checkIsMovable(t) && game.guyAt(t) == null)
      {
        destTile = t;
        inTransit = true;
        actionType = MOVE;
        time = 0;
        useAp(1);
        return true;
      }else
      {
        // we couldn't do a move in the chain, clear the rest
        actionQueue.clear();
      }
    }else
    {
      // this should check and return true if possible
      if (ap-actionQueue.size() > 0)
      {
        actionQueue.add(t);
      }
    }
    return false;
  }
  public void useAp(int num)
  {
    ap-=num;
  }
  // this is also currently used with negative numbers to heal
  public void getDamaged(Guy g, int amnt)
  {
    hp -= amnt;
  }
  // called when the game starts
  public void reset()
  {
    hp = maxHp;
    ap = 0;
  }

  public int movesLeft()
  {
    return ap;
  }
  public boolean hasAp(int num)
  {
    return ap >= num;
  }
  public void newTurn()
  {
    // if we have enough hp, give full ap
    if (hp > 1)
      ap = maxAp;
    else // if we're hurt a lot, give -1 ap
    if (hp > 0)
      ap = maxAp-1;
    else // we're dead:
      ap = 0;
  }

  public void setController(JTPlayer p)
  {
    controller = p;
  }

  public boolean checkSelect(JTTile t)
  {
    return checkAt(t);
  }
  public boolean checkAt(JTTile t)
  {
    return (
      // if we're not moving, or we're attacking, use the current tile;
      ((!inTransit || actionType != MOVE) && tile.check(t)) ||
      // if we're moving, use the destination
      (inTransit && actionType == MOVE && destTile.check(t)));
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
        if (actionType == CAST_FB)
        {
          inTransit = false;
          destTile = null;
        }else
        if (actionType == ATTACK || actionType == MAGIC_ATTACK)
        {
          inTransit = false;
          destTile = null;
          attackTarget.getDamaged(this, 1);
        }else
        if (actionType == HEAL)
        {
          inTransit = false;
          destTile = null;
          attackTarget.getDamaged(this, -1);
        }
        // if there's more in the queue, do the next one
        if (actionQueue.size() > 0)
        {
          Object nextAction = actionQueue.remove(0);
          if (nextAction instanceof Guy)
          {
            Guy g = (Guy)nextAction;
            if (g.controller == controller)
              heal(g);
            else
              attack(g);
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
  
  public void castFireBall(JTTile t)
  {
    if (!inTransit && hp > 0 && (inRing(t, 1) || inRing(t, 2)) && hasAp(1))
    {
      //destTile = t;
      actionType = CAST_FB;
      time = 0;
      useAp(1);
      inTransit = true;
      int dir = tile.getDirection(t);
      List<JTTile> tilesInDir = tile.direction(dir, 2);
      destTile = tilesInDir.get(tilesInDir.size()-1);
    }
  }
  
  boolean pickingLocation = false;
  public boolean checkGUIClick(Vector2 click)
  {
    // see if we're pressing a button
    float width = 50;
    float height = 50;
    if (click.x > Gdx.graphics.getWidth()-width && click.y < height)
    {
      pickingLocation = !pickingLocation;
      return true;
    }
    // we're selecting a location for a button we already pressed
    if (pickingLocation)
    {
      castFireBall(game.field.selectedTile);
      pickingLocation = false;
      return true;
    }
    return false;
  }
  public void drawGUI(SpriteBatch sb, Camera cam)
  {
    float width = 50;
    float height = 50;
    if (pickingLocation)
      sb.setColor(1, 1, 1, 1);
    else
      sb.setColor(.5f, .5f, .5f, 1);
    sb.draw(JTactics.assets.fireball, Gdx.graphics.getWidth()-width, 0, width, height);
  }

  public void draw(SpriteBatch sb, Camera cam)
  {
    Vector2 pos = null;
    if (inTransit && destTile != null)
    {
      Vector2 src = game.field.getPos(tile);
      Vector2 dst = game.field.getPos(destTile);
      if (actionType == HEAL)
      {
        pos = src.cpy();
      }else
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
      }else
      if (actionType == MAGIC_ATTACK)
      {
        pos = src.cpy();
        Fireball fb = new Fireball();
        float delay = .3f;
        fb.pos = dst.cpy().sub(src).scl(time<delay?0:(float)Math.sin((time-delay)/(1-delay)*Math.PI/2f)).add(src);
        fb.tail = dst.cpy().sub(src).scl(time<delay||time>(1-delay)?(delay)*(float)Math.sin(time*Math.PI):(delay)*(float)Math.sin((delay)*Math.PI));
        game.addProj(fb);
      }else
      if (actionType == CAST_FB)
      {
        pos = src.cpy();
        Fireball fb = new Fireball();
        float delay = .3f;
        fb.pos = dst.cpy().sub(src).scl(time<delay?0:(float)Math.sin((time-delay)/(1-delay)*Math.PI/2f)).add(src);
        fb.tail = dst.cpy().sub(src).scl(time<delay||time>(1-delay)?(delay)*(float)Math.sin(time*Math.PI):(delay)*(float)Math.sin((delay)*Math.PI));
        game.addProj(fb);
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
    if (!takingTurn)
      sb.setColor(.6f, .6f, .6f, 1);
    drawTex(sb, getTexture(), width, height, 0, 0, pos);
    if (actionType == HEAL && inTransit)
    {
      sb.setColor(1, 1, 1, 1);
      float size = (float)(Math.sin(time*Math.PI)*1.5f+1f);
      drawTex(sb, JTactics.assets.glow, width*size, height*size, 0, 0, pos);
    }
    if (takingTurn)
    {
      // draw the ap
      float added = -.7f;
      for (int i = 0; i < maxAp; ++i)
      {
        if (i < ap)
          sb.setColor(1, 1, 1, 1);
        else
          sb.setColor(0, 0, 0, 1);
        float sub = ((float)(maxAp-1)/2f);// graphical adj based on max ap to center the hexagons
        drawTex(sb, JTactics.assets.fullHex, width*(1f+added), width*(1f+added), height*(1f+added)/2, width*.4f*(i-sub), pos);
      }
    }
    // draw health bar:
    float barWidth = game.field.width*.6f;
    float barHeight = game.field.height*.15f;
    // back of it
    sb.setColor(.4f, 0, 0, 1);
    sb.draw(JTactics.assets.white,
      pos.x+game.field.width*.2f,
      pos.y+game.field.height/2+height+game.field.height*.05f,
      barWidth, barHeight);
    // filled part:
    sb.setColor(1, 0, 0, 1);
    sb.draw(JTactics.assets.white,
      pos.x+game.field.width*.2f,
      pos.y+game.field.height/2+height+game.field.height*.05f,
      barWidth*(float)hp/(float)maxHp, barHeight);
    // breaks each bar (ticks)
    float tickW = barWidth*.07f;
    for (int i = 1; i < maxHp; ++i)
    {
      sb.setColor(.4f, 0, 0, 1);
      sb.draw(JTactics.assets.white,
        pos.x+game.field.width*.2f+barWidth*((float)(i)/(float)(maxHp))-tickW/2f,
        pos.y+game.field.height/2+height+game.field.height*.02f,
        tickW, barHeight*1.4f);
    }
  }

  public void drawTex(SpriteBatch sb, Texture t, float width, float height, float offsetY, float offsetX, Vector2 pos)
  {
    sb.draw(t, pos.x+game.field.width/2-width/2+offsetX, pos.y+game.field.height/2-offsetY, width, height);

  }

 
  public List<JTTile> getAdjTiles(int num)
  {
    return tile.getAdjTiles(num);
  }

}
