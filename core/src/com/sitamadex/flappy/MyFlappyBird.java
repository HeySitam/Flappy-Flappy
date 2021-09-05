package com.sitamadex.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyFlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture bg;
    Texture base;
    Texture topTube;
    Texture bottomTube;
    Texture[] birds;
    Texture gameOver;
    Texture tabToPlay;
    Texture replay;

    // for bird flying animation
    int birdState = 0;

    // starting the game
    int gameState = 0;

    // bird movement control
    float velocity = 0;
    float birdY = 0;

    // for base height declaration
    float baseHt = 0;

    // for screen height declaration
    float screenHt = 0;

    // for gap between two tube
    float gap = 250;

    // random seed generator
    Random rand;

    // for randomly tube Height changing
    float[] tubeHtRand = new float[6];

    // for tube position check
    boolean[] upDownChk = new boolean[6];

    // for tube width declaration
    float tubeWidth;

    // for tube's extra height declaration
    float tubeVarHeight;

    // for moving tubes
    float tubeVelocity = 4;
    float[] tubeX = new float[6];
    int tubeNum = 6;
    float tubeDistance;

    // for score
    int score = 0;
    int scoringTube = 0;

    // for font
    BitmapFont font;
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    Rectangle upperTubeBox;
    Rectangle lowerTubeBox;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bg = new Texture("background_day.png");
        base = new Texture("base.png");
        topTube = new Texture("top_tube.png");
        gameOver = new Texture("gameover.png");
        tabToPlay = new Texture("tab_to_play.png");
        replay = new Texture("replay.png");
        tubeWidth = 150;
        tubeVarHeight = 500;
        bottomTube = new Texture("bottom_tube.png");
        birds = new Texture[3];
        birds[0] = new Texture("bluebird_upflap.png");
        birds[1] = new Texture("bluebird_midflap.png");
        birds[2] = new Texture("bluebird_downflap.png");
        baseHt = (float) Gdx.graphics.getHeight() / 7;
        screenHt = Gdx.graphics.getHeight() - baseHt;
        rand = new Random();
        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        upperTubeBox = new Rectangle();
        lowerTubeBox = new Rectangle();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(6);
        startGameSetup();
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        //Bird Shape rendering
        birdCircle.set((float) Gdx.graphics.getWidth() / 2 + (float) birds[birdState].getWidth() / 2, birdY + (float) birds[birdState].getHeight(), 35);

        // Game Logic
        if (gameState == 1) {
            if (tubeX[scoringTube] < (float) Gdx.graphics.getWidth() / 2) {
                score++;
                Gdx.app.log("Score", String.valueOf(score));
                if (scoringTube < tubeNum - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }
            if (Gdx.input.justTouched()) {
                velocity = -20;
            }

            for (int i = 0; i < tubeNum; i++) {
                if (upDownChk[i]) {
                    batch.begin();
                    batch.draw(bottomTube,
                            tubeX[i],
                            baseHt - tubeVarHeight - tubeHtRand[i],
                            tubeWidth,
                            screenHt / 2 - gap / 2 + tubeVarHeight);
                    batch.end();
                    lowerTubeBox.set(
                            tubeX[i],
                            baseHt - tubeVarHeight - tubeHtRand[i],
                            tubeWidth,
                            screenHt / 2 - gap / 2 + tubeVarHeight
                    );
                    batch.begin();
                    batch.draw(topTube,
                            tubeX[i],
                            Gdx.graphics.getHeight() - (float) screenHt / 2 + gap / 2 - tubeHtRand[i],
                            tubeWidth,
                            (float) screenHt / 2 - gap / 2 + tubeVarHeight);
                    batch.end();
                    upperTubeBox.set(
                            tubeX[i],
                            Gdx.graphics.getHeight() - (float) screenHt / 2 + gap / 2 - tubeHtRand[i],
                            tubeWidth,
                            (float) screenHt / 2 - gap / 2 + tubeVarHeight
                    );
//                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                    shapeRenderer.setColor(Color.GREEN);
//                    shapeRenderer.rect(lowerTubeBox.x, lowerTubeBox.y, lowerTubeBox.width, lowerTubeBox.height);
//                    shapeRenderer.rect(upperTubeBox.x, upperTubeBox.y, upperTubeBox.width, upperTubeBox.height);
//                    shapeRenderer.end();

                } else {
                    batch.begin();
                    batch.draw(bottomTube,
                            tubeX[i],
                            baseHt - tubeVarHeight + tubeHtRand[i],
                            tubeWidth,
                            screenHt / 2 - gap / 2 + tubeVarHeight);
                    batch.end();
                    lowerTubeBox.set(
                            tubeX[i],
                            baseHt - tubeVarHeight + tubeHtRand[i],
                            tubeWidth,
                            screenHt / 2 - gap / 2 + tubeVarHeight
                    );
                    batch.begin();
                    batch.draw(topTube,
                            tubeX[i],
                            Gdx.graphics.getHeight() - (float) screenHt / 2 + gap / 2 + tubeHtRand[i],
                            tubeWidth,
                            (float) screenHt / 2 - gap / 2 + tubeVarHeight);
                    batch.end();
                    upperTubeBox.set(
                            tubeX[i],
                            Gdx.graphics.getHeight() - (float) screenHt / 2 + gap / 2 + tubeHtRand[i],
                            tubeWidth,
                            (float) screenHt / 2 - gap / 2 + tubeVarHeight
                    );
//                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                    shapeRenderer.setColor(Color.GREEN);
//                    shapeRenderer.rect(lowerTubeBox.x, lowerTubeBox.y, lowerTubeBox.width, lowerTubeBox.height);
//                    shapeRenderer.rect(upperTubeBox.x, upperTubeBox.y, upperTubeBox.width, upperTubeBox.height);
//                    shapeRenderer.end();
                }

                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] = tubeNum * tubeDistance;
                } else {
                    tubeX[i] -= tubeVelocity;
                }
                if (Intersector.overlaps(birdCircle, upperTubeBox) || Intersector.overlaps(birdCircle, lowerTubeBox) || birdY == baseHt) {
                    Gdx.app.log("chk", "Overlaps");
                    gameState = 2;
                }
            }
            batch.begin();
            batch.draw(base, 0, 0, Gdx.graphics.getWidth(), baseHt);
            batch.end();

            if ((birdY > baseHt)) {
                velocity += 2;
                birdY -= velocity;
            } else {
                gameState = 2;
            }
        }
        // for first screen
        else if (gameState == 0) {
            batch.begin();
            batch.draw(base, 0, 0, Gdx.graphics.getWidth(), baseHt);
            batch.draw(tabToPlay,(float)Gdx.graphics.getWidth()/2-100,Gdx.graphics.getHeight()-tabToPlay.getHeight()-200,200,200);
            batch.end();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Gdx.input.justTouched()) gameState = 1;
        }
        // game over logic
        else if (gameState == 2) {
            birdState = 0;
            batch.begin();
            batch.draw(base, 0, 0, Gdx.graphics.getWidth(), baseHt);
            batch.draw(gameOver, (float) Gdx.graphics.getWidth() / 2 - 192, (float) Gdx.graphics.getHeight() / 2 - 42, 384, 84);
            batch.draw(replay, (float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() - 438,500,238);
            batch.draw(birds[birdState], (float) Gdx.graphics.getWidth() / 2 - (float) birds[birdState].getWidth() / 2, birdY, 80, 56);
            batch.end();
            if (Gdx.input.justTouched()) {
                startGameSetup();
                birdState = 0;
                gameState = 0;
                velocity = 0;
                score = 0;
                scoringTube = 0;
            }
        }

        // for bird flying animation
            if (birdState == 0)
                birdState = 1;
            else if (birdState == 1)
                birdState = 2;
            else birdState = 0;
            batch.begin();
            batch.draw(birds[birdState], (float) Gdx.graphics.getWidth() / 2 - (float) birds[birdState].getWidth() / 2, birdY, 80, 56);
            font.draw(batch, String.valueOf(score), 60, 100);
            batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        base.dispose();
        birds[0].dispose();
        birds[1].dispose();
        birds[2].dispose();
        gameOver.dispose();
        tabToPlay.dispose();
    }

    // start game
    private void startGameSetup() {
        birdY = (float) Gdx.graphics.getHeight() / 2 - (float) birds[birdState].getHeight() / 2;
        tubeDistance = (float) Gdx.graphics.getWidth() / 2 + 100;
        for (int i = 0; i < tubeNum; i++) {
            tubeX[i] = (float) Gdx.graphics.getWidth() / 2 - tubeWidth / 2 + Gdx.graphics.getWidth() + i * tubeDistance;
            tubeHtRand[i] = rand.nextInt(200) + 20;
            upDownChk[i] = rand.nextBoolean();
        }
    }
}
