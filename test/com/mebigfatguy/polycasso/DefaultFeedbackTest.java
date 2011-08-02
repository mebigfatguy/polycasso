package com.mebigfatguy.polycasso;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultFeedbackTest {

    private static final long ONE_COMPLETE_GRID_COLOR_DIFFERENCE = 16L * ((255L*255L) + (255L*255L));
    DefaultFeedback feedBack;
    private BufferedImage target;

    @Before
    public void setup() {
        feedBack = new DefaultFeedback();
        target = buildSampleImage();
        Graphics g = target.getGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(4, 4, 8, 8);
        feedBack.setTargetImage(target);
    }

    @Test
    public void testCalculateScoreWithNoChangeArea() {
        BufferedImage sampleImage = buildSampleImage();

        Score score = feedBack.calculateScore(sampleImage, null, null);
        Assert.assertEquals(4L * ONE_COMPLETE_GRID_COLOR_DIFFERENCE, score.getDelta());
    }

    @Test
    public void testCalculateScoreWithChangeAreaTotallyAbove() {
        BufferedImage sampleImage = buildSampleImage();
        Score previousScore = feedBack.calculateScore(sampleImage, null, null);

        Graphics g = sampleImage.getGraphics();
        g.setColor(Color.BLUE);
        Rectangle changedArea = new Rectangle(4, 0, 8, 2);
        g.fillRect(changedArea.x, changedArea.y, changedArea.width, changedArea.height);

        Score score = feedBack.calculateScore(sampleImage, previousScore, changedArea);
        Assert.assertEquals(5L * ONE_COMPLETE_GRID_COLOR_DIFFERENCE, score.getDelta());
    }

    @Test
    public void testCalculateScoreWithChangeAreaPartiallyAbove() {
        BufferedImage sampleImage = buildSampleImage();
        Score previousScore = feedBack.calculateScore(sampleImage, null, null);

        Graphics g = sampleImage.getGraphics();
        g.setColor(Color.BLUE);
        Rectangle changedArea = new Rectangle(4, 2, 8, 4);
        g.fillRect(changedArea.x, changedArea.y, changedArea.width, changedArea.height);

        Score score = feedBack.calculateScore(sampleImage, previousScore, changedArea);
        Assert.assertEquals(4L * ONE_COMPLETE_GRID_COLOR_DIFFERENCE, score.getDelta());
    }

    @Test
    public void testCalculateScoreWithChangeAreaTotallyIn() {
        BufferedImage sampleImage = buildSampleImage();
        Score previousScore = feedBack.calculateScore(sampleImage, null, null);

        Graphics g = sampleImage.getGraphics();
        g.setColor(Color.BLUE);
        Rectangle changedArea = new Rectangle(4, 4, 8, 4);
        g.fillRect(changedArea.x, changedArea.y, changedArea.width, changedArea.height);

        Score score = feedBack.calculateScore(sampleImage, previousScore, changedArea);
        Assert.assertEquals(2L * ONE_COMPLETE_GRID_COLOR_DIFFERENCE, score.getDelta());
    }

    @Test
    public void testCalculateScoreWithChangeAreaPartiallyBelow() {
        BufferedImage sampleImage = buildSampleImage();
        Score previousScore = feedBack.calculateScore(sampleImage, null, null);

        Graphics g = sampleImage.getGraphics();
        g.setColor(Color.BLUE);
        Rectangle changedArea = new Rectangle(4, 10, 8, 4);
        g.fillRect(changedArea.x, changedArea.y, changedArea.width, changedArea.height);

        Score score = feedBack.calculateScore(sampleImage, previousScore, changedArea);
        Assert.assertEquals(4L * ONE_COMPLETE_GRID_COLOR_DIFFERENCE, score.getDelta());
    }

    @Test
    public void testCalculateScoreWithChangeAreaTotallyBelow() {
        BufferedImage sampleImage = buildSampleImage();
        Score previousScore = feedBack.calculateScore(sampleImage, null, null);

        Graphics g = sampleImage.getGraphics();
        g.setColor(Color.BLUE);
        Rectangle changedArea = new Rectangle(4, 14, 8, 2);
        g.fillRect(changedArea.x, changedArea.y, changedArea.width, changedArea.height);

        Score score = feedBack.calculateScore(sampleImage, previousScore, changedArea);
        Assert.assertEquals(5L * ONE_COMPLETE_GRID_COLOR_DIFFERENCE, score.getDelta());
    }

    private BufferedImage buildSampleImage() {
        BufferedImage sample = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = sample.getGraphics();
        for (int y = 0; y < 16; y+=4) {
            for (int x = 0; x < 16; x+=4) {
                g.setColor((((x + y) & 0x01) == 0) ? Color.RED : Color.GREEN);
                g.fillRect(x, y, 4, 4);
            }
        }

        return sample;
    }
}
