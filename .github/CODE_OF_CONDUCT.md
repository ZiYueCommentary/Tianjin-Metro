Hi there!

This code of conduct will tells you how to make your pull request be accepted. ~~(maybe?)~~

# Writing Javadoc

> [!IMPORTANT]
> If you don't want to read the whole rule down below: Adding `@author %your username%` to the classes' Javadoc, which you have modified.

It is crucial to write Javadoc to get the information about the class quickly, without spending time on reading codes.

_Basically_, Javadoc in class should always contain `@author`, `@since`, and `@see`.

* If you modified any class, please add an `@author` which with your GitHub username.
* Classes in `ziyue.tjmetro.mod` **should not** be Javadoc'd with `@author` and `@see`.
* Methods in functional classes (`IRailwaySign`, `GameRuleRegistry`, `IBlockExtension`, `IDrawingExtension`, `IGuiExtension`, and `RouteMapGenerator`) should be Javadoc'd, and append a short description of the method.
    * Overriding methods should add `@see` which linked to the method which is called. `@author` and `@since` is optional.
    * Functional classes (**not methods**) should not add `@author`.
* Classes for block shall always be Javadoc'd. `@see` should be linked to its block entity. If the superclass is located in `ziyue.tjmetro.mod.base`, add it to `@see` as well.
* Classes for block entity shall add `@see` which linked to its renderer. If there is a screen for it, link it too.
* Classes for screen shall add `@see` which linked to its open screen packet and update packet.
* Classes for open screen packets shall add `@see` which linked to its screen.
* Classes for update packets shall add `@see` which linked to its block entity.
* Classes for renderer shall add `@see` which linked to blocks which use it.
* Methods in `DynamicTextureCache` **should not** be Javadoc'd.
* If a class is similar to classes in MTR mod, please add `@see` which linked to the MTR class.
* Classes for mixin shall add `@see` which linked to the mixined class.
    * Mixin, which targeted Minecraft classes, can ignore this rule.

# Writing Mixins

Name your mixin with "`%TargetClassName%Mixin`". For example, the target is `org.mtr.mod.client.DynamicTextureCache`, the mixin class is `DynamicTextureCacheMixin`.

If you are trying to make some `private` methods being invokable by Mixin, the rule on the previous paragraph can be ignored. Now you shall name your mixin with "`%TargetClassOrMethodName%Accessor`". For example, the target is `net.minecraft.world.level.GameRules.Type<GameRules.BooleanValue>#create()`, the mixin class is `GameRuleBooleanAccessor`.

Methods in mixin class should always be `private`, excepted methods with `@Invoker`. Besides, all methods should be `static` except it is against Java grammar.

## Mixining methods

When using `@Inject` or `@Redirect`, the order of parameters should be: `at`, `method`, `cancellable`, and `remap`.

If `at` is `HEAD`, you should name the method with `before%MethodName%`. If `at` is `TAIL` or `RETURN`, you should name the method with `after%MethodName%`. The following code block is an example.

```java
@Inject(at = @At("HEAD"), method = "methodName")
private static void beforeMethodName(...) {
    // Your code here
}
```
