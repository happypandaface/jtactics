package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;

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
    selectedGuy = null;
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

  // returns the first guy at the tile
  public Guy guyAt(JTTile t)
  {
    for (int i = 0; i < objs.size(); ++i)
    {
      if (objs.get(i).checkAt(t))
        return objs.get(i);
    }
    return null; 
  }
  // sees if that tile has obstacles on it
  public boolean checkIsMovable(JTTile t)
  {
    for (int i = 0; i < obstacles.size(); ++i)
    {
      if (obstacles.get(i).check(t))
        return false;
    }
    return true; 
  }
  // this function makes an object try to 'target' a tile
  // this could mean healing the unit there, or 
  // attacking an enemy at the tile, or simple
  // moving to the tile
  public boolean target(Guy guy, JTTile t)
  {// TODO: return whether the target was legal
    boolean stillAMove = true;
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      if (obj.tile.check(t))
      {
        stillAMove = false;
        if (obj.controller != guy.controller)
        {
          // try an attack
          guy.attack(obj);
        }else
        {
          // it's a friendly, try a heal
          guy.heal(obj);
        }
        break;
      }
    }
    if (stillAMove)
      guy.moveTo(t);
    return true;
  }
  // guys will add projectiles during their step (each frame)
  // they draw over everything
  List<Fireball> projectiles;
  public void addProj(Fireball fb)
  {
    fb.setGame(this);
    projectiles.add(fb);
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
      // if we haven't gotten a player (start of game)
      if (currPlayer == null)
      {
        switchPlayer(players.get(0));
      }else
      {
        // check to see if it should be someone else's turn
        if (checkEndTurn() || Gdx.input.isKeyJustPressed(Keys.SPACE))
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
            if (currPlayer.type == JTPlayer.HUMAN)
            {// only issue commands for human player
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
                    // try an attack
                    selectedGuy.attack(obj);
                  }else
                  {
                    // it's a friendly, try a heal
                    selectedGuy.heal(obj);
                  }
                  break;
                }
              }
              if (stillAMove)
                selectedGuy.moveTo(field.selectedTile);
            }
          }else
          if (Gdx.input.isButtonPressed(0))
          {
            // check if the selectedGuy's GUI was clicked
            boolean guiClicked = false;
            if (selectedGuy != null)
              guiClicked = selectedGuy.checkGUIClick(new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()));
            if (!guiClicked)
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
      }
      if (currPlayer.type == JTPlayer.COMPUTER)
      {// it's a computer, do AI
        for (int i = 0; i < currPlayersObjs.size(); ++i)
        {
          Guy obj = currPlayersObjs.get(i);
          if (obj.inTransit || obj.movesLeft() > 0)
          {// it's important to check inTransit
            // because even if we don't do an action,
            // we may break at the end (character by character
            // commands)
            if (!obj.inTransit)
            {// don't assign moves to characters in an action
              // this prevents computers from queueing

              // first check outer ring targets
              List<JTTile> outerTiles = obj.getAdjTiles(2);
              for (int c = 0; c < outerTiles.size(); ++c)
              {
                JTTile t = outerTiles.get(c);
                Guy g = guyAt(t);
                if (g != null && !g.isDead())
                {
                  if (!obj.inTransit)
                    target(obj, t);
                }
              }

              // then check inner ring targets
              if (!obj.inTransit)
              {
                List<JTTile> innerTiles = obj.getAdjTiles(1);
                for (int c = 0; c < innerTiles.size(); ++c)
                {
                  JTTile t = innerTiles.get(c);
                  Guy g = guyAt(t);
                  if (g != null && !g.isDead())
                  {
                    if (!obj.inTransit)
                      target(obj, t);
                  }
                }
              }
              if (!obj.inTransit)
              {// check again, computers REALLY can't handle queueing
                // if we got to this point we don't have any
                // other good targets, we should position
                // so we can fight later (this algorithm will
                // become more complicated, but now we just walk
                // towards enemies)
                List<JTTile> path = null;
                for (int c = 0; c < objs.size(); ++c)
                {
                  Guy currObj = objs.get(c);
                  if (currObj.controller != obj.controller && !currObj.isDead())
                  {
                    List<JTTile> currPath = currObj.tile.getPath(obj.tile, this);
                    if (path == null || currPath.size() < path.size())
                      path = currPath;
                  }
                }
                // because the first node in the path, is the
                // tile we're currently on, we use the second
                // on
                JTTile tarTile = path.get(1);
                target(obj, tarTile);
                /* random directions:
                List<JTTile> innerTiles = obj.getAdjTiles(1);
                
                JTTile t = innerTiles.get((int)(Math.floor(Math.random()*innerTiles.size())));
                target(obj, t);
                */
              }
            }
            //break; // with this break in, the computer does moves one at a time
          }
        }
      }
      for (int i = 0; i < objs.size(); ++i)
      {
        objs.get(i).step(dt); 
      }
    }
    // now for drawing!!!
    // draw obstables:
    for (int i = 0; i < obstacles.size(); ++i)
    {
      JTTile t = obstacles.get(i);
      Vector2 pos = field.getPos(t);
      sb.setColor(.5f, .2f, .3f, 1);
      sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
    }
    // only not null if a guy is selected
    List<JTTile> actionTiles = null;
    if (selectedGuy != null)
    {
      actionTiles = selectedGuy.getAdjTiles(1);
      sb.setColor(0, 0, 1f, .4f);
      if (actionTiles != null)
        for (int i = 0; i < actionTiles.size(); ++i)
        {
          JTTile tile = actionTiles.get(i);
          Vector2 pos = field.getPos(tile);
          sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
        }
      actionTiles = selectedGuy.getAdjTiles(2);
      sb.setColor(0, 1f, 0f, .4f);
      if (actionTiles != null)
        for (int i = 0; i < actionTiles.size(); ++i)
        {
          JTTile tile = actionTiles.get(i);
          Vector2 pos = field.getPos(tile);
          sb.draw(JTactics.assets.hex, pos.x, pos.y, field.width, field.height);
        }

    }
    // reset projectiles array so objs can add them
    projectiles = new ArrayList<Fireball>();
    // now for drawing objects:
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
    // done calling the draw funct on objects
    // (which adds projectiles)
    // this means all the projectiles have been added
    // just draw them, same order as added
    for (int i = 0; i < projectiles.size(); ++i)
    {
      Fireball fb = projectiles.get(i);
      fb.draw(sb);
    }
	// now draw the selected guy's GUI
	if (selectedGuy != null)
		selectedGuy.drawGUI(sb, null);
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
