package com.mytian.lb.helper;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

/**
 * Created by Eugene Levenetc.
 */
public class CookieThumperSample {

	public static void play(TextSurface textSurface, AssetManager assetManager) {

		final Typeface robotoBlack = Typeface.createFromAsset(assetManager, "fonts/Roboto-Black.ttf");
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(robotoBlack);

		Text textDaai = TextBuilder
				.create("土豆 土豆")
				.setPaint(paint)
				.setSize(30)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.SURFACE_CENTER).build();

		Text textBraAnies = TextBuilder
				.create("我是地瓜，我是地瓜")
				.setPaint(paint)
				.setSize(20)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.BOTTOM_OF, textDaai).build();

		Text textFokkenGamBra = TextBuilder
				.create(" 听到请回答，听到请回答")
				.setPaint(paint)
				.setSize(24)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.RIGHT_OF, textBraAnies).build();

		Text textHaai = TextBuilder
				.create(" .............")
				.setPaint(paint)
				.setSize(28)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.BOTTOM_OF, textFokkenGamBra).build();

		Text textDaaiAnies = TextBuilder
				.create(" S b  sb ")
				.setPaint(paint)
				.setSize(34)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textHaai).build();

		Text texThyLamInnie = TextBuilder
				.create(" S sb ")
				.setPaint(paint)
				.setSize(24)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.RIGHT_OF, textDaaiAnies).build();

		Text textThrowDamn = TextBuilder
				.create(" S b b ")
				.setPaint(paint)
				.setSize(26)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.BOTTOM_OF | Align.CENTER_OF, texThyLamInnie).build();

		Text textDevilishGang = TextBuilder
				.create(" S B ")
				.setPaint(paint)
				.setSize(25)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textThrowDamn).build();

		Text textSignsInTheAir = TextBuilder
				.create(" S B ")
				.setPaint(paint)
				.setSize(20)
				.setAlpha(0)
				.setColor(Color.WHITE)
				.setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textDevilishGang).build();

		textSurface.play(
				new Sequential(
						ShapeReveal.create(textDaai, 750, SideCut.show(Side.LEFT), false),
						new Parallel(ShapeReveal.create(textDaai, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(1300), ShapeReveal.create(textDaai, 1600, SideCut.show(Side.LEFT), false))),
						new Parallel(new TransSurface(1500, textBraAnies, Pivot.CENTER), ShapeReveal.create(textBraAnies, 2300, SideCut.show(Side.LEFT), false)),
						Delay.duration(1500),
						new Parallel(new TransSurface(1750, textFokkenGamBra, Pivot.CENTER), Slide.showFrom(Side.LEFT, textFokkenGamBra, 750), ChangeColor.to(textFokkenGamBra, 750, Color.WHITE)),
						Delay.duration(1500),
						new Parallel(TransSurface.toCenter(textHaai, 1500), Rotate3D.showFromSide(textHaai, 1750, Pivot.TOP)),
						new Parallel(TransSurface.toCenter(textDaaiAnies, 1500), Slide.showFrom(Side.TOP, textDaaiAnies, 1500)),
						new Parallel(TransSurface.toCenter(texThyLamInnie, 1750), Slide.showFrom(Side.LEFT, texThyLamInnie, 1500)),
						Delay.duration(1500),
						new Parallel(
								new TransSurface(2500, textSignsInTheAir, Pivot.CENTER),
								new Sequential(
										new Sequential(ShapeReveal.create(textThrowDamn, 1500, Circle.show(Side.CENTER, Direction.OUT), false)),
										new Sequential(ShapeReveal.create(textDevilishGang, 1500, Circle.show(Side.CENTER, Direction.OUT), false)),
										new Sequential(ShapeReveal.create(textSignsInTheAir, 1500, Circle.show(Side.CENTER, Direction.OUT), false))
								)
						),
						Delay.duration(1200),
						new Parallel(
								ShapeReveal.create(textThrowDamn, 1500, SideCut.hide(Side.LEFT), true),
								new Sequential(Delay.duration(250), ShapeReveal.create(textDevilishGang, 500, SideCut.hide(Side.LEFT), true)),
								new Sequential(Delay.duration(500), ShapeReveal.create(textSignsInTheAir, 500, SideCut.hide(Side.LEFT), true)),
								Alpha.hide(texThyLamInnie, 500),
								Alpha.hide(textDaaiAnies, 500),
								Alpha.hide(textHaai, 500),
								Alpha.hide(textFokkenGamBra, 500),
								Alpha.hide(textBraAnies, 500),
								Alpha.hide(textDaai, 500)

						)
				)
		);

	}

}
