// package minecrafthdl.synthesis;

// import net.minecraft.world.entity.player.Player;
// import net.minecraft.core.BlockPos;
// import net.minecraft.network.chat.Component;
// import net.minecraft.network.chat.TextComponent;
// import net.minecraft.world.level.block.Blocks;
// import net.minecraft.world.level.block.state.BlockState;
// import net.minecraft.world.level.block.state.properties.BlockStateProperties;
// import net.minecraft.world.level.block.entity.SignBlockEntity;
// import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
// import net.minecraft.core.Direction;
// import net.minecraft.world.level.block.Block;
// import net.minecraft.world.level.block.state.BlockBehaviour;
// import net.minecraft.world.level.Level;
// import net.minecraft.world.level.block.entity.BlockEntity;
// import net.minecraft.core.Vec3i;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// public class LogicGates {

//     public static Gate Input(String id) {
//         // Create a Gate object, set initial values
//         Gate gate = new Gate(1, 2, 1, 1, 1, 0, 0, new int[]{0});
//         gate.is_io = true;

//         String[] id_txt = extractSignText(id);

//         SignBlockEntity tes = new SignBlockEntity(new BlockPos(0, 1, 0), Blocks.OAK_SIGN.defaultBlockState());
//         for (int i = 0; i < id_txt.length; i++) {
//             tes.setMessage(i, new TextComponent(id_txt[i]));
//         }

//         // Store the TileEntity in the gate's map
//         gate.te_map.put(new Vec3i(0, 1, 0), tes);

//         // Set the blocks at specified locations in the gate
//         gate.setBlock(0, 0, 0, Blocks.CALCITE.defaultBlockState());

//         // For the sign block, use the new BlockState system
//         BlockState signState = Blocks.OAK_SIGN.defaultBlockState().setValue(BlockStateProperties.rotation, 8);
//         gate.setBlock(0, 1, 0, signState);

//         return gate;
//     }

//     // Helper function to extract text from a sign ID string
//     public static String[] extractSignText(String id) {
//         String[] id_txt = new String[4];
//         id_txt[0] = id;
//         id_txt[1] = "Address";
//         id_txt[2] = "Unknown";
//         id_txt[3] = "Message";
//         return id_txt;
//     }

//     // Example Gate class for demonstration
//     public static class Gate {
//         int x, y, z;
//         boolean is_io;
//         int[] ids;
//         java.util.Map<Vec3i, BlockEntity> te_map = new java.util.HashMap<>();

//         public Gate(int x, int y, int z, int width, int height, int depth, int delay, int[] ids) {
//             this.x = x;
//             this.y = y;
//             this.z = z;
//             this.ids = ids;
//         }

//         // Sets the block at a specific position
//         public void setBlock(int x, int y, int z, BlockState blockState) {
//             // Logic for setting blocks (implementation might differ)
//             System.out.println("Setting block at (" + x + ", " + y + ", " + z + ")");
//         }

//         // Example of Vec3i for position handling
//         public static class Vec3i {
//             int x, y, z;
//             public Vec3i(int x, int y, int z) {
//                 this.x = x;
//                 this.y = y;
//                 this.z = z;
//             }
//         }
//     }

//     // Additional helper function or logic goes here...
// }

package minecrafthdl.synthesis;

import minecrafthdl.Demo;
import minecrafthdl.MHDLException;
import minecrafthdl.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
// import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;  
// import net.minecraft.util.text.ITextComponent;
// import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.chat.TextComponent;

/**
 * Created by Francis on 11/12/2016.
 */
public class LogicGates {

    public static void main(String[] args) {
        IntermediateCircuit ic = new IntermediateCircuit();
        ic.loadGraph(Demo.create4bitmuxgraph());
        ic.printLayers();
    }

    public static Gate Input(String id) {
        Gate gate = new Gate(1, 2, 1, 1, 1, 0, 0, new int[]{0});

        gate.is_io = true;
        String[] id_txt = extractSignText(id);

        SignBlockEntity tes = new SignBlockEntity(new BlockPos(0, 1, 0), Blocks.OAK_SIGN.defaultBlockState());
        for (int i = 0; i < id_txt.length; i++) {
            tes.setMessage(i, new TextComponent(id_txt[i]));
        }

        gate.te_map.put(new Vec3i(0, 1, 0), tes);  

        gate.setBlock(0, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
        gate.setBlock(0, 1, 0, Blocks.OAK_SIGN.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, 8));
        return gate;
    }

