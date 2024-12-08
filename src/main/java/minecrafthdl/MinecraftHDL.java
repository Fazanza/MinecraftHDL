package minecrafthdl;

// import net.minecraft.world.item.Items; // Updated import
import net.minecraft.world.item.ItemStack; // Updated import
import net.minecraftforge.fml.common.Mod; // Updated import
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus; // New import for event bus
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(MinecraftHDL.MODID)
public class MinecraftHDL {
    public static final String MODID = "minecrafthdl";
    public static final String MODNAME = "Minecraft HDL";
    public static final String VERSION = "1.0";

    // Removed SidedProxy
    // Use event bus to register the commonSetup and clientSetup methods for modloading
    public MinecraftHDL() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        
        // Register common setup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        // Register the client setup method for modloading
        modEventBus.addListener(this::clientSetup);
        
        // Register the mod to the event bus so that the mod can handle events
        MinecraftForge.EVENT_BUS.register(this);
    }

    // This runs during commonSetup
    private void commonSetup(final FMLCommonSetupEvent event) {
        System.out.println("Common setup for MinecraftHDL");
    }

    // This runs during client setup (client-side only)
    private void clientSetup(final FMLClientSetupEvent event) {
        System.out.println("Client setup for MinecraftHDL");
    }

    // Example event handler for mod setup (equivalent of preInit in old Forge)
    @SubscribeEvent
    public void onModSetup(FMLCommonSetupEvent event) {
        System.out.println("This is the mod setup");
    }

    // Example event handler for post-initialization (equivalent of postInit)
    @SubscribeEvent
    public void onPostSetup(FMLClientSetupEvent event) {
        System.out.println("This is the client setup");
    }
}
