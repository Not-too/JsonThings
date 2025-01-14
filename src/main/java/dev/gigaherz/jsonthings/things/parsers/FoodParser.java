package dev.gigaherz.jsonthings.things.parsers;

import com.google.gson.JsonObject;
import dev.gigaherz.jsonthings.JsonThings;
import dev.gigaherz.jsonthings.things.ThingRegistries;
import dev.gigaherz.jsonthings.things.builders.FoodBuilder;
import dev.gigaherz.jsonthings.things.builders.MobEffectInstanceBuilder;
import dev.gigaherz.jsonthings.util.parse.JParse;
import dev.gigaherz.jsonthings.util.parse.value.ObjValue;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.function.Consumer;

public class FoodParser extends ThingParser<FoodBuilder>
{
    public FoodParser()
    {
        super(GSON, "food");
    }

    @Override
    protected void finishLoadingInternal()
    {
        getBuilders().forEach(thing -> Registry.register(ThingRegistries.FOODS, thing.getRegistryName(), thing.get()));
    }

    @Override
    public FoodBuilder processThing(ResourceLocation key, JsonObject data, Consumer<FoodBuilder> builderModification)
    {
        final FoodBuilder builder = FoodBuilder.begin(this, key);

        JParse.begin(data)
                .key("nutrition", val -> val.intValue().min(1).handle(builder::setNutrition))
                .key("saturation", val -> val.intValue().min(0).handle(builder::setSaturation))
                .ifKey("meat", val -> val.bool().handle(builder::setIsMeat))
                .ifKey("fast", val -> val.bool().handle(builder::setFast))
                .ifKey("always_eat", val -> val.bool().handle(builder::setAlwaysEat))
                .ifKey("effects", val -> val.array().forEach((i, entry) -> {
                    var probability = new MutableFloat(1.0f);
                    var ei = parseEffectInstance(entry.obj()
                            .ifKey("probability", v3 -> v3.floatValue().range(0, 1).handle(probability::setValue)), builder);
                    builder.effect(ei, probability.getValue());
                }));

        builderModification.accept(builder);

        return builder;
    }

    private MobEffectInstanceBuilder parseEffectInstance(ObjValue obj, FoodBuilder parentBuilder)
    {
        var builder = JsonThings.mobEffectInstanceParser.parseFromElement(parentBuilder.getRegistryName(), obj.getAsJsonObject());
        builder.setOwner(parentBuilder);
        return builder;
    }
}
