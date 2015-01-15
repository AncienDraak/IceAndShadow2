package iceandshadow2.nyx.forge;

import iceandshadow2.IaSFlags;
import iceandshadow2.ias.interfaces.IIaSKeepOnDeath;
import iceandshadow2.nyx.items.NyxItemBoneSanctified;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class NyxDeathSystem {

	public static InventoryPlayer plai_inv;
	public static HashMap<Integer, InventoryPlayer> death_inv;

	public static InventoryPlayer determineRespawnInventory(
			InventoryPlayer plai_inv, boolean do_drop) {
		boolean drop_main = true;
		for (int i = 0; i < plai_inv.mainInventory.length; ++i) {
			if (plai_inv.mainInventory[i] != null) {
				final ItemStack is = plai_inv.mainInventory[i];
				if (is.getItem() instanceof NyxItemBoneSanctified
						&& is.isItemDamaged())
					drop_main = false;
			}
		}
		if (drop_main) {
			for (int i = 0; i < plai_inv.mainInventory.length; ++i) {
				if (plai_inv.mainInventory[i] != null) {
					final Item it = plai_inv.mainInventory[i].getItem();
					if (it instanceof ItemTool || it instanceof ItemSword) {
						if (i < 9)
							continue;
					}
					if (it instanceof IIaSKeepOnDeath)
						continue;
					if (do_drop)
						plai_inv.player.dropPlayerItemWithRandomChoice(
								plai_inv.mainInventory[i], true);
					plai_inv.mainInventory[i] = null;
				}
			}
		}
		return plai_inv;
	}

	public NyxDeathSystem() {
		death_inv = new HashMap<Integer, InventoryPlayer>();
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		if (e.entity instanceof EntityPlayerMP) {
			final EntityPlayerMP playNoMore = (EntityPlayerMP) e.entityLiving;
			plai_inv = new InventoryPlayer(playNoMore);
			plai_inv.copyInventory(playNoMore.inventory);
		}
	}

	@SubscribeEvent
	public void onDrop(PlayerDropsEvent e) {
		final boolean gr = e.entityPlayer.worldObj.getGameRules()
				.getGameRuleBooleanValue("keepInventory");
		if (!e.entityPlayer.worldObj.isRemote
				&& e.entityPlayer.dimension == IaSFlags.dim_nyx_id && !gr) {
			System.out.println(e.entityPlayer.getEntityId());
			e.setCanceled(true);
			final boolean drop_main = true;
			plai_inv = determineRespawnInventory(plai_inv, true);
			e.entityPlayer.inventory.copyInventory(plai_inv);
			death_inv.put(e.entityPlayer.getEntityId(),
					e.entityPlayer.inventory);
		}
	}

	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent e) {
		final InventoryPlayer inv = new InventoryPlayer(e.player);
		if (e.player.isDead)
			inv.copyInventory(determineRespawnInventory(e.player.inventory,
					false));
		else
			inv.copyInventory(e.player.inventory);
		death_inv.put(e.player.getEntityId(), inv);

	}

	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent e) {
		death_inv.remove(e.player.getEntityId());
	}

	@SubscribeEvent
	public void onRespawn(PlayerEvent.Clone e) {
		final boolean gr = e.entityPlayer.worldObj.getGameRules()
				.getGameRuleBooleanValue("keepInventory");
		if (!e.entityPlayer.worldObj.isRemote
				&& e.entityPlayer.dimension == IaSFlags.dim_nyx_id && !gr) {
			if (!e.original.isDead)
				return;
			if (death_inv.get(e.original.getEntityId()) != null)
				e.entityPlayer.inventory.copyInventory(death_inv.get(e.original
						.getEntityId()));
			// Raise madness.
		}
	}
}
