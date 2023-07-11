package gg.moonflower.locksmith.clientsource.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class LockSparkParticle extends SimpleAnimatedParticle {

    public LockSparkParticle(ClientLevel clientLevel, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet, 0);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.quadSize *= 0.75F;
        this.lifetime = 20 + this.random.nextInt(10);
        this.setSpriteFromAge(spriteSet);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new LockSparkParticle(clientLevel, d, e, f, g, h, i, this.sprite);
        }
    }
}
