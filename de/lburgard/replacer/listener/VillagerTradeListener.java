// 
// Decompiled by Procyon v0.5.36
// 

package de.lburgard.replacer.listener;

import org.bukkit.event.EventHandler;
import de.lburgard.replacer.CustomItemReplacer;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.Listener;

public class VillagerTradeListener implements Listener
{
    @EventHandler
    public void onVillagerTrade(final InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        if (event.getView().getType() != InventoryType.MERCHANT) {
            return;
        }
        final MerchantInventory inventory = (MerchantInventory)event.getInventory();
        CustomItemReplacer.getInstance().replaceMerchant(inventory);
    }
}
