package minecrafthdl.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "minecraftHDL", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlocks {
    
    // Creating a Deferred Register for blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraftHDL");
    public static final DeferredRegister<net.minecraft.world.item.BlockItem> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraftHDL");

    // Registering a block using DeferredRegister
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(Block.Properties.of(Material.STONE)));

    // Registering a block's item equivalent
    public static final RegistryObject<net.minecraft.world.item.BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new net.minecraft.world.item.BlockItem(EXAMPLE_BLOCK.get(), new CreativeModeTab()));

    public static void register(IEventBus eventBus) {
        // Register blocks and items to the event bus so that they are registered
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
