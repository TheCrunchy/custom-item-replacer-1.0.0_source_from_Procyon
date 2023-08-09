// 
// Decompiled by Procyon v0.5.36
// 

package de.lburgard.replacer.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.block.BlockState;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import de.lburgard.replacer.CustomItemReplacer;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.Listener;

public class ChestListener implements Listener
{
    @EventHandler
    public void onOpenChest(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        final Block block = event.getClickedBlock();
        final BlockState state = block.getState();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItemReplacer.getInstance(), () -> {
			
        if (state instanceof Chest) {
            final Chest chest = (Chest)state;
            CustomItemReplacer.getInstance().replaceChest(chest.getInventory());
        }
    	}, (long) 5);
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
    	ItemStack itemStack = event.getCurrentItem();
      	if (itemStack.getItemMeta().getPersistentDataContainer().has(CustomItemReplacer.key, PersistentDataType.BOOLEAN)) {
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
                meta.getPersistentDataContainer().set(CustomItemReplacer.key, PersistentDataType.BOOLEAN, true);
                finalItem.setItemMeta(meta);
                itemStack = finalItem;
              
            }
        }
    }
}