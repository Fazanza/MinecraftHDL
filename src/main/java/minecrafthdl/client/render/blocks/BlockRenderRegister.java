package minecrafthdl.client.render.blocks;

import minecrafthdl.MinecraftHDL;
import minecrafthdl.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;

/**
 * Created by Francis on 10/28/2016.
 */
public class BlockRenderRegister {
    public static void registerBlockRenderer() {
        reg(ModBlocks.synthesizer);
    }

    public static void reg(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(MinecraftHDL.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
