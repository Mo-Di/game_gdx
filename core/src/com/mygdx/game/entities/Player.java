package com.mygdx.game.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.data.Direction;
import com.mygdx.game.data.MultipleChoice;
import com.mygdx.game.data.QuestionText;
import com.mygdx.game.screens.Play;
import com.mygdx.game.utils.CustomDialog;
import com.mygdx.game.utils.QuestionDialog;
import com.mygdx.game.utils.TextDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Shuni on 2/25/17.
 *
 */
public class Player extends Sprite {

    public static final float SPEED = 20f;
    public static int health = 30;
    public static final int TOTALHEALTH = 30;

    private List<Direction> movingDirs;

    private Stage stage;
    private Array<Rectangle> doorRects;
    private Array<CustomDialog> questions;
    private Array<Rectangle> collisionRects;
    private Map<Rectangle, CustomDialog> spots;

    public QuestionText qt = new QuestionText();

    public ArrayList<Explosion> explosions;


    public Player(Texture texture, Stage stage) {
        super(texture);
        this.movingDirs = new ArrayList<>();
        this.stage = stage;
        //TODO: Refactor!
        explosions = new ArrayList<>();
    }

    public void addNewDirection(Direction dir) {
        this.movingDirs.add(dir);
    }

    public void removeDirection(Direction dir) {
        this.movingDirs.remove(dir);
    }

    private void resetDirection() {
        this.movingDirs.clear();
    }



    //TODO: refactor
    public void makePlayerMove(){

        float oldXPos = getX();
        float oldYPos = getY();

        for (Direction dirInCurDir : movingDirs) {
            makeMove(dirInCurDir, 1.0f);
        }

        if (isOverlappedArray(collisionRects)) {
            popUpMessage();
            setPosition(oldXPos, oldYPos);
        }
    }

    public void makeMove(Direction direction, float scale) {
        switch (direction) {
            case UP:
                translateY(SPEED * scale);
                break;
            case DOWN:
                translateY(-SPEED * scale);
                break;
            case LEFT:
                translateX(-SPEED * scale);
                break;
            case RIGHT:
                translateX(SPEED * scale);
                break;
        }
    }


    public boolean isOverlappedArray(Array<Rectangle> rects) {
        for (Rectangle rect : rects) {
            if (isOverlapped(rect)){
                return true;
            }
        }
        return false;
    }

    public boolean isOverlapped(Rectangle rec2) {
        float p1x = getX();
        float p1y = getY() + getHeight();
        float p2x = getX() + getWidth();
        float p2y = getY();

        float p3x = rec2.getX();
        float p3y = rec2.getY() + rec2.getHeight();
        float p4x = rec2.getX() + rec2.getWidth();
        float p4y = rec2.getY();
        return (!((p2x < p3x) || (p1y < p4y) || (p1x > p4x) || (p2y > p3y)));
    }

    public Array<Rectangle> getExistingDoors() {
        Array<Rectangle> existingDoors = new Array<>();
        for (CustomDialog questionDialog : questions) {
            int i = 0;
            if (questionDialog instanceof TextDialog) {
                CustomDialog customDialog = questionDialog;
                while (customDialog instanceof TextDialog && i < 20) { //TODO: change parameter. 7: number of nested dialogs
                    customDialog = customDialog.getResponseDialog(); //@nullable if the whole chain is TextDialog
                    i++;
                }
                if (customDialog instanceof QuestionDialog) {
                    QuestionDialog question = (QuestionDialog) customDialog;
                    if (question.isAnswered()) {
                        existingDoors.add(Chapter.getKeyByValue(spots, questionDialog));
                    }
                }
            }
            if (questionDialog instanceof QuestionDialog) {
                QuestionDialog question = (QuestionDialog) questionDialog;
                if (question.isAnswered()) {
                    existingDoors.add(Chapter.getKeyByValue(spots, question));
                }
            }
        }
        return existingDoors;
    }

    private Rectangle nextDoor(Array<Rectangle> existingDoors) {
        if (isFinished(existingDoors)) {
            return doorRects.get(existingDoors.size - 1);
        }
        return (doorRects.get(existingDoors.size));
    }

    public boolean isFinished(Array<Rectangle> existingDoors) {
        return (existingDoors.size == doorRects.size);
    }

