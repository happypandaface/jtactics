package com.zooth.jt;

import com.badlogic.gdx.math.*;
import java.util.*;
import com.zooth.jt.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.zooth.jt.actions.*;

/*
this is the class that allows us to make
and identify actions
this base action is the fireball action
it should be split into ActFireball.java

adding an action requires that you
make a class in actions/ that extends
this class and update this file's
getTex and make functions and add
a new enum (int) for it. Then you
give it to a Guy by adding it
to their possibleActions array
*/
public class Action
{
  public static final int FIREBALL = 1;
  public static final int DEFEND = 2;
  // returns the gui texture of the action
  public static Texture getTex(int type)
  {
    switch(type)
    {
      case FIREBALL:
        return JTactics.assets.fireball;
      case DEFEND:
        return JTactics.assets.shield;
    }
    return null;
  }
  // returns a new action of the given type
  public static Action make(int type)
  {
    switch(type)
    {
      case FIREBALL:
        return new Action();
      case DEFEND:
        return new ActShield();
    }
    return null;
  }
  public Guy attackTarget;
  public Guy guy;
  public JTTile dirTile;// set outside
  public List<JTTile> tilesInDir;
  // called when we first click on the action
  public void selected(Guy g)
  {
    guy.pickingLocation = !guy.pickingLocation;
  }
  // called when we have selected a tile for the action
  // only called if we set pickingLocation with 
  // the selected function
  public void startAction(Guy g)
  {
    guy = g;
    guy.actionType = Guy.DOING_ACTION;
    guy.time = 0;
    guy.useAp(2);
    guy.inTransit = true;
    int dir = guy.tile.getDirection(dirTile);
    tilesInDir = guy.tile.direction(dir, 2);
    guy.destTile = tilesInDir.get(tilesInDir.size()-1);
    guy.pickingLocation = false;
  }
  public boolean canCast(Guy g)
  {
    return g.hasAp(2);
  }
  
  public Vector2 step(float time)
  {
    Vector2 src = guy.game.field.getPos(guy.tile);
    Vector2 dst = guy.game.field.getPos(guy.destTile);
    Vector2 pos = src.cpy();
    Fireball fb = new Fireball();
    float delay = .3f;
    fb.pos = dst.cpy().sub(src).scl(time<delay?0:(float)Math.sin((time-delay)/(1-delay)*Math.PI/2f)).add(src);
    fb.tail = dst.cpy().sub(src).scl(time<delay||time>(1-delay)?(delay)*(float)Math.sin(time*Math.PI):(delay)*(float)Math.sin((delay)*Math.PI));
    guy.game.addProj(fb);
    return pos;
  }
  
  public void end()
  {
    guy.inTransit = false;
    guy.destTile = null;
    for (int i = 0; i < tilesInDir.size(); ++i)
    {
      JTTile tile = tilesInDir.get(i);
      Guy currG = guy.game.guyAt(tile);
      if (currG != null && currG != guy)
      {
        currG.getDamaged(guy, 1);
      }
    }
  }
}
