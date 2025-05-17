package dev.hardaway.locksmith.core.data;

import dev.hardaway.locksmith.core.Locksmith;
import dev.hardaway.locksmith.core.registry.LocksmithParticles;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class LocksmithParticleDescriptionProvider extends ParticleDescriptionProvider {

    public LocksmithParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        this.spriteSet(LocksmithParticles.LOCK_BREAK.get(), Locksmith.path("lock_break"), 3, false);
        this.spriteSet(LocksmithParticles.LOCK_SPARK.get(), Locksmith.path("lock_spark"), 3, false);
    }
}
