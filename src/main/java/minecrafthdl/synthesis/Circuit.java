package minecrafthdl.synthesis;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
// import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Circuit class to handle a 3D block structure, allowing placement and interaction of blocks in Minecraft world.
 */
public class Circuit {

    public static boolean TEST = false;

    ArrayList<ArrayList<ArrayList<BlockState>>> blocks;
    HashMap<Vec3i, BlockEntity> te_map = new HashMap<>();

    public Circuit(int sizeX, int sizeY, int sizeZ) {
        this.blocks = new ArrayList<>();
        for (int x = 0; x < sizeX; x++) {
            this.blocks.add(new ArrayList<>());
            for (int y = 0; y < sizeY; y++) {
                this.blocks.get(x).add(new ArrayList<>());
                for (int z = 0; z < sizeZ; z++) {
                    if (!TEST) this.blocks.get(x).get(y).add(Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    public void setBlock(int x, int y, int z, BlockState blockstate) {
        if (TEST) return;
        this.blocks.get(x).get(y).set(z, blockstate);
    }

    public void placeInWorld(Level worldIn, BlockPos pos, Direction direction) {
        int width = blocks.size();
        int height = blocks.get(0).size();
        int length = blocks.get(0).get(0).size();

        int start_x = pos.getX();
        int start_y = pos.getY();
        int start_z = pos.getZ();

        if (direction == Direction.NORTH) {
            start_z += 2;
        } else if (direction == Direction.SOUTH) {
            start_z -= length + 1;
        } else if (direction == Direction.EAST) {
            start_x -= width + 1;
        } else if (direction == Direction.WEST) {
            start_x -= width + 1;
        }

        int y = start_y - 1;
        for (int z = start_z - 1; z < start_z + length + 1; z++) {
            for (int x = start_x - 1; x < start_x + width + 1; x++) {
                worldIn.setBlock(new BlockPos(x, y, z), Blocks.STONE_BRICKS.defaultBlockState(), 3);
            }
        }

        HashMap<Vec3i, BlockState> torches = new HashMap<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < length; k++) {
                    if (this.getState(i, j, k).getBlock() == Blocks.REDSTONE_TORCH) {
                        torches.put(new Vec3i(i, j, k), this.getState(i, j, k));
                    } else {
                        BlockPos blk_pos = new BlockPos(start_x + i, start_y + j, start_z + k);
                        worldIn.setBlock(blk_pos, this.getState(i, j, k), 3);

                        BlockEntity te = this.te_map.get(new Vec3i(i, j, k));
                        if (te != null) {
                            worldIn.setBlockEntity(te);
                        }
                    }
                }
            }
        }

        for (Map.Entry<Vec3i, BlockState> set : torches.entrySet()) {
            worldIn.setBlock(new BlockPos(start_x + set.getKey().getX(), start_y + set.getKey().getY(), start_z + set.getKey().getZ()), set.getValue(), 3);
        }
    }

    public int getSizeX() {
        return this.blocks.size();
    }

    public int getSizeY() {
        return this.blocks.get(0).size();
    }

    public int getSizeZ() {
        return this.blocks.get(0).get(0).size();
    }

    public BlockState getState(int x, int y, int z) {
        return this.blocks.get(x).get(y).get(z);
    }

    public void insertCircuit(int x_offset, int y_offset, int z_offset, Circuit c) {
        for (int x = 0; x < c.getSizeX(); x++) {
            for (int y = 0; y < c.getSizeY(); y++) {
                for (int z = 0; z < c.getSizeZ(); z++) {
                    this.setBlock(x + x_offset, y + y_offset, z + z_offset, c.getState(x, y, z));

                    BlockEntity te = c.te_map.get(new Vec3i(x, y, z));
                    if (te != null) {
                        this.te_map.put(new Vec3i(x + x_offset, y + y_offset, z + z_offset), te);
                    }
                }
            }
        }
    }
}
