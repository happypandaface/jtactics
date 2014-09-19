package com.zooth.jt;

import com.badlogic.gdx.math.*;
import java.util.*;

public class Action
{
  public Guy attackTarget;
  public Guy guy;
  public JTTile dirTile;// set outside
  List<JTTile> tilesInDir;
  public void startAction(Guy g)
  {
    guy = g;
    guy.actionType = Guy.DOING_ACTION;
    guy.time = 0;
    guy.useAp(1);
    guy.inTransit = true;
    int dir = guy.tile.getDirection(dirTile);
    tilesInDir = guy.tile.direction(dir, 2);
    guy.destTile = tilesInDir.get(tilesInDir.size()-1);
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