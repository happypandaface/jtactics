package com.zooth.jt.projectiles;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.zooth.jt.*;

public class InvincibilityProj extends Fireball
{
  public float split = 0;
  public void draw(SpriteBatch sb)
  {
    float width = game.field.height*.35f;
    float height = (float)JTactics.assets.shieldR.getHeight()/(float)JTactics.assets.shieldR.getWidth()*width;
    sb.setColor(1, 1, 1, 1);
    pos.add(game.field.width/2f, game.field.height/2f-height*.2f);
    sb.draw(JTactics.assets.shieldR, pos.x+split*game.field.height, pos.y, width, height);
    sb.draw(JTactics.assets.shieldL, pos.x-width-split*game.field.height, pos.y, width, height);
  }
}
