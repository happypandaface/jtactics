package com.zooth.jt;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;

public class JTTile
{
  int off = 0;
  int x = 0;
  int y = 0;
  public static class Direction
  {
    public static int ZERO = 0;
    public static int N = 1;
    public static int NNE = 2;
    public static int NE = 3;
    public static int NEE = 4;
    public static int E = 5;
    public static int SEE = 6;
    public static int SE = 7;
    public static int SSE = 8;
    public static int S = 9;
    public static int SSW = 10;
    public static int SW = 11;
    public static int SWW = 12;
    public static int W = 13;
    public static int NWW = 14;
    public static int NW = 15;
    public static int NNW = 16;
    public static int NUM_DIRECTIONS = 16;
  }
  
  public JTTile(int off, int x, int y)
  {
    this.off = off;
    this.x = x;
    this.y = y;
  }
  @Override
  public String toString()
  {
    return "["+off+":("+x+", "+y+")]";
  }

  public boolean check(JTTile t)
  {
    Vector2 src = toTiltedCoords();
    Vector2 dst = t.toTiltedCoords();
    return src.x == dst.x && src.y == dst.y;
    //return check(t.off, t.x, t.y);
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
  
  public Vector2 toTiltedCoords()
  {
    Vector2 rtn = new Vector2(this.x+this.y, this.y-this.x);
    if (this.off == 1)
      rtn.x += 1;
    return rtn;
  }
  
  static JTTile fromTiltedCoords(Vector2 v)
  {
    JTTile rtn = new JTTile(((int)Math.abs(v.x+v.y))%2, (int)Math.floor((v.x-v.y)/2), (int)Math.floor((v.x+v.y)/2));
    return rtn;
  }
  
  public int getDirection(JTTile t)
  {
    Vector2 src = toTiltedCoords();
    Vector2 dst = t.toTiltedCoords();
    int diffX = (int)(dst.x - src.x);
    int diffY = (int)(dst.y - src.y);
    int adiffX = (int)Math.abs(diffX);
    int adiffY = (int)Math.abs(diffY);
    if (diffX == diffY) // S or N
    {
      if (diffX > 0 && diffY > 0)
        return Direction.N;
      if (diffX < 0 && diffY < 0)
        return Direction.S;
    }else
    if (diffX == -diffY) // E or W
    {
      if (diffX > 0 && diffY < 0)
        return Direction.E;
      if (diffX < 0 && diffY > 0)
        return Direction.W;
    }else
    if (diffY == 0) // NE or SW
    {
      if (diffX > 0)
        return Direction.NE;
      if (diffX < 0)
        return Direction.SW;
    }else
    if (diffX == 0) // NE or SW
    {
      if (diffY > 0)
        return Direction.NW;
      if (diffY < 0)
        return Direction.SE;
    }
    return Direction.ZERO;
  }
  public List<JTTile> direction(int dir, int num)
  {
    List <JTTile> tiles = new ArrayList<JTTile>();
    tiles.add(this.copy());
    Vector2 curr = toTiltedCoords();
    for (int i = 0; i < num; ++i)
    {
      if (dir == Direction.N)
      {
        curr.x++;
        curr.y++;
      }else
      if (dir == Direction.S)
      {
        curr.x--;
        curr.y--;
      }else
      if (dir == Direction.E)
      {
        // east and west are special
        ++i;// adding extra spaces
        {
          curr.x++;
          JTTile t = fromTiltedCoords(curr);
          tiles.add(t);
        }
        {
          curr.x--;
          curr.y--;
          JTTile t = fromTiltedCoords(curr);
          tiles.add(t);
        }
        // this has to be the last one (so we know where it ends)
        {
          curr.x++;
          // we add this last one at the end of the if statement
        }
      }else
      if (dir == Direction.W)
      {
        // read the above if statement for more clarification here
        ++i;
        {
          curr.x--;
          JTTile t = fromTiltedCoords(curr);
          tiles.add(t);
        }
        {
          curr.y++;
          curr.x++;
          JTTile t = fromTiltedCoords(curr);
          tiles.add(t);
        }
        {
          curr.x--;
          // we add this last one at the end of the if statement
        }
      }else
      if (dir == Direction.NE)
      {
        curr.x++;
      }else
      if (dir == Direction.SW)
      {
        curr.x--;
      }else
      if (dir == Direction.NW)
      {
        curr.y++;
      }else
      if (dir == Direction.SE)
      {
        curr.y--;
      }
      JTTile t = fromTiltedCoords(curr);
      tiles.add(t);
    }
    return tiles;
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
            game.checkIsPathable(currAdjTile))// I guess we could end
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
  
  // this gets a ring around the tile
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
