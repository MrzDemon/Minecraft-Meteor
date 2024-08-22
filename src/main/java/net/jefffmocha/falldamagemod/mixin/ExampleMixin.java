package net.jefffmocha.falldamagemod.mixin;

import net.jefffmocha.falldamagemod.FallDamageMod;
import net.jefffmocha.falldamagemod.PreventFallDamageAccessor;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class ExampleMixin implements PreventFallDamageAccessor {

	private boolean preventFallDamage = false;

	@Override
	public boolean isPreventFallDamage() {
		return preventFallDamage;
	}

	@Override
	public void setPreventFallDamage(boolean preventFallDamage) {
		this.preventFallDamage = preventFallDamage;
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void modifyFallDamage(float fallDistance, float multiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if(fallDistance <=6) {
			cir.setReturnValue(false);
		}


		else if (preventFallDamage && FallDamageMod.countdownActive) {
			//if(fallDistance > 6 && fallDistance <= 12 ) {
				cir.setReturnValue(false); // Cancel fall damage
			//}
			//else fallDistance = (fallDistance-=12) /2;
		}

		//FallDamageMod.sauce = true;
	}
}


