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

public class FrogKnight extends Guy
{
  public FrogKnight()
  {
    super();
    maxHp = 3;
    maxAp = 3;
    possibleActions.add(Action.DEFEND);
  }
  @Override
  public Texture getTexture()
  {
    return JTactics.assets.frogKnight;
  }
  @Override
  public boolean canRange()
  {
    return false;
  }
}
