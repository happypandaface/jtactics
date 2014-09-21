package com.zooth.jt;

import java.util.*;

public class JTPlayer
{
  static int EMPTY = 0;
  static int COMPUTER = 1;
  static int HUMAN = 2;
  int type = EMPTY;
  JTGame game;
  
  public void setGame(JTGame g)
  {
    game = g;
  }
  
  public void doAi(List<Guy> objs)
  {
    boolean attackOccurring = false;
    for (int i = 0; i < objs.size(); ++i)
    {
      Guy obj = objs.get(i);
      // make sure we control this character
      if (obj.controller == this)
      {
        // check if they're attacking
        if (obj.inTransit && obj.actionType != Guy.MOVE)
          attackOccurring = true;
        if (obj.inTransit || obj.movesLeft() > 0)
        {// it's important to check inTransit
          // because even if we don't do an action,
          // we may break at the end (character by character
          // commands)
          if (!obj.inTransit)
          {// don't assign moves to characters in an action
            // this prevents computers from queueing
            
            // we only want one attack occuring at once
            if (!attackOccurring)
            {
              // first check outer ring targets
              List<JTTile> outerTiles = obj.getAdjTiles(2);
              for (int c = 0; c < outerTiles.size(); ++c)
              {
                JTTile t = outerTiles.get(c);
                Guy g = game.guyAt(t);
                if (g != null && !g.isDead())
                {
                  if (!obj.inTransit)
                    game.target(obj, t);
                }
              }

              // then check inner ring targets
              if (!obj.inTransit)
              {
                List<JTTile> innerTiles = obj.getAdjTiles(1);
                for (int c = 0; c < innerTiles.size(); ++c)
                {
                  JTTile t = innerTiles.get(c);
                  Guy g = game.guyAt(t);
                  if (g != null && !g.isDead())
                  {
                    if (!obj.inTransit)
                      game.target(obj, t);
                  }
                }
              }
            }
            if (obj.inTransit && obj.actionType != Guy.MOVE)
              attackOccurring = true;
            if (!obj.inTransit)
            {// check again, computers REALLY can't handle queueing
              // if we got to this point we don't have any
              // other good targets, we should do a low priority
              // action 
              // like positioning
              // so we can fight later (this algorithm will
              // become more complicated, but now we just walk
              // towards enemies)
              if (!obj.hasAp(2) && obj.hasAp(1) && obj.checkCanPerform(Action.DEFEND))
              {
                obj.currAction = Action.make(Action.DEFEND);
                obj.currAction.guy = obj;
                obj.currAction.selected(obj);
              }else
              {
                List<JTTile> path = null;
                for (int c = 0; c < objs.size(); ++c)
                {
                  Guy currObj = objs.get(c);
                  if (currObj.controller != obj.controller && !currObj.isDead())
                  {
                    List<JTTile> currPath = currObj.tile.getPath(obj.tile, game);
                    if (path == null || (currPath != null && currPath.size() < path.size()))
                      path = currPath;
                  }
                }
                // the path should be larger than 2, otherwise it's an attack
                if (path != null && path.size() > 2)
                {
                  // because the first node in the path, is the
                  // tile we're currently on, we use the second
                  // on
                  JTTile tarTile = path.get(1);
                  game.target(obj, tarTile);
                }else
                {
                  // just sit here
                  // but first make sure there's no other attacks
                  // going
                  if (!attackOccurring)
                    obj.useAp(1);
                }
                /* random directions:
                List<JTTile> innerTiles = obj.getAdjTiles(1);
                
                JTTile t = innerTiles.get((int)(Math.floor(Math.random()*innerTiles.size())));
                game.target(obj, t);
                */
              }
            }
          }
          //break; // with this break in, the computer does moves one at a time
        }
      }
    }
  }
}
