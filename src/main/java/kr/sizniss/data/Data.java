package kr.sizniss.data;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Data extends JavaPlugin {

    public static Data plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        new Files(); // 파일 객체 생성
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // files.saveData();
    }
}
