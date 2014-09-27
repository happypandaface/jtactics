package com.zooth.jt.actions;

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
import com.zooth.jt.projectiles.*;
import com.zooth.jt.objs.*;

public class Invincibility extends Action
{
  Guy tar;
  public boolean canCast(Guy g)
  {
    return g.hasAp(2);
  }
  public void selected(Guy g)
  {
    guy = g;
    guy.pickingLocation = !guy.pickingLocation;
  }
  public void startAction(Guy g)
  {
    guy = g;
    tar = guy.game.guyAt(dirTile);
    // make sure there is a guy there
    if (tar != null)
    {
      guy.pickingLocation = false;
      guy.useAp(2);
      guy.setupAction();
    }
  }
  
  public Vector2 step(float time)
  {
    Vector2 src = guy.game.field.getPos(guy.tile);
    Vector2 pos = src.cpy();
    return pos;
  }
  public void draw(SpriteBatch sb, Vector2 pos, float w, float h, float time)
  {
    sb.setColor(1, 1, 1, 1);
    float size = (float)(Math.sin(guy.time*Math.PI)*1.5f+1f);
    guy.drawTex(sb, JTactics.assets.glow, w*size, h*size, 0, 0, pos);
  }
  
  public void end()
  {
    guy.shutdownAction();
    tar.invincTurns++;
  }
}