    public static Gate Output(String id) {
        Gate gate = new Gate(1, 2, 1, 1, 1, 0, 0, new int[]{0});

        gate.is_io = true;
        String[] id_txt = extractSignText(id);

        SignBlockEntity tes = new SignBlockEntity(new BlockPos(0, 1, 0), Blocks.OAK_SIGN.defaultBlockState());
        for (int i = 0; i < id_txt.length; i++) {
            tes.setMessage(i, new TextComponent(id_txt[i]));
        }

        gate.te_map.put(new Vec3i(0, 1, 0), tes);  // Using Minecraft's Vec3i

        gate.setBlock(0, 0, 0, Blocks.REDSTONE_LAMP.defaultBlockState());
        gate.setBlock(0, 1, 0, Blocks.OAK_SIGN.defaultBlockState().setValue(BlockStateProperties.ROTATION_16, 0));
        return gate;
    }

    private static String[] extractSignText(String id) {
        String[] txt = {"", "", ""};

        int i = 0;

        do {
            if (id.length() <= 15) {
                txt[i] += id;
                break;
            } else {
                String line = id.substring(0, 15);
                id = id.substring(15, id.length());
                txt[i] += line;
                i++;
            }

        } while (i < 3);

        return txt;
    }

    public static Gate NOT() {
        Gate gate = new Gate(1, 1, 3, 1, 1, 0, 0, new int[]{0});
        gate.setBlock(0, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
        gate.setBlock(0, 0, 1, Blocks.REDSTONE_TORCH.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH));
        gate.setBlock(0, 0, 2, Blocks.REDSTONE_WIRE.defaultBlockState());
        return gate;
    }

    public static Gate RELAY() {
        Gate gate = new Gate(1, 1, 3, 1, 1, 0, 0, new int[]{0});
        gate.setBlock(0, 0, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(0, 0, 1, Blocks.REPEATER.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));
        gate.setBlock(0, 0, 2, Blocks.REDSTONE_WIRE.defaultBlockState());
        return gate;
    }

    public static Gate AND(int inputs) {
        if (inputs == 0) throw new MHDLException("Gate cannot have 0 inputs");
        int width;
        if (inputs == 1) width = 1;
        else width = (inputs * 2) - 1;

        Gate gate = new Gate(width, 2, 4, inputs, 1, 1, 0, new int[]{0});

        gate.setBlock(0, 0, 2, Blocks.REDSTONE_TORCH.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH));
        gate.setBlock(0, 0, 3, Blocks.REDSTONE_WIRE.defaultBlockState());

        for (int i = 0; i < width; i += 2) {
            gate.setBlock(i, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
            gate.setBlock(i, 0, 1, Blocks.WHITE_WOOL.defaultBlockState());
            gate.setBlock(i, 1, 0, Blocks.REDSTONE_TORCH.defaultBlockState());
            gate.setBlock(i, 1, 1, Blocks.REDSTONE_WIRE.defaultBlockState());

            if (i != width - 1) {
                gate.setBlock(i + 1, 0, 1, Blocks.WHITE_WOOL.defaultBlockState());
                if (i == 14) {
                    gate.setBlock(i + 1, 1, 1, Blocks.REPEATER.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));
                } else {
                    gate.setBlock(i + 1, 1, 1, Blocks.REDSTONE_WIRE.defaultBlockState());
                }
            }
        }

