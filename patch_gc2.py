import sys

with open('src/com/playfulminds/GameController.java', 'r') as f:
    content = f.read()

# 1. Add fields
old_fields = """    // Particle system for reward
    private List<Particle> particles;
    
    private enum RoundState { PLAYING, REWARD, FLIP_OUT, FLIP_IN }
    private RoundState roundState;"""

new_fields = """    // Particle system for reward
    private List<Particle> particles;
    
    private enum RoundState { PLAYING, REWARD, FLIP_OUT, FLIP_IN }
    private RoundState roundState;
    
    private int tokens = 0;
    private int idleTicks = 0;
    private boolean firstTry = true;"""
content = content.replace(old_fields, new_fields)

# 2. generateNewLevel (reset firstTry)
old_gen = """        // Play instruction for the new level
        if (targetAsset != null) {
            audioManager.playInstruction(targetAsset.getAssociatedSoundId());
        }
    }"""
new_gen = """        // Play instruction for the new level
        if (targetAsset != null) {
            audioManager.playInstruction(targetAsset.getAssociatedSoundId());
        }
        firstTry = true;
        idleTicks = 0;
    }"""
content = content.replace(old_gen, new_gen)

# 3. update (idleTicks)
old_upd = """        if (roundState == RoundState.REWARD) {"""
new_upd = """        if (roundState == RoundState.PLAYING) {
            idleTicks++;
            if (idleTicks >= 300) { // 5 seconds at ~60fps
                if (targetAsset != null) {
                    audioManager.playInstruction(targetAsset.getAssociatedSoundId());
                    targetAsset.triggerPulse();
                }
                idleTicks = 0;
            }
        }

        if (roundState == RoundState.REWARD) {"""
content = content.replace(old_upd, new_upd)

# 4. render (tokens)
old_rend = """        // Draw score as golden stars
        int visualTokens = Math.min(score / 10, 10);
        for (int i = 0; i < visualTokens; i++) {
            drawSmallStar(g2d, 30 + (i * 40), 40);
        }"""
new_rend = """        // Draw Visual Token Board (5 stars)
        for (int i = 0; i < 5; i++) {
            drawSmallStar(g2d, 30 + (i * 40), 40, i < tokens);
        }"""
content = content.replace(old_rend, new_rend)

# 5. drawSmallStar
old_star = """    private void drawSmallStar(Graphics2D g2d, int x, int y) {
        Path2D star = new Path2D.Double();
        int rOuter = 15;
        int rInner = 6;
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i - Math.PI / 2;
            int r = (i % 2 == 0) ? rOuter : rInner;
            double px = x + Math.cos(angle) * r;
            double py = y + Math.sin(angle) * r;
            if (i == 0) star.moveTo(px, py);
            else star.lineTo(px, py);
        }
        star.closePath();
        g2d.setColor(new Color(255, 215, 0));
        g2d.fill(star);
    }"""
new_star = """    private void drawSmallStar(Graphics2D g2d, int x, int y, boolean filled) {
        Path2D star = new Path2D.Double();
        int rOuter = 15;
        int rInner = 6;
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i - Math.PI / 2;
            int r = (i % 2 == 0) ? rOuter : rInner;
            double px = x + Math.cos(angle) * r;
            double py = y + Math.sin(angle) * r;
            if (i == 0) star.moveTo(px, py);
            else star.lineTo(px, py);
        }
        star.closePath();
        if (filled) {
            g2d.setColor(new Color(255, 215, 0));
            g2d.fill(star);
        } else {
            g2d.setColor(new Color(200, 200, 200, 100)); // hollow/grey
            g2d.fill(star);
            g2d.setColor(new Color(150, 150, 150, 150));
            g2d.draw(star);
        }
    }"""
content = content.replace(old_star, new_star)

# 6. handleClick
old_click = """    public void handleClick(int x, int y) {
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
    }"""
new_click = """    public void handleClick(int x, int y) {
        if (roundState != RoundState.PLAYING) return;
        idleTicks = 0;
        
        for (VisualAsset asset : assets) {
            if (asset.contains(x, y) && asset.getAlpha() > 0.5) {
                if (asset == targetAsset) {
                    score += 10;
                    tokens++;
                    ProgressTracker.getInstance().recordResult(targetAsset.getObjectType(), firstTry);
                    
                    for (VisualAsset a : assets) {
                        if (a != targetAsset) a.setTargetAlpha(0.0);
                    }
                    
                    if (tokens >= 5) {
                        triggerBigReward(x, y);
                        tokens = 0;
                    } else {
                        triggerReward(x, y);
                    }
                    roundState = RoundState.REWARD;
                } else {
                    firstTry = false;
                    audioManager.playSound("error");
                    asset.triggerShake();
                }
                break;
            }
        }
    }"""
content = content.replace(old_click, new_click)

# 7. handleMouseMove
old_move = """    public void handleMouseMove(int x, int y) {
        if (roundState != RoundState.PLAYING) {"""
new_move = """    public void handleMouseMove(int x, int y) {
        if (roundState != RoundState.PLAYING) {"""
# wait, actually let's just reset idleTicks on move
old_move_full = """    public void handleMouseMove(int x, int y) {
        if (roundState != RoundState.PLAYING) {
            for (VisualAsset asset : assets) asset.setHovered(false);
            return;
        }
        for (VisualAsset asset : assets) {
            asset.setHovered(asset.contains(x, y));
        }
    }"""
new_move_full = """    public void handleMouseMove(int x, int y) {
        if (roundState != RoundState.PLAYING) {
            for (VisualAsset asset : assets) asset.setHovered(false);
            return;
        }
        idleTicks = 0;
        for (VisualAsset asset : assets) {
            asset.setHovered(asset.contains(x, y));
        }
    }"""
content = content.replace(old_move_full, new_move_full)

# 8. triggerBigReward
old_trig = """    public void triggerReward(int startX, int startY) {
        audioManager.playSound("success");
        particles.clear();
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(startX, startY));
        }
    }"""
new_trig = """    public void triggerReward(int startX, int startY) {
        audioManager.playSound("success");
        particles.clear();
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(startX, startY));
        }
    }

    public void triggerBigReward(int startX, int startY) {
        audioManager.playSound("success"); // maybe play a big success later
        particles.clear();
        for (int i = 0; i < 200; i++) {
            particles.add(new Particle(400, 300)); // center screen explosion
        }
    }"""
content = content.replace(old_trig, new_trig)

with open('src/com/playfulminds/GameController.java', 'w') as f:
    f.write(content)
print("Updated GameController.java")
