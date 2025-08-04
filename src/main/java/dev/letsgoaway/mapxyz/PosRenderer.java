package dev.letsgoaway.mapxyz;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

public class PosRenderer extends MapRenderer {
    @SuppressWarnings("deprecation")
    private static final int color = MapPalette.matchColor(13, 13, 13);

    public PosRenderer(boolean contextual) {
        super(contextual);
    }

    private static boolean holdingItem(Player player, Material material) {
        return player.getInventory().getItemInMainHand().getType().equals(material) || player.getInventory().getItemInOffHand().getType().equals(material);
    }

    private static int getHeldMapID(ItemStack stack) {
        if (stack != null && stack.getType().equals(Material.FILLED_MAP)) {
            MapMeta meta = (MapMeta) stack.getItemMeta();
            if (meta == null) {
                return -1;
            }

            MapView view = meta.getMapView();
            if (view == null) {
                return -1;
            }
            return view.getId();
        }
        return -1;
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        int ymax = 10;
        if (Config.playersonitemframes)
        {
            ymax = 127;
        }
        for (int x = 0; x < 127; x++) {
            for (int y = 0; y < ymax; y++) {
                canvas.setPixelColor(x, y, canvas.getBasePixelColor(x, y));
            }
        }

        PlayerCursorRenderer.renderAll(player, canvas, map);

        if (!Config.enableXYZDisplay) {
            return;
        }

        /*
         this is for item frames, unfortunately the position is still visible
         if you are holding the same map of id in your hand but that's the best we can do atm
        */
        if (holdingItem(player, Material.FILLED_MAP)) {
            int handID = getHeldMapID(player.getInventory().getItemInMainHand());
            int offhandID = getHeldMapID(player.getInventory().getItemInOffHand());

            // error (returned because something was null)
            if (offhandID == -1 && handID == -1) {
                return;
            }

            // holding two maps
            if (handID != -1 && offhandID != -1) {
                // the map ID is completely different to the ones the player is holding
                if (map.getId() != handID && map.getId() != offhandID) {
                    return;
                }
            }

            // only holding a map in offhand
            if (handID == -1) {
                if (map.getId() != offhandID) {
                    return;
                }
            }

            // only holding a map in mainhand
            if (offhandID == -1) {
                if (map.getId() != handID) {
                    return;
                }
            }
        } else {
            return;
        }

        Location pos;
        if (Config.useEyeLevelPosition) {
            pos = player.getEyeLocation();
        } else {
            pos = player.getLocation();
        }
        canvas.drawText(0, 0, MapXYZ.asciiFont, "§" + color + ";X: " + pos.getBlockX() + ", Y: " + pos.getBlockY() + ", Z: " + pos.getBlockZ());
    }
}
