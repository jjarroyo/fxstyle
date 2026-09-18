package com.jjarroyo.animation;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility engine for applying fluent, declarative animations to JavaFX nodes.
 * Inspired by Animate.css and modern design systems.
 */
public class JAnimation {

    public static final Duration DEFAULT_DURATION = Duration.millis(350);

    /**
     * Animation task builder for configuring delays, callbacks, and playback.
     */
    public static class AnimationTask {
        private final Animation animation;

        public AnimationTask(Animation animation) {
            this.animation = animation;
        }

        public AnimationTask delay(Duration delay) {
            animation.setDelay(delay);
            return this;
        }

        public AnimationTask onFinished(Runnable callback) {
            animation.setOnFinished(e -> {
                if (callback != null) callback.run();
            });
            return this;
        }

        public AnimationTask cycleCount(int count) {
            animation.setCycleCount(count);
            return this;
        }

        public AnimationTask autoReverse(boolean autoReverse) {
            animation.setAutoReverse(autoReverse);
            return this;
        }

        public AnimationTask play() {
            animation.play();
            return this;
        }

        public void stop() {
            animation.stop();
        }

        public Animation getAnimation() {
            return animation;
        }
    }

    // ── FADE ANIMATIONS ──────────────────────────────────────────────────

    public static AnimationTask fadeIn(Node node) {
        return fadeIn(node, DEFAULT_DURATION);
    }

    public static AnimationTask fadeIn(Node node, Duration duration) {
        node.setOpacity(0.0);
        node.setVisible(true);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setInterpolator(Interpolator.EASE_OUT);

        AnimationTask task = new AnimationTask(fade);
        task.play();
        return task;
    }

    public static AnimationTask fadeOut(Node node) {
        return fadeOut(node, DEFAULT_DURATION);
    }

    public static AnimationTask fadeOut(Node node, Duration duration) {
        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(node.getOpacity());
        fade.setToValue(0.0);
        fade.setInterpolator(Interpolator.EASE_IN);

        AnimationTask task = new AnimationTask(fade);
        task.onFinished(() -> node.setVisible(false));
        task.play();
        return task;
    }

    // ── SLIDE ANIMATIONS ─────────────────────────────────────────────────

    public static AnimationTask slideInUp(Node node) {
        return slideInUp(node, DEFAULT_DURATION, 20);
    }

    public static AnimationTask slideInUp(Node node, Duration duration, double distance) {
        node.setTranslateY(distance);
        node.setOpacity(0.0);
        node.setVisible(true);

        TranslateTransition translate = new TranslateTransition(duration, node);
        translate.setFromY(distance);
        translate.setToY(0);
        translate.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        AnimationTask task = new AnimationTask(parallel);
        task.play();
        return task;
    }

    public static AnimationTask slideInDown(Node node) {
        return slideInDown(node, DEFAULT_DURATION, 20);
    }

    public static AnimationTask slideInDown(Node node, Duration duration, double distance) {
        node.setTranslateY(-distance);
        node.setOpacity(0.0);
        node.setVisible(true);

        TranslateTransition translate = new TranslateTransition(duration, node);
        translate.setFromY(-distance);
        translate.setToY(0);
        translate.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        AnimationTask task = new AnimationTask(parallel);
        task.play();
        return task;
    }

    public static AnimationTask slideInLeft(Node node) {
        return slideInLeft(node, DEFAULT_DURATION, 20);
    }

    public static AnimationTask slideInLeft(Node node, Duration duration, double distance) {
        node.setTranslateX(-distance);
        node.setOpacity(0.0);
        node.setVisible(true);

        TranslateTransition translate = new TranslateTransition(duration, node);
        translate.setFromX(-distance);
        translate.setToX(0);
        translate.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        AnimationTask task = new AnimationTask(parallel);
        task.play();
        return task;
    }

    public static AnimationTask slideInRight(Node node) {
        return slideInRight(node, DEFAULT_DURATION, 20);
    }

    public static AnimationTask slideInRight(Node node, Duration duration, double distance) {
        node.setTranslateX(distance);
        node.setOpacity(0.0);
        node.setVisible(true);

        TranslateTransition translate = new TranslateTransition(duration, node);
        translate.setFromX(distance);
        translate.setToX(0);
        translate.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        AnimationTask task = new AnimationTask(parallel);
        task.play();
        return task;
    }

    // ── ZOOM / POP ANIMATIONS ─────────────────────────────────────────────

    public static AnimationTask zoomIn(Node node) {
        return zoomIn(node, DEFAULT_DURATION);
    }

