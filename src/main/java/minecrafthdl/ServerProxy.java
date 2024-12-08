// /**
// package minecrafthdl;

// import net.minecraftforge.fml.common.event.FMLInitializationEvent;
// import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
// import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// /**
//  * Created by Francis on 10/5/2016.
//  */
// public class ServerProxy extends CommonProxy {

//     @Override
//     public void preInit(FMLPreInitializationEvent e) {
//         super.preInit(e);
//     }

//     @Override
//     public void init(FMLInitializationEvent e) {
//         super.init(e);
//     }

//     @Override
//     public void postInit(FMLPostInitializationEvent e) {
//         super.postInit(e);
//     }

// }
// */

package minecrafthdl;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
// import net.minecraftforge.eventbus.api.event.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
public class ServerProxy extends CommonProxy {

    // The commonSetup method for handling server-related setup tasks
    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        super.commonSetup(event);
    }

    // The clientSetup method for handling client-related setup tasks
    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        super.clientSetup(event);
    }
}

// package minecrafthdl;

// import net.minecraftforge.api.distmarker.Dist;
// import net.minecraftforge.eventbus.api.IEventBus;
// import net.minecraftforge.eventbus.api.SubscribeEvent;
// // import net.minecraftforge.eventbus.api.event.FMLCommonSetupEvent;
// import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
// import net.minecraftforge.eventbus.api.event.FMLPreInitializationEvent;
// import net.minecraftforge.fml.common.Mod;

// @Mod.EventBusSubscriber(modid = "minecrafthdl", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
// public class ServerProxy extends CommonProxy {

//     // This method replaces the previous preInit lifecycle event
//     @SubscribeEvent
//     public static void onCommonSetup(FMLCommonSetupEvent event) {
//         // Do server-side setup here
//         System.out.println("Server-side common setup");
//     }

//     @Override
//     public void preInit() {
//         // No longer needs the event, just handle setup here
//         super.preInit();
//     }

//     @Override
//     public void init() {
//         // Perform server-side initialization
//         super.init();
//     }

//     @Override
//     public void postInit() {
//         // Handle post-initialization tasks here
//         super.postInit();
//     }
// }
