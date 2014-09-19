package com.zooth.jt.guys;

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

public class Frog extends Guy
{
  public Frog()
  {
    super();
    maxHp = 2;
    maxAp = 2;
  }
  @Override
  public Texture getTexture()
  {
    return JTactics.assets.frog;
  }
  @Override
  public boolean canRange()
  {
    return false;
  }
  public void reset()
  {
    hp = 2;
    ap = 0;
  }
}
