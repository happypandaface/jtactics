package com.zooth.jt;

import java.util.*;

public class JTTile
{
  int off = 0;
  int x = 0;
  int y = 0;

  public JTTile(int off, int x, int y)
  {
    this.off = off;
    this.x = x;
    this.y = y;
  }

  public boolean check(JTTile t)
  {
    return check(t.off, t.x, t.y);
  }
  public boolean check(int off, int x, int y)
  {
    return (this.off == off)&&
    (this.x == x)&&
    (this.y == y);
  }
  public JTTile copy()
  {
    return new JTTile(off, x, y);
  }
  
  // breadth-first search to get the
  // path to the target tile
  public List<JTTile> getPath(JTTile target, JTGame game)
  {
    List<JTTile> toTry = new ArrayList<JTTile>();
    toTry.add(this.copy());
    List<JTTile> tried = new ArrayList<JTTile>();
    Map<JTTile, JTTile> parents = new HashMap<JTTile, JTTile>();
    while (toTry.size() > 0)
    {
      JTTile currTile = toTry.remove(0);
      // say that we tried this node
      tried.add(currTile);
      if (currTile.check(target))
      {
        // this is the one!
        // iterate back through parents to 
        // get the correct path
        List<JTTile> correctPath = new ArrayList<JTTile>();
        correctPath.add(currTile);
        // keep getting and adding the parent until we get to
        // the root node (which has no parent)
        JTTile parent = parents.get(currTile);
        while (parent != null)
        {
          correctPath.add(parent);
          parent = parents.get(parent);
        }
        return correctPath;
      }
      List<JTTile> currAdjTiles = currTile.getAdjTiles(1);
      for (int i = 0; i < currAdjTiles.size(); ++i)
      {
        JTTile currAdjTile = currAdjTiles.get(i);
        // check if we tried this node
        boolean alreadyTried = false;
        for (int c = 0; c < tried.size(); ++c)
        {
          if (tried.get(c).check(currAdjTile))
          {
            alreadyTried = true;
            break;
          }
        }
        if (!alreadyTried)
        {
          // add this tile to the queue and
          // add it's parent relationship to the
          // map
          // but only if it's traversable
          Guy g = game.guyAt(currAdjTile);
          if ((g == null || g.checkAt(target)) &&
            game.checkIsMovable(currAdjTile))// I guess we could end
            // if it's the target, but oh well
          {
            toTry.add(currAdjTile);
            parents.put(currAdjTile, currTile);
          }
        }
      }
    }
    return null;
  }

  public List<JTTile> getAdjTiles(int num)
  {
    List<JTTile> tiles = new ArrayList<JTTile>();
    if (num == 1)
    {
      {
        JTTile t = this.copy();
        t.y -= 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.y += 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off += 1;
        t.off = t.off%2;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off += 1;
        t.off = t.off%2;
        if (t.off == 0)
        {
          t.x += 1;
          t.y += 1;
        }else
        {
          t.x -= 1;
          t.y -= 1;
        }
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off += 1;
        t.off = t.off%2;
        if (t.off == 0)
        {
          t.y += 1;
        }else
        {
          t.y -= 1;
        }
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off += 1;
        t.off = t.off%2;
        if (t.off == 0)
        {
          t.x += 1;
        }else
        {
          t.x -= 1;
        }
        tiles.add(t);
      }
    }
    if (num == 2)
    {
      {
        JTTile t = this.copy();
        t.y += 2;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.y -= 2;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.x += 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.x -= 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.y += 1;
        t.x += 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.y -= 1;
        t.x += 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.y -= 1;
        t.x -= 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.y += 1;
        t.x -= 1;
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off = (t.off+1)%2;
        if (t.off == 0)
        {
          t.x += 1;
          t.y += 2;
        }else
        {
          t.y += 1;
        }
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off = (t.off+1)%2;
        if (t.off == 0)
        {
          t.x += 1;
          t.y -= 1;
        }else
        {
          t.y -= 2;
        }
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off = (t.off+1)%2;
        if (t.off == 0)
        {
          t.y += 2;
        }else
        {
          t.x -= 1;
          t.y += 1;
        }
        tiles.add(t);
      }
      {
        JTTile t = this.copy();
        t.off = (t.off+1)%2;
        if (t.off == 0)
        {
          t.y -= 1;
        }else
        {
          t.x -= 1;
          t.y -= 2;
        }
        tiles.add(t);
      }
    }
    return tiles;
  }
}
