package minecrafthdl.block.blocks;

import minecrafthdl.MHDLException;
import minecrafthdl.MinecraftHDL;
import minecrafthdl.block.BasicBlock;
import minecrafthdl.gui.MinecraftHDLGuiHandler;
import minecrafthdl.synthesis.Circuit;
import minecrafthdl.synthesis.IntermediateCircuit;
import minecrafthdl.synthesis.LogicGates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.Random;

public class Synthesizer extends BasicBlock {

    public static String file_to_gen;
    public static int check_threshold = 100;

    public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

    private int check_counter = 0;
    private boolean to_check = false;
    private Circuit c_check = null;
    private BlockPos p_check = null;

    public Synthesizer(String unlocalizedName) {
        super(unlocalizedName);
        this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, ServerPlayer player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            NetworkHooks.openScreen(player, new MinecraftHDLGuiHandler(), pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!world.isClientSide) {
            boolean isPowered = world.hasNeighborSignal(pos);

            if (isPowered && !state.getValue(TRIGGERED)) {
                world.setBlock(pos, state.setValue(TRIGGERED, true), 3);

                if (file_to_gen != null) {
                    synthGen(world, pos);
                }
            } else if (!isPowered) {
                world.setBlock(pos, state.setValue(TRIGGERED, false), 3);
            }
        }
    }

    private void synthGen(Level world, BlockPos pos) {
        try {
            IntermediateCircuit ic = new IntermediateCircuit();
            ic.loadGraph(GraphBuilder.buildGraph(file_to_gen));
            ic.buildGates();
            ic.routeChannels();
            this.c_check = ic.genCircuit();
            c_check.placeInWorld(world, pos, Direction.NORTH);
            this.to_check = true;
            this.p_check = pos;

        } catch (Exception e) {
            if (world.isClientSide) {
                Minecraft.getInstance().player.sendMessage(
                    Component.literal("An error occurred while generating the circuit, check the logs! Sorry!"), 
                    Minecraft.getInstance().player.getUUID()
                );
            }
            e.printStackTrace();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state) {
        return this.defaultBlockState().setValue(TRIGGERED, false);
    }
}
