package com.zooth.jt.cinematics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.zooth.jt.*;
import com.zooth.jt.cinematics.*;

public class Track
{
  public List<TrackEvent> events = new ArrayList<TrackEvent>();
  public int currIdx;
  public Texture bg;
  
  public void reset()
  {
    currIdx = 0;
  }
}