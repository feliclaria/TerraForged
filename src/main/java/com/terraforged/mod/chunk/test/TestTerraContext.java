package com.terraforged.mod.chunk.test;

import com.terraforged.world.GeneratorContext;
import com.terraforged.world.WorldGeneratorFactory;
import com.terraforged.mod.chunk.TerraContext;

public class TestTerraContext extends TerraContext {

    public TestTerraContext(TerraContext context) {
        super(context);
    }

    @Override
    protected WorldGeneratorFactory createFactory(GeneratorContext context) {
        return new WorldGeneratorFactory(context, new TestHeightMap(context));
    }
}
