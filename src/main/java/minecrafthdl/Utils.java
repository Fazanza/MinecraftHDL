package minecrafthdl;

import minecrafthdl.synthesis.CircuitTest;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Created by Francis on 10/28/2016.
 */
public class Utils {

    public static Property<?> getPropertyByName(Block block, String name){
        BlockState blockState = block.defaultBlockState();
        for (Property<?> prop : blockState.getProperties()){
            if (prop.getName().equals(name)){
                return prop;
            }
        }
        return null;
    }

    public static void printProperties(Block block){
        BlockState blockState = block.defaultBlockState();
        for (Property<?> prop : blockState.getProperties()){
            System.out.println(prop.getName());
            System.out.println(prop.getPossibleValues());
        }
    }

    public static void printCircuit(CircuitTest circuit){
        for (int y = 0; y < circuit.getSizeY(); y++) {
            for (int x = 0; x < circuit.getSizeX(); x++) {
                for (int z = 0; z < circuit.getSizeZ(); z++) {
                    System.out.print(circuit.getState(x, y, z));
                }
                System.out.print("\n");
            }
            System.out.print("\n\n");
        }
    }
}
