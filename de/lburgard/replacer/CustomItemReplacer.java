// 
// Decompiled by Procyon v0.5.36
// 

package de.lburgard.replacer;

import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import java.util.Map;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.Inventory;
import de.lburgard.replacer.listener.VillagerTradeListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import de.lburgard.replacer.listener.ChestListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import de.lburgard.replacer.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemReplacer extends JavaPlugin
{
    private static CustomItemReplacer instance;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        CustomItemReplacer.instance = this;
        this.configManager = new ConfigManager();
        Bukkit.getPluginManager().registerEvents((Listener)new ChestListener(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new VillagerTradeListener(), (Plugin)this);
        key = new NamespacedKey(instance, "replacer");
    }
    
    @Override
    public void onDisable() {
    }
    public static NamespacedKey key;
   
    public void replaceChest(final Inventory inventory) {
        for (final ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null) {
            	if (itemStack.hasItemMeta() && (itemStack.getItemMeta().getPersistentDataContainer().has(CustomItemReplacer.key, PersistentDataType.BOOLEAN) || itemStack.getItemMeta().hasCustomModelData())) {
            		return;
            	}
             	String customItem;
              	if (itemStack.getItemMeta().hasDisplayName() && itemStack.hasItemMeta()) {
              		customItem = CustomItemReplacer.getInstance().getConfigManager().getString(itemStack.getItemMeta().getDisplayName());
              	  if (customItem != null) {
              		customItem = CustomItemReplacer.getInstance().getConfigManager().getString(itemStack.getType().toString().toLowerCase());
              	  }
              	}
              	else {
              		customItem = CustomItemReplacer.getInstance().getConfigManager().getString(itemStack.getType().toString().toLowerCase());
              	}
                if (customItem != null) {
                    if (CustomStack.isInRegistry(customItem)) {
                        final CustomStack customStack = CustomStack.getInstance(customItem);
                        final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)itemStack.getEnchantments();
                        final ItemStack finalItem = customStack.getItemStack();
                        finalItem.addUnsafeEnchantments((Map)enchantments);
                        finalItem.setAmount(itemStack.getAmount());
                        ItemMeta meta = finalItem.getItemMeta();
                        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
                        finalItem.setItemMeta(meta);
                        inventory.all(itemStack).forEach((key, value) -> inventory.setItem((int)key, finalItem));
                    }
                }
            }
        }
    }
    
    public void replaceMerchant(final MerchantInventory inventory) {
        for (int i = 0; i < inventory.getMerchant().getRecipeCount(); ++i) {
            final MerchantRecipe recipe = inventory.getMerchant().getRecipe(i);
            final ItemStack itemStack = recipe.getResult();  
         	String customItem;
          	if (itemStack.getItemMeta().hasDisplayName() && itemStack.hasItemMeta()) {
          		customItem = CustomItemReplacer.getInstance().getConfigManager().getString(itemStack.getItemMeta().getDisplayName());
          	  if (customItem == null) {
          		customItem = CustomItemReplacer.getInstance().getConfigManager().getString(itemStack.getType().toString().toLowerCase());
          	  }
          	}
          	else {
          		customItem = CustomItemReplacer.getInstance().getConfigManager().getString(itemStack.getType().toString().toLowerCase());
          	}
            if (customItem != null) {
                if (CustomStack.isInRegistry(customItem)) {
                    final CustomStack customStack = CustomStack.getInstance(customItem);
                    final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)itemStack.getEnchantments();
                    final ItemStack finalItem = customStack.getItemStack();
                    finalItem.addUnsafeEnchantments((Map)enchantments);
                    finalItem.setAmount(itemStack.getAmount());
                    ItemMeta meta = finalItem.getItemMeta();
                    meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
                    finalItem.setItemMeta(meta);
                    final MerchantRecipe finalRecipe = new MerchantRecipe(finalItem, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier(), recipe.getDemand(), recipe.getSpecialPrice());
                    finalRecipe.setIngredients(recipe.getIngredients());
                    inventory.getMerchant().setRecipe(i, finalRecipe);
                }
            }
        }
    }
    
    public static CustomItemReplacer getInstance() {
        return CustomItemReplacer.instance;
    }
    
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}
