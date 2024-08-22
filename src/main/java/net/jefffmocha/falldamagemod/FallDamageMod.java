package net.jefffmocha.falldamagemod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;


public class FallDamageMod implements ModInitializer {
	public static final String MOD_ID = "fall-damage-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static KeyBinding customKeyMapping;
	private static long lastShiftPressTime = 0;
	public static boolean countdownActive = false;
	private static long lastSauceLoss = 0;
	public static boolean sauceCountdownActive = false;
	public static boolean sauce = true;
	private final int SAUCE_COOLDOWN_LENGTH = 740;
	private int sauce_incrementor = 0;
	private int cooldown_stage = 15;

	private static final Identifier COOLDOWN0 = new Identifier(MOD_ID, "textures/gui/0.png");
	private static final Identifier COOLDOWN1 = new Identifier(MOD_ID, "textures/gui/1.png");
	private static final Identifier COOLDOWN2 = new Identifier(MOD_ID, "textures/gui/2.png");
	private static final Identifier COOLDOWN3 = new Identifier(MOD_ID, "textures/gui/3.png");
	private static final Identifier COOLDOWN4 = new Identifier(MOD_ID, "textures/gui/4.png");
	private static final Identifier COOLDOWN5 = new Identifier(MOD_ID, "textures/gui/5.png");
	private static final Identifier COOLDOWN6 = new Identifier(MOD_ID, "textures/gui/6.png");
	private static final Identifier COOLDOWN7 = new Identifier(MOD_ID, "textures/gui/7.png");
	private static final Identifier COOLDOWN8 = new Identifier(MOD_ID, "textures/gui/8.png");
	private static final Identifier COOLDOWN9 = new Identifier(MOD_ID, "textures/gui/9.png");
	private static final Identifier COOLDOWN10 = new Identifier(MOD_ID, "textures/gui/10.png");
	private static final Identifier COOLDOWN11 = new Identifier(MOD_ID, "textures/gui/11.png");
	private static final Identifier COOLDOWN12 = new Identifier(MOD_ID, "textures/gui/12.png");
	private static final Identifier COOLDOWN13 = new Identifier(MOD_ID, "textures/gui/13.png");
	private static final Identifier COOLDOWN14 = new Identifier(MOD_ID, "textures/gui/14.png");
	private static final Identifier COOLDOWN15 = new Identifier(MOD_ID, "textures/gui/15.png");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");



		HudRenderCallback.EVENT.register((context, tickDelta) -> {
			renderCustomHUD(context, 0, 0, 0);
		});
		
		
		// Register keybind
		customKeyMapping = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Negate Fall Damage",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_SHIFT,
				"Fall Damage Mod"
		));

		// Register the client tick event to detect key presses
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (customKeyMapping.isPressed() && !countdownActive && sauce) {
				// Send a packet to the server to notify the key press
				lastShiftPressTime = System.currentTimeMillis();
				ModNetwork.sendFallDamagePacket();
				//client.player.sendMessage(Text.literal("Custom key pressed!"), false);
				countdownActive = true;
				cooldown_stage = 0;
			}

			else if (customKeyMapping.isPressed() && !countdownActive) {
				// Send a packet to the server to notify the key press
				//lastShiftPressTime = System.currentTimeMillis();
				//ModNetwork.sendFallDamagePacket();
				//client.player.sendMessage(Text.literal("Custom key pressed!"), false);
				//countdownActive = true;
				cooldown_stage = -1;
			}



			if (countdownActive) {
				long currentTime = System.currentTimeMillis();
				if (currentTime - lastShiftPressTime >= 100) {
					//client.player.sendMessage(Text.literal("countdown over"), false);
					//countdownActive = false;  // Reset countdown
					countdownActive = false;
					sauce = false;
					lastSauceLoss = System.currentTimeMillis();
					sauceCountdownActive = true;
				}
			}

			if (sauceCountdownActive) {
				long currentTime = System.currentTimeMillis();
				if(!customKeyMapping.isPressed()) cooldown_stage++;
				else lastSauceLoss = System.currentTimeMillis();


				if (currentTime - lastSauceLoss >= SAUCE_COOLDOWN_LENGTH) {
					//client.player.sendMessage(Text.literal("sauce is back"), false);
					//countdownActive = false;  // Reset countdown
					sauce = true;
					sauceCountdownActive = false;
					if(!customKeyMapping.isPressed()) cooldown_stage = 15;
					sauce_incrementor = 0;
				}
			}
		});

		// Register the server receiver for packets
		ModNetwork.registerServerReceiver();
	}

	public void renderCustomHUD(DrawContext context, int mouseX, int mouseY, float delta) {

		int cooldownY = context.getScaledWindowHeight() - 32;
		MinecraftClient client = MinecraftClient.getInstance();
		int guiScale = client.options.getGuiScale().getValue();
		int cooldownX = (context.getScaledWindowWidth()/2) + 88;
		// Ensure the texture is bound
		context.getMatrices().push();

		//if(sauceCountdownActive) context.drawTexture(FALLING_COOLDOWN, 10, 10, 0,0,32,32, 32, 32);
		//else context.drawTexture(WALKING_COOLDOWN, 10, 10, 0,0,32,32, 32, 32);

		if(cooldown_stage <= 0)context.drawTexture(COOLDOWN0, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 1)context.drawTexture(COOLDOWN1, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 2)context.drawTexture(COOLDOWN2, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 3)context.drawTexture(COOLDOWN3, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 4)context.drawTexture(COOLDOWN4, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 5)context.drawTexture(COOLDOWN5, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 6)context.drawTexture(COOLDOWN6, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 7)context.drawTexture(COOLDOWN7, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 8)context.drawTexture(COOLDOWN8, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 9)context.drawTexture(COOLDOWN9, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 10)context.drawTexture(COOLDOWN10, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 11)context.drawTexture(COOLDOWN11, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 12)context.drawTexture(COOLDOWN12, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 13)context.drawTexture(COOLDOWN13, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 14)context.drawTexture(COOLDOWN14, cooldownX, cooldownY, 0,0,32,32, 32, 32);
		if(cooldown_stage == 15)context.drawTexture(COOLDOWN15, cooldownX, cooldownY, 0,0,32,32, 32, 32);







		context.getMatrices().pop();
	}
}
