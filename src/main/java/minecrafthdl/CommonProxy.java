// package minecrafthdl;

// import minecrafthdl.block.ModBlocks;
// import minecrafthdl.gui.MinecraftHDLGuiHandler;
// import net.minecraftforge.fml.common.event.FMLInitializationEvent;
// import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
// import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
// import net.minecraftforge.fml.common.network.NetworkRegistry;

// /**
//  * Created by Francis on 10/5/2016.
//  */
// public class CommonProxy {

//     public void preInit(FMLPreInitializationEvent e) {
//         ModBlocks.createBlocks();
//     }

//     public void init(FMLInitializationEvent e) {
//         NetworkRegistry.INSTANCE.registerGuiHandler(MinecraftHDL.instance, new MinecraftHDLGuiHandler());
//     }

//     public void postInit(FMLPostInitializationEvent e) {

//     }
// }

package minecrafthdl;

import minecrafthdl.block.ModBlocks;
import minecrafthdl.gui.MinecraftHDLGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;

/**
 * Created by Francis on 10/5/2016.
 */
public class CommonProxy {

    // This method will handle common setup logic
    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent e) {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        // Your common initialization logic
        ModBlocks.register(modEventBus);
    }

    // Client-side specific logic can be handled here
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent e) {
        // Any client-specific setup (optional for CommonProxy)
        MinecraftHDLGuiHandler.registerGui();    
    }

}
