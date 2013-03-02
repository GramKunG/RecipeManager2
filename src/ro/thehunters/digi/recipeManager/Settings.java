package ro.thehunters.digi.recipeManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * RecipeManager's settings loaded from its config.yml, values are read-only.
 */
public class Settings
{
    public final boolean              SPECIAL_REPAIR;
    public final boolean              SPECIAL_REPAIR_METADATA;
    
    public final boolean              SPECIAL_LEATHER_DYE;
    public final boolean              SPECIAL_FIREWORKS;
    public final boolean              SPECIAL_MAP_CLONING;
    public final boolean              SPECIAL_MAP_EXTENDING;
    
    public final boolean              SOUNDS_REPAIR;
    public final boolean              SOUNDS_FAILED;
    public final boolean              SOUNDS_FAILED_CLICK;
    
    public final boolean              UPDATE_BOOKS;
    public final boolean              COLOR_CONSOLE;
    
    public final boolean              RETURN_BUCKETS;
    public final boolean              RETURN_POTIONS;
    public final boolean              RETURN_BOWL;
    
    public final boolean              FUEL_RETURN_BUCKETS;
    
    public final char                 FURNACE_SHIFT_CLICK;
    public final int                  FURNACE_TICKS;
    
    public final boolean              MULTITHREADING;
    
    public final boolean              CLEAR_RECIPES;
    
    public final boolean              METRICS;
    
    protected final String            LASTCHANGED;
    
    private final Map<String, String> itemAlias = new HashMap<String, String>();
    private final Map<String, String> aliasItem = new HashMap<String, String>();
    
    public Settings(CommandSender sender)
    {
        // Load/reload/generate config.yml
        FileConfiguration yml = loadYML(sender, "config.yml");
        
        RecipeManager.plugin.reloadConfig();
        
        SPECIAL_REPAIR = yml.getBoolean("special-recipes.repair", true);
        SPECIAL_REPAIR_METADATA = yml.getBoolean("special-recipes.repair-metadata", false);
        
        SPECIAL_LEATHER_DYE = yml.getBoolean("special-recipes.leather-armor-dye", false);
        SPECIAL_FIREWORKS = yml.getBoolean("special-recipes.fireworks", false);
        SPECIAL_MAP_CLONING = yml.getBoolean("special-recipes.map-cloning", false);
        SPECIAL_MAP_EXTENDING = yml.getBoolean("special-recipes.map-extending", false);
        
        SOUNDS_REPAIR = yml.getBoolean("sounds.repair", true);
        SOUNDS_FAILED = yml.getBoolean("sounds.failed", true);
        SOUNDS_FAILED_CLICK = yml.getBoolean("sounds.failed_click", true);
        
        UPDATE_BOOKS = yml.getBoolean("update-books", true);
        COLOR_CONSOLE = yml.getBoolean("color-console", true);
        
        RETURN_BUCKETS = yml.getBoolean("return-empty.buckets", true);
        RETURN_POTIONS = yml.getBoolean("return-empty.potions", true);
        RETURN_BOWL = yml.getBoolean("return-empty.bowl", true);
        
        FUEL_RETURN_BUCKETS = yml.getBoolean("fuel-return-buckets", true);
        FURNACE_SHIFT_CLICK = yml.getString("furnace-shift-click", "f").charAt(0);
        FURNACE_TICKS = yml.getInt("furnace-ticks", 1);
        
        MULTITHREADING = yml.getBoolean("multithreading", true);
        
        CLEAR_RECIPES = yml.getBoolean("clear-recipes", false);
        
        METRICS = yml.getBoolean("metrics", true);
        
        LASTCHANGED = yml.getString("lastchanged");
        
        Messages.log("config.yml settings:");
        Messages.log("    special-recipes.repair: " + SPECIAL_REPAIR);
        Messages.log("    special-recipes.repair-metadata: " + SPECIAL_REPAIR_METADATA);
        Messages.log("    special-recipes.leather-dye: " + SPECIAL_LEATHER_DYE);
        Messages.log("    special-recipes.fireworks: " + SPECIAL_FIREWORKS);
        Messages.log("    special-recipes.map-cloning: " + SPECIAL_MAP_CLONING);
        Messages.log("    special-recipes.map-extending: " + SPECIAL_MAP_EXTENDING);
        Messages.log("    update-books: " + UPDATE_BOOKS);
        Messages.log("    color-console: " + COLOR_CONSOLE);
        Messages.log("    return-empty.buckets: " + RETURN_BUCKETS);
        Messages.log("    return-empty.potions: " + RETURN_POTIONS);
        Messages.log("    return-empty.bowl: " + RETURN_BOWL);
        Messages.log("    fuel-return-buckets: " + FUEL_RETURN_BUCKETS);
        Messages.log("    furnace-shift-click: " + FURNACE_SHIFT_CLICK);
        Messages.log("    furnace-ticks: " + FURNACE_TICKS);
        Messages.log("    metrics: " + METRICS);
        
        yml = loadYML(sender, "aliases.yml");
        
        aliasItem.clear();
        itemAlias.clear();
        String alias;
        String item;
        
        for(Entry<String, Object> entry : yml.getValues(false).entrySet())
        {
            if(entry.getKey().equals("lastchanged"))
                continue;
            
            item = entry.getValue().toString().toUpperCase();
            
            if(!item.contains(":"))
                item = item + ":*";
            
            alias = entry.getKey().toUpperCase();
            
            aliasItem.put(alias, item);
            itemAlias.put(item, alias);
        }
    }
    
    public static void reload(CommandSender sender)
    {
        RecipeManager.settings = new Settings(sender);
        
        if(!Files.LASTCHANGED_CONFIG.equals(RecipeManager.settings.LASTCHANGED))
            Messages.send(sender, ChatColor.YELLOW + "The 'config.yml' file is outdated, please delete it to allow it to be generated again.");
    }
    
    private FileConfiguration loadYML(CommandSender sender, String fileName)
    {
        File file = new File(RecipeManager.getPlugin().getDataFolder() + File.separator + fileName);
        
        if(!file.exists())
        {
            RecipeManager.getPlugin().saveResource(fileName, false);
            Messages.log("Generated and loaded '" + fileName + "' file.");
        }
        else
            Messages.log("Loaded '" + fileName + "' file.");
        
        return YamlConfiguration.loadConfiguration(file);
    }
}