# Tactical Infiltration Action
Simple top-down wave based shooter, envisioned as a stealth game, however, due to both budget and time constraints the guards have already spotted you (tbf they're orange due to following a strict carrot-based diet, which is 100% the lore reasoning behind their inhuman eyesight), so good luck surviving it. Originaly intended only as an university project, but I might still add/update a few things, no promises tho.

Everything necessary to run it should be included, nevertheless, textures and maps were packed/made using some applications external to Java/LibGDX, manly TexturePacker and Tiled(map).

As mentioned, the project uses LibGDX, so it's highly advisable to use their [wiki](https://libgdx.com/wiki/) as a guide.

# Rundown
![<insert metal gear joke here>](menu-painted.png "<insert metal gear joke here>")

Contains six main features:
  1. Top-down controls with aim in eight directions (might be a bit wonky on bigger screens, can be ajusted through code);
  2. Tiled map with custom made collision. Please, if possbile, use Box2d, it will save you a lot of trouble;
  3. Separate screens for each "game status" (menu, loading, and game);
  4. Enemy bots (no high spectations, they're kinda dumb so be nice to them);
  5. Fully functional hud (healthbar and points), not made with best practices in mind. If you're looking for anything more advanced, using [GlyphLayout](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/g2d/GlyphLayout.html) 
  is highly advisable.
  6. A (mildly) funny jumping title.

You can check out a gameplay demonstration [here](https://www.youtube.com/watch?v=T-C4wldxsbU).

If, for whatever reason, you intend to play it without running the code, the .jar can be downloaded [here](https://drive.google.com/file/d/1E1hkhwQ0f1FVBrc8avHkuKjNzwywOrOH/view).

# Structure
Most of the aforementioned features are divided between three files each: An entity, a controller, and a processor (if they receive inputs from the player). 
There are also some util files for collison, a very handy direction enum, and some custom implementations of things Scene2D can do better for you 
(wish I knew it existed), such as the Stage and World classes.

If you pay close atention it's noticeable I inteded to implement multiple stages, this ideia was dropped due to deadlines, and at time of writing I still haven't
implemented it. The relation Game(screen)-World-Stage is where this was suppossed to be done, meaning that we would have many different implementations of the Stage
class. If you're interested, feel free to modify it.

# Conclusion
Most if not all of the code documentation was written in portuguese, however, english was used for everything else, so it should be intelligible if you made it here.

Have fun digging through it, I'm sure you're pretty good ;)
