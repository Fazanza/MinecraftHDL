package minecrafthdl.gui;

import minecrafthdl.block.blocks.Synthesizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Francis on 3/25/2017.
 */
public class SynthesiserGUI extends Screen {

    Button synthesize_button, up_button, down_button;
    int synth_b_id = 0;
    int up_b_id = 1;
    int down_b_id = 2;

    String file_directory = "./verilog_designs";

    ArrayList<String> file_names = new ArrayList<>();
    int selected_file = -1;

    int window_width = 256;
    int window_height = 256;

    int window_left, window_top, filebox_left, filebox_top, filebox_right, filebox_bottom;

    int line_height = 10;
    int padding = 2;
    int total_height = line_height + (2 * padding);

    int start_file_index = 0;

    int block_x, block_y, block_z;
    Level world;


    public SynthesiserGUI(Level world, int x, int y, int z) {
        super(new TextComponent("Synthesizer")); // This sets the title for the screen.
        this.world = world;
        this.block_x = x;
        this.block_y = y;
        this.block_z = z;
    }


    @Override
    protected void init() {
        this.window_left = centerObjectTL(this.window_width, this.width);
        this.window_top = centerObjectTL(this.window_height, this.height);

        this.filebox_left = window_left + 12;
        this.filebox_right = window_left + 150;
        this.filebox_top = window_top + 25;
        this.filebox_bottom = window_top + 130;

        // Create the synthesize button with a new action for button press
        this.synthesize_button = new Button(this.width / 2 - 50, this.height / 2 + 52, 100, 20, new TextComponent("Generate Design"), (button) -> {
            if (this.selected_file >= 0) {
                Synthesizer.file_to_gen = this.file_directory + "/" + this.file_names.get(this.selected_file);
                this.minecraft.setScreen(null); // Close the screen
            }
        });

        // Create up and down buttons
        this.up_button = new Button(this.filebox_right + 1, this.filebox_top - 1, 20, 20, new TextComponent("^"), (button) -> {
            if (this.start_file_index > 0) this.start_file_index -= 1;
        });

        this.down_button = new Button(this.filebox_right + 1, this.filebox_bottom - 19, 20, 20, new TextComponent("/"), (button) -> {
            if (this.start_file_index + 6 < this.file_names.size() - 1) this.start_file_index += 1;
        });

        // Add buttons to the button list
        this.addRenderableWidget(this.synthesize_button);
        this.addRenderableWidget(this.up_button);
        this.addRenderableWidget(this.down_button);

        // Disable the synthesize button initially
        this.synthesize_button.active = false;

        // Load file names
        this.file_names = this.readFileNames();
    }

    private ArrayList<String> readFileNames(){
        ArrayList<String> files = new ArrayList<>();
        File folder = new File(file_directory);

        if (!folder.exists()) {
            folder.mkdir();
            Minecraft.getInstance().player.sendMessage(new TextComponent("Created folder 'verilog_designs'"), Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().player.sendMessage(new TextComponent("Copy your synthesized JSON files to this directory: " + folder.getAbsolutePath()), Minecraft.getInstance().player.getUUID());
        } else {
            for (File f : folder.listFiles()){
                if (f.getName().toLowerCase().endsWith(".json")) {
                    files.add(f.getName());
                }
            }
        }
        return files;
    }

    private int centerObjectTL(int obj_dimension, int scrn_dimension){
        return (scrn_dimension / 2) - (obj_dimension / 3);
    }

    private int centerObjectBR(int obj_dimension, int scrn_dimension){
        return (scrn_dimension / 2) + (obj_dimension / 2);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        // Draw background texture
        RenderSystem.setShaderTexture(0, new ResourceLocation("minecrafthdl:textures/gui/synthesiser.png"));
        blit(poseStack, centerObjectTL(this.window_width, this.width), centerObjectTL(this.window_height, this.height), 0, 0, this.window_width, this.window_height);

        // Draw title
        this.font.draw(poseStack, new TextComponent("Synthesiser"), (this.width / 2) - (this.font.width("Synthesiser") / 2), (this.height / 2) - 75, 0xFF0000);

        // Draw file names list
        this.drawFileNames(poseStack);

        // Call the superclass render method with PoseStack and other parameters
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    private void drawFileNames(PoseStack poseStack){
        int current_height = this.filebox_top;
        int files_shown = 0;
        for (int i = this.start_file_index; i < this.file_names.size(); i++) {
            if (files_shown == 7) break;
            else  files_shown++;
            String file_name = this.file_names.get(i);
            int max_width = this.filebox_right - this.filebox_left - (2 * this.padding);
            if (this.font.width(file_name) > max_width) {
                file_name = this.font.plainSubstrByWidth(file_name, max_width - this.font.width("...")) + "...";
            }

            if (this.selected_file == i){
                fill(poseStack, this.filebox_left, current_height, this.filebox_right, current_height + this.total_height, 0xFFFFFFFF); // highlight selected file
                current_height += this.padding;

                this.font.draw(poseStack, file_name, this.filebox_left + this.padding, current_height, 0xFF0000);
                current_height += this.line_height + this.padding;
            } else {
                current_height += this.padding;
                this.font.draw(poseStack, file_name, this.filebox_left + this.padding, current_height, 0xFF0000);
                current_height += this.line_height + this.padding;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if(mouseX >= this.filebox_left && mouseX <= this.filebox_right && mouseY >= this.filebox_top && mouseY <= this.filebox_bottom) {
            int index = (int) ((mouseY - this.filebox_top + (this.start_file_index * this.line_height)) / this.total_height);
            if (index < this.file_names.size()){
                this.selected_file = index;
                this.synthesize_button.active = true;
            } else {
                this.selected_file = -1;
                this.synthesize_button.active = false;
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
