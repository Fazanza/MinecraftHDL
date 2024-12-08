// package minecrafthdl;

// import minecrafthdl.client.render.blocks.BlockRenderRegister;
// import net.minecraftforge.fml.common.event.FMLInitializationEvent;
// import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
// import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// /**
//  * Created by Francis on 10/5/2016.
//  */
// public class ClientProxy extends CommonProxy {

//     @Override
//     public void preInit(FMLPreInitializationEvent e) {
//         super.preInit(e);
//     }

//     @Override
//     public void init(FMLInitializationEvent e) {
//         super.init(e);
//         BlockRenderRegister.registerBlockRenderer();
//     }

//     @Override
//     public void postInit(FMLPostInitializationEvent e) {
//         super.postInit(e);
//     }

// }

package minecrafthdl;

import minecrafthdl.client.render.blocks.BlockRenderRegister;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

    @Override
    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent e) {
        super.commonSetup(e);
        // Your common setup logic
    }

    @Override
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent e) {
        super.clientSetup(e);
        BlockRenderRegister.registerBlockRenderer();
        // Your client-specific setup logic
    }
}
