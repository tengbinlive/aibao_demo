package com.mytian.lb.helper;

import com.mytian.lb.imp.ECallOnClick;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.AnimationsSet;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Axis;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;
import su.levenetc.android.textsurface.contants.TYPE;
import su.levenetc.android.textsurface.interfaces.IEndListener;
import su.levenetc.android.textsurface.interfaces.ISurfaceAnimation;

/**
 * Created by Eugene Levenetc.
 */
public class ShapeRevealSample {

    private static int fontSize = 19;
    private static int fontColor = 0x3b3733;

    public static void play(TextSurface textSurface, final ECallOnClick onClick) {

        Text textA = TextBuilder.create("Never give up,").setColor(fontColor).setSize(fontSize).setPosition(Align.SURFACE_CENTER).build();
        Text textB = TextBuilder.create("Never lose hope.").setColor(fontColor).setSize(fontSize).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textA).build();
        Text textC = TextBuilder.create("Always have faith,").setColor(fontColor).setSize(fontSize).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textB).build();
        Text textD = TextBuilder.create("It allows you to cope.").setColor(fontColor).setSize(fontSize).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textC).build();
        Text textE = TextBuilder.create("                                                 --amandamal Amanda").setColor(fontColor).setSize(fontSize).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textD).build();

        final int flash = 2000;

        ISurfaceAnimation animation = new ISurfaceAnimation() {
            @Override
            public void onStart() {
            }

            @Override
            public void start(IEndListener iEndListener) {
                if (null != onClick) {
                    onClick.callOnClick();
                }
            }

            @Override
            public void setTextSurface(TextSurface textSurface) {

            }

            @Override
            public long getDuration() {
                return 0;
            }

            @Override
            public void cancel() {

            }
        };

        textSurface.play(TYPE.SEQUENTIAL,
                Rotate3D.showFromCenter(textA, 500, Direction.CLOCK, Axis.X),
                new AnimationsSet(TYPE.PARALLEL,
                        ShapeReveal.create(textA, flash, SideCut.hide(Side.LEFT), false),
                        new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(flash / 5), ShapeReveal.create(textA, flash, SideCut.show(Side.LEFT), false))
                ),
                new AnimationsSet(TYPE.PARALLEL,
                        Rotate3D.showFromSide(textB, 800, Pivot.TOP),
                        new TransSurface(500, textB, Pivot.CENTER)
                ),
                Delay.duration(500),
                new AnimationsSet(TYPE.PARALLEL,
                        Rotate3D.showFromSide(textC, 800, Pivot.TOP),
                        new TransSurface(1000, textC, Pivot.CENTER)
                ),
                Delay.duration(500),
                new AnimationsSet(TYPE.PARALLEL,
                        Rotate3D.showFromSide(textD, 800, Pivot.TOP),
                        new TransSurface(1500, textD, Pivot.CENTER)
                ),
                Delay.duration(500),
                new AnimationsSet(TYPE.PARALLEL,
                        ShapeReveal.create(textE, 1000, Circle.show(Side.CENTER, Direction.OUT), false),
                        new TransSurface(2000, textE, Pivot.CENTER)
                ),
                Delay.duration(2000),
                new AnimationsSet(TYPE.PARALLEL,
                        new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textE, 700), ShapeReveal.create(textE, 1000, SideCut.hide(Side.LEFT), true)),
                        new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textD, 700), ShapeReveal.create(textD, 1000, SideCut.hide(Side.LEFT), true))),
                        new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textC, 700), ShapeReveal.create(textC, 1000, SideCut.hide(Side.LEFT), true))),
                        new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textB, 700), ShapeReveal.create(textB, 1000, SideCut.hide(Side.LEFT), true))),
                        new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(2000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(textA, 700), ShapeReveal.create(textA, 1000, SideCut.hide(Side.LEFT), true))),
                        new TransSurface(4000, textA, Pivot.CENTER)
                ),
                animation
        );
    }
}
