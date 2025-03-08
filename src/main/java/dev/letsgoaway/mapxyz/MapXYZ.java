package dev.letsgoaway.mapxyz;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;

public final class MapXYZ extends JavaPlugin implements Listener {

    private static final PosRenderer rendererInstance = new PosRenderer(true);
    public static BananaTypeFont asciiFont;
    public static MapXYZ instance;

    public static void onConfigLoad() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, Config.enableReducedDebugInfo);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        Config.init();
        try {
            InputStream data = getResource("minecraft-font-ascii.btf");
            if (data != null)
                asciiFont = BananaTypeFont.from(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("mapxyz").setExecutor(new MapXYZCommand());
    }

    @Override
    public void onDisable() {
        if (Config.enableReducedDebugInfo) {
            // if the server owner stops the server,
            // theres a chance they uninstall this plugin or they change the config option,
            // so we will revert reduced debug info to the default of false
            for (World world : Bukkit.getWorlds()) {
                world.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        if (Config.enableReducedDebugInfo) {
            ev.getPlayer().getWorld().setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        }
        if (Config.enableStartingMap) {
            if (!ev.getPlayer().hasPlayedBefore()) {
                ev.getPlayer().getInventory().addItem(new ItemStack(Material.MAP, 1));
            }
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent ev) {
        if (Config.enableReducedDebugInfo) {
            ev.getFrom().setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
            ev.getPlayer().getWorld().setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        }
    }

    @EventHandler
    public void onMapInit(MapInitializeEvent event) {
        if (Config.useLegacyConsoleDefaultZoom) {
            event.getMap().setScale(MapView.Scale.FAR);
        }
        for (MapRenderer renderer : event.getMap().getRenderers()) {
            if (renderer instanceof PosRenderer) {
                return;
            }
        }
        event.getMap().addRenderer(rendererInstance);
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent ev) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            addRenderer(ev.getPlayer().getInventory().getItemInMainHand());
            addRenderer(ev.getPlayer().getInventory().getItemInOffHand());
        }, 1L);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent ev) {
        addRenderer(ev.getItem().getItemStack());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent ev) {
        addRenderer(ev.getCurrentItem());
        addRenderer(ev.getCursor());
    }

    private void addRenderer(ItemStack stack) {
        if (stack != null && stack.getType().equals(Material.FILLED_MAP)) {
            MapMeta meta = (MapMeta) stack.getItemMeta();
            if (meta == null) {
                return;
            }

            MapView view = meta.getMapView();
            if (view == null) {
                return;
            }

            for (MapRenderer renderer : view.getRenderers()) {
                if (renderer instanceof PosRenderer) {
                    return;
                }
            }
            view.addRenderer(rendererInstance);
        }
    }
}
