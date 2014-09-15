package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.*;

public class JTGame
{
  JTField field;
  List<Guy> objs;
  List<Guy> currPlayersObjs;
  SpriteBatch sb;
  Guy selectedGuy;
  List<JTPlayer> players;
  JTPlayer currPlayer = null;
  List<JTTile> obstacles;

  public JTGame()
  {
    field = new JTField();
    objs = new ArrayList<Guy>();
  }

  public void create()
  {
    sb = new SpriteBatch();
    field.create();
  }

  public void startGame()
  {
    players = new ArrayList<JTPlayer>();
    JTPlayer p = new JTPlayer();
    p.type = JTPlayer.HUMAN;
    addPlayer(p);
    JTPlayer pc = new JTPlayer();
    pc.type = JTPlayer.COMPUTER;
    addPlayer(pc);
    obstacles = new ArrayList<JTTile>();
    int fieldW = 8;
    int xTopOff = 1;
    int xBotOff = 0;
    int fieldH = 10;
    int yTopOff = 0;
    int yBotOff = 0;
    for (int x = 0; x < fieldW; ++x)
      for (int y = 0; y < fieldH; ++y)
        for (int off = 0; off < 2; ++off)
          if (x+(xBotOff==1?off:0) == 0 || x+(xTopOff==1?off:0) == fieldW-1 || y+(yBotOff==1?off:0) == 0 || y+(yTopOff==1?off:0) == fieldH-1)
            obstacles.add(new JTTile(off, x, y));

    objs = new ArrayList<Guy>();
    {
      Guy g = new BlackMage();
      g.setController(p);
      g.tile = new JTTile(1, 1, 1);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(p);
      g.tile = new JTTile(0, 1, 2);
      addObj(g);
    }
    {
      Guy g = new WhiteMage();
      g.setController(pc);
      g.tile = new JTTile(0, 4, 4);
      addObj(g);
    }
    {
      Guy g = new BlackMage();
      g.setController(pc);
      g.tile = new JTTile(1, 4, 3);
      addObj(g);
    }
    // this will make the loop get the first player
    currPlayer = null;
  }
  public void  addPlayer(JTPlayer p)
  {
    players.add(p);
  }
  public void addObj(Guy g)
  {
    g.setGame(this);
    g.reset();
    objs.add(g);
  }