    public static AnimationTask zoomIn(Node node, Duration duration) {
        node.setScaleX(0.7);
        node.setScaleY(0.7);
        node.setOpacity(0.0);
        node.setVisible(true);

        ScaleTransition scale = new ScaleTransition(duration, node);
        scale.setFromX(0.7);
        scale.setFromY(0.7);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(scale, fade);
        AnimationTask task = new AnimationTask(parallel);
        task.play();
        return task;
    }

    public static AnimationTask popIn(Node node) {
        return popIn(node, Duration.millis(400));
    }

    public static AnimationTask popIn(Node node, Duration duration) {
        node.setScaleX(0.5);
        node.setScaleY(0.5);
        node.setOpacity(0.0);
        node.setVisible(true);

        // Stage 1: Scale from 0.5 to 1.12 (Overshoot)
        ScaleTransition s1 = new ScaleTransition(duration.multiply(0.7), node);
        s1.setFromX(0.5);
        s1.setFromY(0.5);
        s1.setToX(1.12);
        s1.setToY(1.12);
        s1.setInterpolator(Interpolator.EASE_OUT);

        // Stage 2: Settle from 1.12 to 1.0
        ScaleTransition s2 = new ScaleTransition(duration.multiply(0.3), node);
        s2.setFromX(1.12);
        s2.setFromY(1.12);
        s2.setToX(1.0);
        s2.setToY(1.0);
        s2.setInterpolator(Interpolator.EASE_IN);

        SequentialTransition scaleSeq = new SequentialTransition(s1, s2);

        FadeTransition fade = new FadeTransition(duration.multiply(0.5), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ParallelTransition parallel = new ParallelTransition(scaleSeq, fade);
        AnimationTask task = new AnimationTask(parallel);
        task.play();
        return task;
    }

    // ── ATTENTION SEEKERS (FEEDBACK) ──────────────────────────────────────

    public static AnimationTask shake(Node node) {
        return shake(node, Duration.millis(450));
    }

    public static AnimationTask shake(Node node, Duration duration) {
        double cycleTime = duration.toMillis() / 6.0;

        TranslateTransition t1 = new TranslateTransition(Duration.millis(cycleTime), node);
        t1.setByX(-10);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(cycleTime), node);
        t2.setByX(20);
        TranslateTransition t3 = new TranslateTransition(Duration.millis(cycleTime), node);
        t3.setByX(-20);
        TranslateTransition t4 = new TranslateTransition(Duration.millis(cycleTime), node);
        t4.setByX(20);
        TranslateTransition t5 = new TranslateTransition(Duration.millis(cycleTime), node);
        t5.setByX(-20);
        TranslateTransition t6 = new TranslateTransition(Duration.millis(cycleTime), node);
        t6.setToX(0);

        SequentialTransition seq = new SequentialTransition(t1, t2, t3, t4, t5, t6);
        AnimationTask task = new AnimationTask(seq);
        task.play();
        return task;
    }

    public static AnimationTask pulse(Node node) {
        return pulse(node, Duration.millis(300));
    }

    public static AnimationTask pulse(Node node, Duration duration) {
        ScaleTransition s1 = new ScaleTransition(duration.multiply(0.5), node);
        s1.setToX(1.08);
        s1.setToY(1.08);

        ScaleTransition s2 = new ScaleTransition(duration.multiply(0.5), node);
        s2.setToX(1.0);
        s2.setToY(1.0);

        SequentialTransition seq = new SequentialTransition(s1, s2);
        AnimationTask task = new AnimationTask(seq);
        task.play();
        return task;
    }

    public static AnimationTask bounce(Node node) {
        return bounce(node, Duration.millis(500));
    }

    public static AnimationTask bounce(Node node, Duration duration) {
        double subDuration = duration.toMillis() / 4.0;

        TranslateTransition t1 = new TranslateTransition(Duration.millis(subDuration), node);
        t1.setByY(-14);
        t1.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition t2 = new TranslateTransition(Duration.millis(subDuration), node);
        t2.setToY(0);
        t2.setInterpolator(Interpolator.EASE_IN);

        TranslateTransition t3 = new TranslateTransition(Duration.millis(subDuration * 0.7), node);
        t3.setByY(-6);
        t3.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition t4 = new TranslateTransition(Duration.millis(subDuration * 0.7), node);
        t4.setToY(0);
        t4.setInterpolator(Interpolator.EASE_IN);

        SequentialTransition seq = new SequentialTransition(t1, t2, t3, t4);
        AnimationTask task = new AnimationTask(seq);
        task.play();
        return task;
    }
}
