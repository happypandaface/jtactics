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

public class ActShield extends Action
{
  public void selected(Guy g)
  {
    g.useAp(1);
    g.setupAction();
  }
  public void startAction(Guy g)
  {
    //should never be called as we never set pickingLocation
  }
  
  public Vector2 step(float time)
  {
    Vector2 src = guy.game.field.getPos(guy.tile);
    Vector2 pos = src.cpy();//.add(guy.game.field.width/2f, guy.game.field.height/2f);
    ShieldProj fb = new ShieldProj();
    fb.pos = pos.cpy();
    fb.split = time<.5f?1-(time*2):0;
    guy.game.addProj(fb);
    // we're returning where the character should be, remember that
    // that's why the stuff commented out on the pos decl line puts
    // the character in a weird spot
    return pos;
  }
  
  public void end()
  {
    guy.getArmor(guy, 1);
    guy.shutdownAction();
  }
}
