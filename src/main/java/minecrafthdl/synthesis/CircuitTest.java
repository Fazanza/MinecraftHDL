package minecrafthdl.synthesis;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

/**
 * Created by Francis on 10/28/2016.
 */
public class CircuitTest {
    ArrayList<ArrayList<ArrayList<BlockState>>> blocks;

    public CircuitTest(int sizeX, int sizeY, int sizeZ){
        this.blocks = new ArrayList<ArrayList<ArrayList<BlockState>>>();
        for (int x = 0; x < sizeX; x++) {
            this.blocks.add(new ArrayList<ArrayList<BlockState>>());
            for (int y = 0; y < sizeY; y++) {
                this.blocks.get(x).add(new ArrayList<BlockState>());
                for (int z = 0; z < sizeZ; z++) {
                    this.blocks.get(x).get(y).add(Blocks.AIR.defaultBlockState());  // Default block state is air
                }
            }
        }
    }

    public void setBlock(int x, int y, int z, BlockState blockState) {
        this.blocks.get(x).get(y).set(z, blockState);
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

    public BlockState getState(int x, int y, int z){
        return this.blocks.get(x).get(y).get(z);
    }
}
