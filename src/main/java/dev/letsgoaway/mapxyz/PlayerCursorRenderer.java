package dev.letsgoaway.mapxyz;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.io.File;
import javax.imageio.ImageIO;

@SuppressWarnings("deprecation")
public class PlayerCursorRenderer {

    private static final MapCursor.Type MARKER_TYPE = MapCursor.Type.BLUE_MARKER;
    private static final MapCursor.Type NETHER_MARKER_TYPE = MapCursor.Type.RED_MARKER;

    public static void render(Player viewer, Player player, MapCanvas canvas, MapView map) {
        if (viewer.equals(player)) {
            return;
        }

        int scaleFactor = 1 << map.getScale().getValue();
        float x = (float)(player.getLocation().getX() - (double)map.getCenterX()) / (float)scaleFactor;
        float z = (float)(player.getLocation().getZ() - (double)map.getCenterZ()) / (float)scaleFactor;

        byte d = calculateRotation(player);

        if (x < -62 || x > 62 || z < -62 || z > 62)
        {
            return;
        }


        // Nether marker type matches Bedrock Edition locator maps
        canvas.getCursors().addCursor(new MapCursor(clampMapCoordinate(x), clampMapCoordinate(z), d, map.getWorld().getEnvironment().equals(World.Environment.NETHER) ? NETHER_MARKER_TYPE : MARKER_TYPE, true, player.getName()));
        if (Config.playersonitemframes)
        {
            try
            {
                canvas.drawImage(Math.round(x) + 62, Math.round(z) + 62, ImageIO.read(MapXYZ.instance.getResource("test.png")));
            }
            catch(Exception e)
            {
                System.out.println("error");
            }
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if ((i == 0 || i == 2) && (j == 0 || j == 2))
                    {
                        canvas.setPixelColor(Math.round(x) + 62 + i, Math.round(z) + 62 + j, canvas.getBasePixelColor(Math.round(x) + 62 + i, Math.round(z) + 62 + j));
                    }
                }
            }
        }
    }

    private static byte clampMapCoordinate(float coordinate) {
        if (coordinate <= -63.0F) {
            return -128;
        } else {
            return coordinate >= 63.0F ? 127 : (byte)((int)((double)(coordinate * 2.0F) + 0.5));
        }
    }

    private static byte calculateRotation(Player player) {
        double yaw = player.getLocation().getYaw();
        boolean shouldFlip = 0.0 > yaw;
        World world = player.getLocation().getWorld();
        if (world != null && world.getEnvironment().equals(World.Environment.NETHER)) {
            // use world.getFullTime() if its the same player
            // but im going to use player.getTicksLived()
            // to make sure it looks different from your marker
            int i = (int) (player.getTicksLived() / 10L);
            return (byte) (i * i * 34187121 + i * 121 >> 15 & 15);
        } else {
            double adjusted = yaw < 0.0 ? yaw - 8.0 : yaw + 8.0;
            if (shouldFlip) {
                int i = 16 + ((int) (adjusted * 16.0 / 360.0));
                // todo figure out better way to fix
                if (i == 16) {
                    return (byte) 0;
                }
                return (byte) (i);
            } else {
                return (byte) ((int) (adjusted * 16.0 / 360.0));
            }
        }
    }

    public static void renderAll(Player viewer, MapCanvas canvas, MapView map) {
        for (int i = 0; i < canvas.getCursors().size(); i++) {
            MapCursor c = canvas.getCursors().getCursor(i);
            MapCursor.Type t = c.getType();

            if (t.equals(MARKER_TYPE) || t.equals(NETHER_MARKER_TYPE)) {
                canvas.getCursors().removeCursor(c);
            }

            if (Config.enableLocatorMaps && (t.equals(MapCursor.Type.PLAYER) || t.equals(MapCursor.Type.PLAYER_OFF_MAP) || t.equals(MapCursor.Type.PLAYER_OFF_LIMITS))){
                canvas.getCursors().removeCursor(c);
            }
        }

        if (!Config.enableLocatorMaps) {
            return;
        }

        World world = map.getWorld();
        if (world != null) {
            for (Player player : world.getPlayers()) {
                render(viewer, player, canvas, map);
            }
        }
    }
}
