package com.zooth.jt.cinematics;

import java.util.*;

public class Track
{
  public List<TrackEvent> events = new ArrayList<TrackEvent>();
  public int currIdx;
  
  public void reset()
  {
    currIdx = 0;
  }
}