        return gate;
    }

    public static Gate OR(int inputs) {
        if (inputs == 0) throw new MHDLException("Gate cannot have 0 inputs");
        int width;
        if (inputs == 1) width = 1;
        else width = (inputs * 2) - 1;

        Gate gate = new Gate(width, 2, 4, inputs, 1, 1, 0, new int[]{0});

        gate.setBlock(0, 0, 3, Blocks.REDSTONE_WIRE.defaultBlockState());

        for (int i = 0; i < width; i += 2) {
            gate.setBlock(i, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
            gate.setBlock(i, 0, 1, Blocks.REPEATER.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));
            gate.setBlock(i, 0, 2, Blocks.REDSTONE_WIRE.defaultBlockState());
            if (i != width - 1) {
                if (i == 14) {
                    gate.setBlock(i + 1, 0, 2, Blocks.REPEATER.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));
                } else {
                    gate.setBlock(i + 1, 0, 2, Blocks.REDSTONE_WIRE.defaultBlockState());
                }
            }
        }
        return gate;
    }

    public static Gate XOR() {
        Gate gate = new Gate(3, 2, 7, 2, 1, 1, 0, new int[]{0});

        gate.setBlock(0, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
        gate.setBlock(0, 0, 1, Blocks.REDSTONE_TORCH.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH));
        gate.setBlock(0, 0, 2, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(0, 0, 3, Blocks.WHITE_WOOL.defaultBlockState());
        gate.setBlock(0, 0, 4, Blocks.REDSTONE_TORCH.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH));
        gate.setBlock(0, 0, 5, Blocks.REDSTONE_WIRE.defaultBlockState());

        gate.setBlock(0, 1, 0, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(0, 1, 1, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(0, 1, 2, Blocks.REDSTONE_TORCH.defaultBlockState());

        gate.setBlock(0, 1, 4, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(0, 1, 5, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(0, 1, 6, Blocks.REDSTONE_WIRE.defaultBlockState());

        return gate;
    }

    public static Gate NAND() {
        Gate gate = new Gate(1, 1, 3, 1, 1, 0, 0, new int[]{0});
        gate.setBlock(0, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
        gate.setBlock(0, 0, 1, Blocks.REDSTONE_TORCH.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH));
        gate.setBlock(0, 0, 2, Blocks.REDSTONE_WIRE.defaultBlockState());
        return gate;
    }

    public static Gate MUX() {
        Gate gate = new Gate(5, 3, 7, 2, 1, 0, 0, new int[]{0});

        gate.setBlock(0, 0, 0, Blocks.WHITE_WOOL.defaultBlockState());
        gate.setBlock(0, 0, 1, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(1, 0, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(1, 0, 1, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(2, 0, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(2, 0, 1, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(3, 0, 0, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(3, 0, 1, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(4, 0, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(4, 0, 1, Blocks.REDSTONE_WIRE.defaultBlockState());

        gate.setBlock(0, 1, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(0, 1, 1, Blocks.REDSTONE_WIRE.defaultBlockState());

        gate.setBlock(0, 2, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(0, 2, 1, Blocks.REDSTONE_WIRE.defaultBlockState());

        return gate;
    }

    public static Gate LOW() {
        Gate gate = new Gate(1, 1, 1, 1, 0, 0, 0, new int[]{0});
        gate.setBlock(0, 0, 0, Blocks.REDSTONE_TORCH.defaultBlockState());
        return gate;
    }

    public static Gate HIGH() {
        Gate gate = new Gate(1, 1, 1, 1, 0, 0, 0, new int[]{0});
        gate.setBlock(0, 0, 0, Blocks.REDSTONE_BLOCK.defaultBlockState());
        return gate;
    }

    public static Gate D_LATCH() {
        Gate gate = new Gate(5, 2, 9, 1, 1, 0, 0, new int[]{0});

        gate.setBlock(0, 0, 0, Blocks.REDSTONE_BLOCK.defaultBlockState());
        gate.setBlock(1, 0, 0, Blocks.REDSTONE_BLOCK.defaultBlockState());
        gate.setBlock(2, 0, 0, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(2, 0, 1, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(3, 0, 0, Blocks.REDSTONE_BLOCK.defaultBlockState());
        gate.setBlock(3, 0, 1, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(4, 0, 0, Blocks.REDSTONE_WIRE.defaultBlockState());
        gate.setBlock(4, 0, 1, Blocks.REDSTONE_BLOCK.defaultBlockState());

        gate.setBlock(0, 1, 0, Blocks.REDSTONE_TORCH.defaultBlockState());
        gate.setBlock(1, 1, 0, Blocks.REDSTONE_TORCH.defaultBlockState());

        return gate;
    }
}

