package com.cyr1en.m1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class M1 extends JavaPlugin {

  @Override
  public void onEnable() {
    Bukkit.getScheduler().runTaskLater(this, () -> Roll.rollOut(0, false), 20L);
  }

}
