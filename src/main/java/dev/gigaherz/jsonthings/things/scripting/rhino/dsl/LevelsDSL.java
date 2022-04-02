package dev.gigaherz.jsonthings.things.scripting.rhino.dsl;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;

public class LevelsDSL
{
    public static void use(Context cx, Scriptable scope)
    {
        if (scope.has(".use_levels", scope))
            return;

        scope.put(".use_levels", scope, true);
    }
}