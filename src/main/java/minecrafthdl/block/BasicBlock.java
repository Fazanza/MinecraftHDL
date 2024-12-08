package minecrafthdl.block;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.StateDefinition;

public class BasicBlock extends Block {

    public BasicBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(Properties.of(material)
                .strength(hardness, resistance)  // Replaces setHardness and setResistance
        );
        // For Minecraft 1.18 and above, setting creative tab is done via Item Group
        this.registerDefaultState(this.stateDefinition.any());  // Register the block state
    }

    public BasicBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.STONE, hardness, resistance);  // Default to Material.STONE
    }

    public BasicBlock(String unlocalizedName) {
        this(unlocalizedName, 2.0f, 10.0f);  // Default values for hardness and resistance
    }

    // Adding the creative tab equivalent in 1.18+
    @Override
    public void fillItemCategory(CreativeModeTab tab) {
        if (tab == CreativeModeTab.BUILDING_BLOCKS) {
            super.fillItemCategory(tab);
        }
    }
}