  public void switchPlayer(JTPlayer p)
  {
    // tell all the other players that it's not their turn
    // anymore
    if (currPlayer != null)
    {
      for (int i = 0; i < currPlayersObjs.size(); ++i)
      {
        Guy obj = currPlayersObjs.get(i);
        obj.setTurn(false);
      }
    }
    currPlayer = p;
    currPlayersObjs = new ArrayList<Guy>();
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      if (obj.controller == currPlayer)
      {
        obj.newTurn();
        obj.setTurn(true);
        currPlayersObjs.add(obj);
      }
    }
  }
  
  public boolean checkEndTurn()
  {
    int left = 0;
    for (int i = 0; i < currPlayersObjs.size(); ++i)
    {
      Guy obj = currPlayersObjs.get(i);
      if (!obj.okayForTurnEnd())
        left++;
    }
    return (left == 0);
  }

  public boolean checkIsMovable(JTTile t)
  {
    for (int i = 0; i < obstacles.size(); ++i)
    {
      if (obstacles.get(i).check(t))
        return false;
    }
    return true; 
  }

  public void render()
  {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    sb.begin();
    field.render(sb, null);
    // check if the game was won:
    // this should check teams eventually
    boolean gameOver = false;
    List<JTPlayer> playersLeft = new ArrayList<JTPlayer>();
    for (int i = 0; i < players.size(); ++i)
    {
      JTPlayer p = players.get(i);
      int left = 0;
      for (int c = 0; c < objs.size(); ++c)
      {
        Guy obj = objs.get(c);
        if (obj.controller == p && !obj.isDead())
        {
          ++left;
          break;// we just care if one is still alive
        }
      }
      if (left != 0)
      {
        playersLeft.add(p);
      }
    }
    if (playersLeft.size() < 2)// not enough players to continue
      gameOver = true;
    if (!gameOver)
    {
      float dt = Gdx.graphics.getDeltaTime();
      // if we haven't gotten a player
      if (currPlayer == null)
      {
        switchPlayer(players.get(0));
      }else
      {
        // check to see if it should be someone else's turn
        if (checkEndTurn())
        {
          // this guy's turn is over, switch players
          int idx = players.indexOf(currPlayer);
          switchPlayer(players.get((idx+1)%players.size()));
        }
      }
      if (Gdx.input.justTouched())
      {
        if (field.selectedTile != null)
        {
          if (Gdx.input.isButtonPressed(1) && selectedGuy != null)
          {
            // check if this is an attack (or legal):
            boolean stillAMove = true;
            for (int i = 0; i < objs.size(); ++i)
            {
              Guy obj = objs.get(i);
              if (obj.tile.check(field.selectedTile))
              {
                stillAMove = false;
                if (obj.controller != selectedGuy.controller)
                {
                  // do the attack
                  selectedGuy.attack(obj);
                }
                break;
              }
            }
            if (stillAMove)
              selectedGuy.moveTo(field.selectedTile);
          }else
          if (Gdx.input.isButtonPressed(0))
          {
            // alert the last selected guy
            if (selectedGuy != null)
              selectedGuy.setSelected(false);
            // find the next one (default: null)
            selectedGuy = null;
            for (int i = 0; i < objs.size(); ++i)
            {
              Guy obj = objs.get(i);
              if (obj.checkSelect(field.selectedTile))
              {
                selectedGuy = obj;
                // tell the new guy he was selected
                selectedGuy.setSelected(true);
                break;
              }
            }
          }
        }
      }
      for (int i = 0; i < objs.size(); ++i)
      {
        objs.get(i).step(dt); 
      }
    }
    for (int i = 0; i < obstacles.size(); ++i)
    {
      JTTile t = obstacles.get(i);
      Vector2 pos = field.getPos(t);
      sb.setColor(.5f, .2f, .3f, 1);
      sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
    }
    // order for draw (lower Y objs in front)
    List<Guy> objsCpy = new ArrayList<Guy>(objs);
    int highestY = 0;
    int drawIdx = -1;
    while(objsCpy.size() > 0)
    {
      highestY = 0;
      drawIdx = -1;
      for (int i = 0; i < objsCpy.size(); ++i)
      {
        Guy obj = objsCpy.get(i);
        
        if (obj.tile.y*2+obj.tile.off > highestY || drawIdx == -1)
        {
          drawIdx = i;
          highestY = obj.tile.y*2+obj.tile.off;
        }
      }
      Guy closestObj = objsCpy.remove(drawIdx);
      closestObj.draw(sb, null);
    }
    /* old draw funct
    for (int i = 0; i < objs.size(); ++i)
    {
      objs.get(i).draw(sb, null); 
    }*/
    if (playersLeft.size() < 2)
    {
      // tell who won:
      String endStr = "";
      if (playersLeft.size() == 0)
      {
        // no one won:
        endStr = "Everyone loses!";
      }else
      {
        // figure out who won
        int playerNum = -1;
        for (int i = 0; i < players.size(); ++i)
        {
          if (playersLeft.get(0) == players.get(i))
          {
            playerNum = i;
          }
        }
        endStr = "Player "+(playerNum+1)+" wins!";
      }
      BitmapFont.TextBounds tb = JTactics.assets.font.getWrappedBounds(endStr, Gdx.graphics.getWidth());
      float midX = Gdx.graphics.getWidth()/2f;
      float midY = Gdx.graphics.getHeight()/2f;
      float pad = Gdx.graphics.getWidth()*.29f;
      sb.setColor(.4f, .5f, .9f, .9f);
      sb.draw(JTactics.assets.glow, midX-tb.width/2f-pad/2f, midY-tb.height/2f-pad/2f, tb.width+pad, tb.height+pad);
      JTactics.assets.font.setColor(1f, 1f, 1, 1);
      JTactics.assets.font.drawWrapped(sb, endStr, midX-tb.width/2f, midY+tb.height/2f, Gdx.graphics.getWidth());
      if (Gdx.input.justTouched())
      {
        startGame();
      }
    }

    sb.end();
  }
}
