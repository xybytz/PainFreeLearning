import sys

with open('src/com/playfulminds/GameController.java', 'r') as f:
    content = f.read()

# Replace round state variables
old_vars = """    // Particle system for reward
    private List<Particle> particles;
    private boolean isRewardAnimationPlaying;"""

new_vars = """    // Particle system for reward
    private List<Particle> particles;
    
    private enum RoundState { PLAYING, REWARD, FLIP_OUT, FLIP_IN }
    private RoundState roundState;"""

content = content.replace(old_vars, new_vars)

# Replace constructor init
old_init = """        this.particles = new ArrayList<>();
        this.isRewardAnimationPlaying = false;
        this.isHighContrastMode = false;"""

new_init = """        this.particles = new ArrayList<>();
        this.roundState = RoundState.PLAYING;
        this.isHighContrastMode = false;"""

content = content.replace(old_init, new_init)

# Replace generateNewLevel call inside update block
old_update = """    public void update() {
        boolean needsRepaint = false;

        for (VisualAsset asset : assets) {
            asset.updateAnimation();
            needsRepaint = true;
        }

        if (isRewardAnimationPlaying) {
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.update();
                if (p.life <= 0) {
                    particles.remove(i);
                }
            }
            if (particles.isEmpty()) {
                isRewardAnimationPlaying = false;
                generateNewLevel();
            }
            needsRepaint = true;
        }

        if (needsRepaint && gamePanel != null) {
            gamePanel.repaint();
        }
    }"""

new_update = """    public void update() {
        boolean needsRepaint = false;

        for (VisualAsset asset : assets) {
            asset.updateAnimation();
            needsRepaint = true;
        }

        if (roundState == RoundState.REWARD) {
            boolean particlesAlive = false;
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particle p = particles.get(i);
                p.update();
                if (p.life <= 0) {
                    particles.remove(i);
                } else {
                    particlesAlive = true;
                }
            }
            if (!particlesAlive) {
                roundState = RoundState.FLIP_OUT;
                for (VisualAsset asset : assets) {
                    asset.setTargetScaleX(0.0);
                }
            }
            needsRepaint = true;
        } else if (roundState == RoundState.FLIP_OUT) {
            boolean doneFlipping = true;
            for (VisualAsset asset : assets) {
                if (asset.getScaleX() > 0.05) doneFlipping = false;
            }
            if (doneFlipping) {
                generateNewLevel();
                for (VisualAsset asset : assets) {
                    asset.setScaleX(0.01);
                    asset.setTargetScaleX(1.0);
                }
                roundState = RoundState.FLIP_IN;
            }
            needsRepaint = true;
        } else if (roundState == RoundState.FLIP_IN) {
            boolean doneFlipping = true;
            for (VisualAsset asset : assets) {
                if (asset.getScaleX() < 0.95) doneFlipping = false;
            }
            if (doneFlipping) {
                roundState = RoundState.PLAYING;
            }
            needsRepaint = true;
        }

        if (needsRepaint && gamePanel != null) {
            gamePanel.repaint();
        }
    }"""

content = content.replace(old_update, new_update)

# Replace particle render condition
old_render = """        // Draw particles
        if (isRewardAnimationPlaying) {
            for (Particle p : particles) {
                p.draw(g2d);
            }
        }"""

new_render = """        // Draw particles
        if (roundState == RoundState.REWARD || roundState == RoundState.FLIP_OUT) {
            for (Particle p : particles) {
                p.draw(g2d);
            }
        }"""

content = content.replace(old_render, new_render)

# Replace interaction logic
old_interaction = """    public void handleClick(int x, int y) {
        if (isRewardAnimationPlaying) return;
        
        for (VisualAsset asset : assets) {
            if (asset.contains(x, y)) {
                if (asset == targetAsset) {
                    score += 10;
                    triggerReward(x, y);
                } else {
                    audioManager.playSound("error");
                }
                break;
            }
        }
    }

    public void handleMouseMove(int x, int y) {
        if (isRewardAnimationPlaying) return;
        for (VisualAsset asset : assets) {
            asset.setHovered(asset.contains(x, y));
        }
    }
    
    public void handleDrag(int x, int y) {}

    public void triggerReward(int startX, int startY) {
        audioManager.playSound("success");
        isRewardAnimationPlaying = true;
        particles.clear();
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(startX, startY));
        }
    }"""

new_interaction = """    public void handleClick(int x, int y) {
        if (roundState != RoundState.PLAYING) return;
        
        for (VisualAsset asset : assets) {
            if (asset.contains(x, y) && asset.getAlpha() > 0.5) {
                if (asset == targetAsset) {
                    score += 10;
                    for (VisualAsset a : assets) {
                        if (a != targetAsset) a.setTargetAlpha(0.0);
                    }
                    triggerReward(x, y);
                    roundState = RoundState.REWARD;
                } else {
                    audioManager.playSound("error");
                    asset.triggerShake();
                }
                break;
            }
        }
    }

    public void handleMouseMove(int x, int y) {
        if (roundState != RoundState.PLAYING) {
            for (VisualAsset asset : assets) asset.setHovered(false);
            return;
        }
        for (VisualAsset asset : assets) {
            asset.setHovered(asset.contains(x, y));
        }
    }
    
    public void handleDrag(int x, int y) {}

    public void triggerReward(int startX, int startY) {
        audioManager.playSound("success");
        particles.clear();
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(startX, startY));
        }
    }"""

content = content.replace(old_interaction, new_interaction)

with open('src/com/playfulminds/GameController.java', 'w') as f:
    f.write(content)

print("Updated GameController.java")
