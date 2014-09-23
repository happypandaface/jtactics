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
import com.zooth.jt.objs.*;
import com.zooth.jt.actions.*;

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
  public int armor= 0;
  public static int NOTHING = 0;
  public static int ATTACK = 1;
  public static int MOVE = 2;
  public static int HEAL = 3;
  public static int MAGIC_ATTACK = 4;
  public static int CAST_FB = 5;
  public static int DOING_ACTION = 6;
  public int actionType;
  public Guy attackTarget;
  public List<JTTile> destTiles;
  // QoL action queueing
  public List<Object> actionQueue;
  public int maxAp = 3;
  public int maxHp = 3;
  // the actions this guy can take
  public List<Integer> possibleActions;
  // boolean to catch a tile select for actions
  public boolean pickingLocation = false;
  public Action currAction = null;

  public Guy()
  {
    possibleActions = new ArrayList<Integer>();
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
      Guy guyAt = game.guyAt(t);
      Boulder boulderAt = game.boulderAt(t);
      // check if an action was taken
      // used to clear the queue if no action is taken
      boolean actionTaken = false;
      if (!t.check(tile) && canMove(t) && boulderAt == null && game.checkIsMovable(t) && (guyAt == null || guyAt.isDead()))
      {
        destTile = t;
        inTransit = true;
        actionType = MOVE;
        time = 0;
        useAp(1);
        resetArmor();// if you move, you lose your armor
        actionTaken = true;// not used b/c of return, but
                           // may be used in newer code
        return true;
      }
      if (boulderAt != null)
      {
        // move the boulder if we can
        BoulderPush b = new BoulderPush();
        b.setGame(game);
        b.tarBoulder = boulderAt;
        b.guy = this;
        if (b.canCast(this))
        {
          currAction = b;
          b.selected(this);
          actionTaken = true;
          resetArmor();// if you move, you lose your armor
        }
      }
      if (actionTaken == false)
      {
        // we couldn't do a move in the chain, clear the rest
        actionQueue.clear();
      }
    }else
    {// happens if we were in transit
      // this should check and return true if possible (very difficult)
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
  public void healed(Guy g, int amnt)
  {
    if (maxHp-hp > amnt)
      hp += amnt;
    else
      hp = maxHp;
  }
  public void getDamaged(Guy g, int amnt)
  {
    // deal damage to our armor first
    armor -= amnt;
    // check if the damage got through our armor
    if (armor < 0)
    {
      // deal damage that got through armor
      hp += armor;
      // make sure we dont have negative armor
      armor = 0;
    }
  }
  // resets when you take an action 
  public void resetArmor()
  {
    armor = 0;
  }
  // used by ActShield to give armor
  public void getArmor(Guy g, int amnt)
  {
    if (armor == 0)
      armor = 1;
//    armor += amnt;
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
  
  // adds an action to possible
  // actions
  public Guy addAction(int act)
  {
    possibleActions.add(act);
    return this;
  }
  // allows easy addition (linking)
  public Guy setController(JTPlayer p)
  {
    controller = p;
    return this;
  }
  public Guy setTile(int off, int x, int y)
  {
    tile = new JTTile(off, x, y);
    return this;
  }

  public void removeAction(int act)
  {
    for (int i = 0; i < possibleActions.size(); ++i)
      if (possibleActions.get(i) == act)
      {
        possibleActions.remove(i);
        break;
      }
  }
  public boolean checkCanPerform(int act)
  {
    for (int i = 0; i < possibleActions.size(); ++i)
    {
      if (possibleActions.get(i) == act)
        return true;
    }
    return false;
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
        if (actionType == DOING_ACTION)
        {
          currAction.end();
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
          attackTarget.healed(this, 2);
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

  public float characterHeight()
  {
    return 1;
  }
  public Texture getTexture()
  {
    return JTactics.assets.mage;
  }

  // called by the action and 
  // sets us up for doing an action
  public void setupAction()
  {
    inTransit = true;
    time = 0;
    actionType = DOING_ACTION;
    destTile = tile.copy();
    pickingLocation = false;
  }
  // called by the action when
  // we stop doing an action
  public void shutdownAction()
  {
    inTransit = false;
    destTile = null;
  }
  
  public void castFireBall(JTTile t)
  {
    if (!inTransit && hp > 0 && (inRing(t, 1) || inRing(t, 2)))
    {
      //destTile = t;
      currAction.dirTile = t;
      currAction.startAction(this);
      /* old code
      actionType = CAST_FB;
      time = 0;
      useAp(1);
      inTransit = true;
      int dir = tile.getDirection(t);
      List<JTTile> tilesInDir = tile.direction(dir, 2);
      destTile = tilesInDir.get(tilesInDir.size()-1);
      */
    }
  }
  
  // if this returns true it means a button was pressed
  // and we shouldn't count the click as a field tile select
  // (unless the current action says we should)
  public boolean checkGUIClick(Vector2 click)
  {
    // make sure we can't click this while we're
    // already doing an action
    // because it changes the currAction
    // and can cause NullPointer and weird behaviour
    if (inTransit)
      return false;
    // see if we're pressing a button
    float width = 50;
    float height = 50;
    for (int i = 0; i < possibleActions.size(); ++i)
    {
      int act = possibleActions.get(i);
      if (click.x < width*(i+1) && click.y < height)
      {
        currAction = Action.make(act);
        if (currAction.canCast(this))
        {
          currAction.guy = this;
          currAction.selected(this);
        }else
        {
          currAction = null;
        }
        return true;
      }
    }
    // we're selecting a location for a button we already pressed
    if (pickingLocation)
    {
      castFireBall(game.field.selectedTile);
      return true;
    }
    return false;
  }
  public void drawGUI(SpriteBatch sb, Camera cam)
  {
    float width = 50;
    float height = 50;
    for (int i = 0; i < possibleActions.size(); ++i)
    {
      int act = possibleActions.get(i);
      if (pickingLocation)
        sb.setColor(1, 1, 1, 1);
      else
        sb.setColor(.5f, .5f, .5f, 1);
      sb.draw(Action.getTex(act), width*i, 0, width, height);
    }
  }

  public void draw(SpriteBatch sb, Camera cam)
  {
    Vector2 pos = null;
    // if we're doing an action,
    // get our position from that
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
      }else
      if (actionType == DOING_ACTION)
      {
        pos = currAction.step(time);
      }
    }else// if we're not doing an action, just get the position of the tile
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
    if (!takingTurn || !hasAp(1))
      sb.setColor(.6f, .6f, .6f, 1);
    if (isDead())
      sb.setColor(0, 0, 0, 1);
    drawTex(sb, getTexture(), width, height*characterHeight(), 0, 0, pos);
    if (actionType == HEAL && inTransit)
    {
      sb.setColor(1, 1, 1, 1);
      float size = (float)(Math.sin(time*Math.PI)*1.5f+1f);
      drawTex(sb, JTactics.assets.glow, width*size, height*size, 0, 0, pos);
    }
    if (takingTurn && !isDead())
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
    if (!isDead())
    {
      // draw health bar:
      float barWidth = game.field.width*.6f;
      float barHeight = game.field.height*.15f;
      // offset of the health bar (used when there's armor so
      // we don't draw over characters behind this one)
      float barOffY = armor==0?0:-game.field.height*.2f;
      // back of it
      sb.setColor(.4f, 0, 0, 1);
      sb.draw(JTactics.assets.white,
        pos.x+game.field.width*.2f,
        pos.y+game.field.height/2+height+game.field.height*.05f+barOffY,
        barWidth, barHeight);
      // filled part:
      sb.setColor(1, 0, 0, 1);
      sb.draw(JTactics.assets.white,
        pos.x+game.field.width*.2f,
        pos.y+game.field.height/2+height+game.field.height*.05f+barOffY,
        barWidth*(float)(hp>0?hp:0)/(float)maxHp, barHeight);
      // breaks each bar (ticks)
      float tickW = barWidth*.07f;
      for (int i = 1; i < maxHp; ++i)
      {
        sb.setColor(.4f, 0, 0, 1);
        sb.draw(JTactics.assets.white,
          pos.x+game.field.width*.2f+barWidth*((float)(i)/(float)(maxHp))-tickW/2f,
          pos.y+game.field.height/2+height+game.field.height*.02f+barOffY,
          tickW, barHeight*1.4f);
      }
      float armorW = barWidth*.3f;
      float armorH = barWidth*.3f;
      // draw armor if we have any
      for (int i = 0; i < armor; ++i)
      {
        sb.setColor(1f, 1, 1, 1);
        sb.draw(JTactics.assets.shield,
          pos.x+game.field.width*.2f+barWidth*((float)(i+1)/(float)(armor+1))-armorW/2f,
          pos.y+game.field.height/2+height+game.field.height*.02f,
          armorW, armorH);
      }
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
