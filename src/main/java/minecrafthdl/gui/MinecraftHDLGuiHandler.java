package minecrafthdl.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.PlayMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.MenuProvider;


public class MinecraftHDLGuiHandler {

    // Ensure the GUI ID is defined
    public static final int SYNTHESIZER_GUI_ID = 0;

    /**
     * Opens the Synthesizer GUI on the client side.
     */
    public static void openSynthesiserGui(Level world, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SynthesiserGUI(world, pos.getX(), pos.getY(), pos.getZ()));
    }

    /**
     * Opens the Synthesiser GUI on the server side.
     */
    public static void openSynthesiserGuiServer(Player player, BlockPos pos) {
        if (!player.level.isClientSide) {
            NetworkHooks.openScreen(
                (ServerPlayer) player, // Server player
                new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.literal("Synthesizer GUI");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, InventoryMenu inv, Player player) {
                        return new SynthesiserContainer(id, inv, pos); // Replace with your container
                    }
                },
                pos
            );
        }
    }

    /**
     * Register the Synthesizer GUI handler with the given GUI ID.
     */
    public static void registerGui() {
        MenuScreens.register(SYNTHESIZER_GUI_ID, MinecraftHDLGuiHandler::openSynthesiserGui);
    }
}