    // TODO：factor out sound playing and update current clue
    private void popUpMessage() {
        if (doorRects.random() == null || questions.random() == null) { // check if the array is empty
            return;
        }
        Array<Rectangle> existingDoors = getExistingDoors();
        Rectangle newDoor = nextDoor(existingDoors);
        // Visiting answered questions
        for (Rectangle rect : existingDoors) {
            //If it's the 5th or 9th door at Olin Rice, or 3rd door at Art, skip.
            if ((existingDoors.size >= 6 && rect == existingDoors.get(4))
                    ||(existingDoors.size >= 10 && rect == existingDoors.get(8))
                    || (existingDoors.size >= 10 && rect == existingDoors.get(2))) continue;
            if (isOverlapped(rect)) {
                Play.hitCorrectDoor.play();
                resetDirection();
                CustomDialog dialogBox = spots.get(rect);
                while (dialogBox instanceof TextDialog) {
                    dialogBox = dialogBox.getResponseDialog();
                }
                dialogBox.getResponseDialog().show(this.stage);
            }
        }

        // Visiting a new spot
        if (isOverlapped(newDoor)) {
            Play.hitCorrectDoor.play();
            resetDirection();
            CustomDialog dialogBox = spots.get(newDoor);
            if (isFinished(existingDoors)) {
                dialogBox.getResponseDialog().show(this.stage);
            }
            else {
                dialogBox.show(this.stage);
            }


            //不要删，list of clues
            Screen curScreen = ((Game) Gdx.app.getApplicationListener()).getScreen();
            if (curScreen instanceof Play) {
                Play play = (Play) curScreen;

                if ((qt.getQs().get(existingDoors.size)) instanceof MultipleChoice) {
                    MultipleChoice mc = (MultipleChoice) qt.getQs().get(existingDoors.size);
                    play.setCurClue(mc.getCorretResponse());
                }

                //Object[] old = play.sampleSelectBox;
                //play.sampleSelectBox = new Object[old.length + 1];
                //for (int i = 0; i < old.length; i++) {
                //    play.sampleSelectBox[i] =  old[i];
                //}
                //play.sampleSelectBox[old.length] = "update";
            }
        }

        for (Rectangle rect : doorRects) {
            if (isOverlapped(rect) && !existingDoors.contains(rect, true)) {
                Play.hitWrongDoor.play();
                resetDirection();
            }
        }
    }

    public Array<CustomDialog> generateQuestions(Skin skin) {

        Array<CustomDialog> questions = new Array<>();

        for (int i = 0; i < qt.getNumQuestions(); i++){
            CustomDialog td = new TextDialog("TEXT", skin, null);
            List<CustomDialog> responseDialogs = generateTextDialog(skin, 20, "ANSWER");
            CustomDialog responseDialog = responseDialogs.get(0);
            CustomDialog qd = new QuestionDialog("CLUE", skin, responseDialog);
            Object ithQuestion = qt.getNthQuestion(i);
            if (ithQuestion instanceof MultipleChoice) {
                MultipleChoice ithMC = (MultipleChoice) ithQuestion;
                if (ithMC.getQuestion().length > 1) {
                    List<CustomDialog> longQuestionChain = generateTextDialog(skin, ithMC
                            .getQuestion().length-1, "CLUE");
                    CustomDialog lastElement = longQuestionChain.get(longQuestionChain.size()-1);
                    lastElement.setResponseDialog(qd);
                    longQuestionChain.set(longQuestionChain.size()-1, lastElement);
                    CustomDialog firstElement = longQuestionChain.get(0);
                    firstElement.renderContent(ithMC);
                    questions.add(firstElement);
                }
                else {
                    qd.renderContent(ithQuestion);
                    questions.add(qd);
                }
            }
            else if (ithQuestion instanceof String) {
                td.renderContent(ithQuestion);
                questions.add(td);
            }
        }
        return questions;
    }

    // Generate num of TextDialogs in a list
    public List<CustomDialog> generateTextDialog(Skin skin, int num, String title) {
        List<CustomDialog> responseDialogs = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (i == 0) {
                CustomDialog responseDialog = new TextDialog(title, skin, null);
                responseDialogs.add(responseDialog);
            }
            else {
                CustomDialog responseDialog = new TextDialog(title, skin, responseDialogs.get(i-1));
                responseDialogs.add(responseDialog);
            }
        }
        Collections.reverse(responseDialogs);
        return responseDialogs;
    }

    public void hitEnemy(Array<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if(isOverlapped(enemy.getBoundingRectangle())) {
                health--;
                enemies.removeValue(enemy, true);
                explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                Play.punch.play();
            }
        }
    }

    public boolean isAlive(int health) {
        return (health > 0);
    }

    public void setCollisionRects(Array<Rectangle> collisionRects) {
        this.collisionRects = collisionRects;
    }

    public void setDoorRects(Array<Rectangle> doorRects) {
        this.doorRects = doorRects;
    }

    public void setQuestions(Array<CustomDialog> questions) {
        this.questions = questions;
    }

    public void setSpots(Map<Rectangle, CustomDialog> spots) {
        this.spots = spots;
    }
}
