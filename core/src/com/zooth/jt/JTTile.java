package com.zooth.jt;

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
}
